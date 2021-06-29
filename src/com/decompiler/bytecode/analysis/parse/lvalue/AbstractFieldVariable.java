package com.decompiler.bytecode.analysis.parse.lvalue;

import com.decompiler.bytecode.analysis.parse.Expression;
import com.decompiler.bytecode.analysis.parse.LValue;
import com.decompiler.bytecode.analysis.parse.StatementContainer;
import com.decompiler.bytecode.analysis.parse.utils.LValueAssignmentCollector;
import com.decompiler.bytecode.analysis.parse.utils.SSAIdentifierFactory;
import com.decompiler.bytecode.analysis.parse.utils.SSAIdentifiers;
import com.decompiler.bytecode.analysis.types.JavaRefTypeInstance;
import com.decompiler.bytecode.analysis.types.JavaTypeInstance;
import com.decompiler.bytecode.analysis.types.discovery.InferredJavaType;
import com.decompiler.entities.ClassFile;
import com.decompiler.entities.ClassFileField;
import com.decompiler.entities.Field;
import com.decompiler.entities.constantpool.ConstantPoolEntry;
import com.decompiler.entities.constantpool.ConstantPoolEntryFieldRef;
import com.decompiler.state.TypeUsageCollector;
import com.decompiler.util.CannotLoadClassException;
import com.decompiler.util.ConfusedDecompilerException;

public abstract class AbstractFieldVariable extends AbstractLValue {

    private final ClassFileField classFileField;
    private final String failureName; // if we can't get the classfileField.
    private final JavaTypeInstance owningClass;

    AbstractFieldVariable(ConstantPoolEntry field) {
        super(getFieldType((ConstantPoolEntryFieldRef) field));
        ConstantPoolEntryFieldRef fieldRef = (ConstantPoolEntryFieldRef) field;
        this.classFileField = getField(fieldRef);
        this.failureName = fieldRef.getLocalName();
        this.owningClass = fieldRef.getClassEntry().getTypeInstance();
    }

    AbstractFieldVariable(ClassFileField field, JavaTypeInstance owningClass) {
        super(new InferredJavaType(field.getField().getJavaTypeInstance(), InferredJavaType.Source.UNKNOWN));
        this.classFileField = field;
        this.failureName = field.getFieldName();
        this.owningClass = owningClass;
    }

    AbstractFieldVariable(AbstractFieldVariable other) {
        super(other.getInferredJavaType());
        this.classFileField = other.classFileField;
        this.failureName = other.failureName;
        this.owningClass = other.owningClass;
    }

    AbstractFieldVariable(InferredJavaType type, JavaTypeInstance clazz, String varName) {
        super(type);
        this.classFileField = null;
        this.owningClass = clazz;
        this.failureName = varName;
    }

    AbstractFieldVariable(InferredJavaType type, JavaTypeInstance clazz, ClassFileField classFileField) {
        super(type);
        this.classFileField = classFileField;
        this.owningClass = clazz;
        this.failureName = null;
    }

    @Override
    public void collectTypeUsages(TypeUsageCollector collector) {
        super.collectTypeUsages(collector);
        if (classFileField != null) collector.collect(classFileField.getField().getJavaTypeInstance());
        collector.collect(owningClass);
    }

    @Override
    public void markFinal() {

    }

    @Override
    public boolean isFinal() {
        return false;
    }

    @Override
    public boolean isFakeIgnored() {
        return false;
    }

    @Override
    public void markVar() {

    }

    @Override
    public boolean isVar() {
        return false;
    }

    @Override
    public int getNumberOfCreators() {
        throw new ConfusedDecompilerException("NYI");
    }

    public JavaTypeInstance getOwningClassType() {
        return owningClass;
    }

    public String getFieldName() {
        if (classFileField == null) {
            return failureName;
        }
        return classFileField.getFieldName();
    }

    protected boolean isHiddenDeclaration() {
        if (classFileField == null) {
            return false;
        }
        return classFileField.shouldNotDisplay();
    }

    public String getRawFieldName() {
        if (classFileField == null) {
            return failureName;
        }
        return classFileField.getRawFieldName();
    }

    public ClassFileField getClassFileField() {
        return classFileField;
    }

    @Override
    public SSAIdentifiers<LValue> collectVariableMutation(SSAIdentifierFactory<LValue, ?> ssaIdentifierFactory) {
        //noinspection unchecked
        return new SSAIdentifiers(this, ssaIdentifierFactory);
    }

    @Override
    public void collectLValueAssignments(Expression assignedTo, StatementContainer statementContainer, LValueAssignmentCollector lValueAssigmentCollector) {
    }


    public static ClassFileField getField(ConstantPoolEntryFieldRef fieldRef) {
        String name = fieldRef.getLocalName();
        JavaRefTypeInstance ref = (JavaRefTypeInstance) fieldRef.getClassEntry().getTypeInstance();
        try {
            ClassFile classFile = ref.getClassFile();
            if (classFile == null) return null;

            ClassFileField field = classFile.getFieldByName(name, fieldRef.getJavaTypeInstance());
            return field;
        } catch (NoSuchFieldException ignore) {
        } catch (CannotLoadClassException ignore) {
        }
        return null;
    }


    private static InferredJavaType getFieldType(ConstantPoolEntryFieldRef fieldRef) {
        String name = fieldRef.getLocalName();
        JavaRefTypeInstance ref = (JavaRefTypeInstance) fieldRef.getClassEntry().getTypeInstance();
        try {
            ClassFile classFile = ref.getClassFile();
            if (classFile != null) {
                // this now seems rather pointless, as it's passing the type to GET the type!
                Field field = classFile.getFieldByName(name, fieldRef.getJavaTypeInstance()).getField();
                return new InferredJavaType(field.getJavaTypeInstance(), InferredJavaType.Source.FIELD, true);
            }
        } catch (CannotLoadClassException ignore) {
        } catch (NoSuchFieldException ignore) {
        }
        return new InferredJavaType(fieldRef.getJavaTypeInstance(), InferredJavaType.Source.FIELD, true);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractFieldVariable)) return false;

        AbstractFieldVariable that = (AbstractFieldVariable) o;

        if (!getFieldName().equals(that.getFieldName())) return false;
        if (owningClass != null ? !owningClass.equals(that.owningClass) : that.owningClass != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = getFieldName().hashCode();
        result = 31 * result + (owningClass != null ? owningClass.hashCode() : 0);
        return result;
    }
}

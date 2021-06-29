package com.decompiler.entities.classfilehelpers;

import java.util.List;

import com.decompiler.bytecode.analysis.parse.Expression;
import com.decompiler.bytecode.analysis.parse.expression.AbstractConstructorInvokation;
import com.decompiler.bytecode.analysis.parse.expression.ConstructorInvokationAnonymousInner;
import com.decompiler.bytecode.analysis.parse.expression.ConstructorInvokationSimple;
import com.decompiler.bytecode.analysis.parse.lvalue.StaticVariable;
import com.decompiler.bytecode.analysis.parse.utils.Pair;
import com.decompiler.bytecode.analysis.types.ClassSignature;
import com.decompiler.bytecode.analysis.types.JavaTypeInstance;
import com.decompiler.entities.*;
import com.decompiler.state.DCCommonState;
import com.decompiler.state.TypeUsageCollector;
import com.decompiler.util.MiscUtils;
import com.decompiler.util.output.Dumper;

/**
 * This isn't static - we populate it from the decoded enum information.
 */
public class ClassFileDumperEnum extends AbstractClassFileDumper {
    private static final AccessFlag[] dumpableAccessFlagsEnum = new AccessFlag[]{
            AccessFlag.ACC_PUBLIC, AccessFlag.ACC_PRIVATE, AccessFlag.ACC_PROTECTED, AccessFlag.ACC_STRICT, AccessFlag.ACC_STATIC
    };

    private final List<Pair<StaticVariable, AbstractConstructorInvokation>> entries;

    public ClassFileDumperEnum(DCCommonState dcCommonState, List<Pair<StaticVariable, AbstractConstructorInvokation>> entries) {
        super(dcCommonState);
        this.entries = entries;
    }

    private static void dumpHeader(ClassFile c, InnerClassDumpType innerClassDumpType, Dumper d) {
        d.print(getAccessFlagsString(c.getAccessFlags(), dumpableAccessFlagsEnum));

        d.print("enum ").dump(c.getThisClassConstpoolEntry().getTypeInstance()).print(" ");

        ClassSignature signature = c.getClassSignature();
        List<JavaTypeInstance> interfaces = signature.getInterfaces();
        if (!interfaces.isEmpty()) {
            d.print("implements ");
            int size = interfaces.size();
            for (int x = 0; x < size; ++x) {
                JavaTypeInstance iface = interfaces.get(x);
                d.dump(iface).print((x < (size - 1) ? "," : "")).newln();
            }
        }
    }

    private static void dumpEntry(Dumper d, Pair<StaticVariable, AbstractConstructorInvokation> entry, boolean last, JavaTypeInstance classType) {
        StaticVariable staticVariable = entry.getFirst();
        AbstractConstructorInvokation constructorInvokation = entry.getSecond();
        d.fieldName(staticVariable.getFieldName(), classType, false, true, true);

        if (constructorInvokation instanceof ConstructorInvokationSimple) {
            List<Expression> args = constructorInvokation.getArgs();
            if (args.size() > 2) {
                d.separator("(");
                for (int x = 2, len = args.size(); x < len; ++x) {
                    if (x > 2) d.print(", ");
                    d.dump(args.get(x));
                }
                d.separator(")");
            }
        } else if (constructorInvokation instanceof ConstructorInvokationAnonymousInner) {
            ((ConstructorInvokationAnonymousInner) constructorInvokation).dumpForEnum(d);
        } else {
            MiscUtils.handyBreakPoint();
        }
        if (last) {
            d.endCodeln();
        } else {
            d.print(",").newln();
        }
    }

    @Override
    public Dumper dump(ClassFile classFile, InnerClassDumpType innerClass, Dumper d) {

        if (!innerClass.isInnerClass()) {
            dumpTopHeader(classFile, d, true);
            dumpImports(d, classFile);
        }

        dumpComments(classFile, d);
        dumpAnnotations(classFile, d);
        dumpHeader(classFile, innerClass, d);
        d.separator("{").newln();
        d.indent(1);

        JavaTypeInstance classType = classFile.getClassType();

        for (int x = 0, len = entries.size(); x < len; ++x) {
            dumpEntry(d, entries.get(x), (x == len - 1), classType);
        }

        d.newln();

        List<ClassFileField> fields = classFile.getFields();
        for (ClassFileField field : fields) {
            if (field.shouldNotDisplay()) continue;
            field.dump(d, classFile);
        }
        List<Method> methods = classFile.getMethods();
        if (!methods.isEmpty()) {
            for (Method method : methods) {
                if (method.hiddenState() != Method.Visibility.Visible) continue;
                d.newln();
                method.dump(d, true);
            }
        }
        classFile.dumpNamedInnerClasses(d);
        d.indent(-1);
        d.print("}").newln();

        return d;
    }

    @Override
    public void collectTypeUsages(TypeUsageCollector collector) {
        for (Pair<StaticVariable, AbstractConstructorInvokation> entry : entries) {
            collector.collectFrom(entry.getFirst());
            collector.collectFrom(entry.getSecond());
        }
    }
}

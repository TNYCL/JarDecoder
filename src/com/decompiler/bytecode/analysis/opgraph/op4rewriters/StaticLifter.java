package com.decompiler.bytecode.analysis.opgraph.op4rewriters;

import java.util.LinkedList;
import java.util.List;

import com.decompiler.bytecode.analysis.opgraph.Op04StructuredStatement;
import com.decompiler.bytecode.analysis.opgraph.op4rewriters.util.MiscStatementTools;
import com.decompiler.bytecode.analysis.parse.LValue;
import com.decompiler.bytecode.analysis.parse.lvalue.StaticVariable;
import com.decompiler.bytecode.analysis.structured.StructuredStatement;
import com.decompiler.bytecode.analysis.structured.statement.StructuredAssignment;
import com.decompiler.bytecode.analysis.structured.statement.StructuredComment;
import com.decompiler.entities.AccessFlag;
import com.decompiler.entities.ClassFile;
import com.decompiler.entities.ClassFileField;
import com.decompiler.entities.Method;
import com.decompiler.util.collections.Functional;
import com.decompiler.util.functors.Predicate;

public class StaticLifter {

    private final ClassFile classFile;

    public StaticLifter(ClassFile classFile) {
        this.classFile = classFile;
    }

    public void liftStatics(Method staticInit) {

        // All uninitialised static fields, in definition order.
        LinkedList<ClassFileField> classFileFields = new LinkedList<ClassFileField>(Functional.filter(classFile.getFields(), new Predicate<ClassFileField>() {
            @Override
            public boolean test(ClassFileField in) {
                if (!in.getField().testAccessFlag(AccessFlag.ACC_STATIC)) return false;
                if (in.getField().testAccessFlag(AccessFlag.ACC_SYNTHETIC)) return false;
                if (in.getInitialValue() != null) return false;
                return true;
            }
        }));
        if (classFileFields.isEmpty()) return;

        /* We use a LUDICROUSLY simple plan - while the first line is a valid static initialiser, we move it into
         * static init code.
         * (Only exception, we skip over comments).
         *
         * (We also need to make sure that initialisation is performed in declaration order).
         *
         * This means we don't need to worry about illegal 'natural' initialisation code, as we can't have any
         * temporaries, and we can't be out of order.
         */
        List<Op04StructuredStatement> statements = MiscStatementTools.getBlockStatements(staticInit.getAnalysis());
        if (statements == null) return;

        for (Op04StructuredStatement statement : statements) {
            StructuredStatement structuredStatement = statement.getStatement();
            if (structuredStatement instanceof StructuredComment) continue;
            if (!(structuredStatement instanceof StructuredAssignment)) break;

            StructuredAssignment assignment = (StructuredAssignment) structuredStatement;
            if (!liftStatic(assignment, classFileFields)) return;
        }
    }

    private boolean liftStatic(StructuredAssignment assignment, LinkedList<ClassFileField> classFileFields) {
        LValue lValue = assignment.getLvalue();
        if (!(lValue instanceof StaticVariable)) return false;
        StaticVariable fieldVariable = (StaticVariable) lValue;
        ClassFileField field;
        try {
            field = classFile.getFieldByName(fieldVariable.getFieldName(), fieldVariable.getInferredJavaType().getJavaTypeInstance());
        } catch (NoSuchFieldException e) {
            return false;
        }
        if (classFileFields.isEmpty()) return false;
        if (field != classFileFields.getFirst()) return false;
        classFileFields.removeFirst();

        // by definition, but let's check again....
        if (field.getInitialValue() != null) return false;

        field.setInitialValue(assignment.getRvalue());
        assignment.getContainer().nopOut();
        return true;
    }
}

package com.decompiler.bytecode.analysis.opgraph.op3rewriters;

import java.util.List;

import com.decompiler.bytecode.analysis.opgraph.Op03SimpleStatement;
import com.decompiler.bytecode.analysis.parse.Expression;
import com.decompiler.bytecode.analysis.parse.LValue;
import com.decompiler.bytecode.analysis.parse.expression.LValueExpression;
import com.decompiler.bytecode.analysis.parse.expression.Literal;
import com.decompiler.bytecode.analysis.parse.expression.StackValue;
import com.decompiler.bytecode.analysis.parse.lvalue.FieldVariable;
import com.decompiler.bytecode.analysis.parse.lvalue.StaticVariable;
import com.decompiler.bytecode.analysis.parse.statement.AssignmentSimple;
import com.decompiler.bytecode.analysis.parse.statement.ExpressionStatement;
import com.decompiler.entities.Field;
import com.decompiler.entities.exceptions.ExceptionCheckSimple;
import com.decompiler.util.collections.Functional;

public class PointlessExpressions {

    // Expression statements which can't have any effect can be removed.
    static void removePointlessExpressionStatements(List<Op03SimpleStatement> statements) {
        List<Op03SimpleStatement> exrps = Functional.filter(statements, new TypeFilter<ExpressionStatement>(ExpressionStatement.class));
        for (Op03SimpleStatement esc : exrps) {
            ExpressionStatement es = (ExpressionStatement) esc.getStatement();
            Expression expression = es.getExpression();
            if (isSafeToIgnore(expression)) {
                esc.nopOut();
            }
        }
        List<Op03SimpleStatement> sas = Functional.filter(statements, new TypeFilter<AssignmentSimple>(AssignmentSimple.class));
        for (Op03SimpleStatement ass : sas) {
            AssignmentSimple assignmentSimple = (AssignmentSimple) ass.getStatement();
            LValue lValue = assignmentSimple.getCreatedLValue();
            if (lValue instanceof FieldVariable) continue;
            Expression rValue = assignmentSimple.getRValue();
            if (rValue.getClass() == LValueExpression.class) {
                LValueExpression lValueExpression = (LValueExpression) rValue;
                LValue lFromR = lValueExpression.getLValue();
                if (lFromR.equals(lValue)) {
                    ass.nopOut();
                }
            }
        }
    }

    public static boolean isSafeToIgnore(Expression expression) {
        return (expression instanceof LValueExpression && !expression.canThrow(ExceptionCheckSimple.INSTANCE)) ||
                expression instanceof StackValue ||
                expression instanceof Literal;
    }
}

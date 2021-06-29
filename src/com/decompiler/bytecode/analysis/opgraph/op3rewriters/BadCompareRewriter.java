package com.decompiler.bytecode.analysis.opgraph.op3rewriters;

import java.util.List;

import com.decompiler.bytecode.analysis.loc.BytecodeLoc;
import com.decompiler.bytecode.analysis.opgraph.Op03SimpleStatement;
import com.decompiler.bytecode.analysis.parse.Expression;
import com.decompiler.bytecode.analysis.parse.LValue;
import com.decompiler.bytecode.analysis.parse.StatementContainer;
import com.decompiler.bytecode.analysis.parse.expression.ArithOp;
import com.decompiler.bytecode.analysis.parse.expression.ArithmeticOperation;
import com.decompiler.bytecode.analysis.parse.expression.AssignmentExpression;
import com.decompiler.bytecode.analysis.parse.expression.CompOp;
import com.decompiler.bytecode.analysis.parse.expression.ComparisonOperation;
import com.decompiler.bytecode.analysis.parse.expression.LValueExpression;
import com.decompiler.bytecode.analysis.parse.expression.Literal;
import com.decompiler.bytecode.analysis.parse.expression.TernaryExpression;
import com.decompiler.bytecode.analysis.parse.lvalue.FieldVariable;
import com.decompiler.bytecode.analysis.parse.lvalue.LocalVariable;
import com.decompiler.bytecode.analysis.parse.rewriters.AbstractExpressionRewriter;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriterFlags;
import com.decompiler.bytecode.analysis.parse.utils.SSAIdentifiers;
import com.decompiler.bytecode.analysis.variables.VariableFactory;

public class BadCompareRewriter extends AbstractExpressionRewriter {
    private final VariableFactory vf;

    BadCompareRewriter(VariableFactory vf) {
        this.vf = vf;
    }

    public void rewrite(List<Op03SimpleStatement> op03SimpleParseNodes) {
        for (Op03SimpleStatement stm : op03SimpleParseNodes) {
            stm.rewrite(this);
        }
    }

    @Override
    public Expression rewriteExpression(Expression expression, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer, ExpressionRewriterFlags flags) {
        if (expression instanceof ArithmeticOperation) {
            ArithmeticOperation operation = (ArithmeticOperation) expression;
            ArithOp op = operation.getOp();
            if (op.isTemporary()) {
                expression = rewriteTemporary(operation);
            }
        }
        return expression.applyExpressionRewriter(this, ssaIdentifiers, statementContainer, flags);
    }

    private Expression rewriteTemporary(ArithmeticOperation arith) {
        Expression lhs = arith.getLhs();
        Expression rhs = arith.getRhs();
        boolean safe = isSideEffectFree(lhs) && isSideEffectFree(rhs);
        ComparisonOperation compareEq;
        if (safe) {
            compareEq = new ComparisonOperation(arith.getLoc(), lhs, rhs, CompOp.EQ);
        } else {
            LValue tmp = vf.tempVariable(lhs.getInferredJavaType());
            Expression zero = Literal.getLiteralOrNull(lhs.getInferredJavaType().getRawType(), lhs.getInferredJavaType(), 0);
            if (zero == null) {
                zero = Literal.INT_ZERO;
            }
            compareEq = new ComparisonOperation(arith.getLoc(), new AssignmentExpression(BytecodeLoc.NONE, tmp, new ArithmeticOperation(arith.getLoc(), lhs, rhs, ArithOp.MINUS)), zero, CompOp.EQ);
            lhs = new LValueExpression(tmp);
            rhs = zero;
        }
        switch (arith.getOp()) {
            case LCMP:
                // CmpG will return 1 if either value is nan.
            case DCMPG:
            case FCMPG:
                return new TernaryExpression(BytecodeLoc.NONE, compareEq, Literal.INT_ZERO,
                        new TernaryExpression(arith.getLoc(), new ComparisonOperation(arith.getLoc(), lhs, rhs, CompOp.LT), Literal.MINUS_ONE, Literal.INT_ONE));
            // CmpL will return -1 if either value is nan.
            case DCMPL:
            case FCMPL:
                return new TernaryExpression(BytecodeLoc.NONE, compareEq, Literal.INT_ZERO,
                        new TernaryExpression(arith.getLoc(), new ComparisonOperation(arith.getLoc(), lhs, rhs, CompOp.GT), Literal.INT_ONE, Literal.MINUS_ONE));
        }
        return arith;
    }

    private boolean isSideEffectFree(Expression lhs) {
        if (!(lhs instanceof LValueExpression)) return false;
        LValue lv = ((LValueExpression) lhs).getLValue();
        // don't even trust field variables, in case we've snuck a side effect into the object.
        return (lv instanceof LocalVariable);
    }
}

package com.decompiler.bytecode.analysis.parse.rewriters;

import java.util.Map;

import com.decompiler.bytecode.analysis.parse.Expression;
import com.decompiler.bytecode.analysis.parse.LValue;
import com.decompiler.bytecode.analysis.parse.StatementContainer;
import com.decompiler.bytecode.analysis.parse.expression.ConditionalExpression;
import com.decompiler.bytecode.analysis.parse.lvalue.StackSSALabel;
import com.decompiler.bytecode.analysis.parse.utils.SSAIdentifiers;
import com.decompiler.util.collections.LazyMap;
import com.decompiler.util.collections.MapFactory;
import com.decompiler.util.functors.UnaryFunction;

public class AccountingRewriter implements ExpressionRewriter {

    private final Map<StackSSALabel, Long> count = new LazyMap<StackSSALabel, Long>(
            MapFactory.<StackSSALabel, Long>newOrderedMap(),
            new UnaryFunction<StackSSALabel, Long>() {
                @Override
                public Long invoke(StackSSALabel arg) {
                    return 0L;
                }
            });

    @Override
    public void handleStatement(StatementContainer statementContainer) {

    }

    @Override
    public Expression rewriteExpression(Expression expression, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer, ExpressionRewriterFlags flags) {
        return expression.applyExpressionRewriter(this, ssaIdentifiers, statementContainer, flags);
    }

    @Override
    public ConditionalExpression rewriteExpression(ConditionalExpression expression, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer, ExpressionRewriterFlags flags) {
        Expression res = expression.applyExpressionRewriter(this, ssaIdentifiers, statementContainer, flags);
        return (ConditionalExpression) res;
    }

//    @Override
//    public AbstractAssignmentExpression rewriteExpression(AbstractAssignmentExpression expression, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer, ExpressionRewriterFlags flags) {
//        Expression res = expression.applyExpressionRewriter(this, ssaIdentifiers, statementContainer, flags);
//        return (AbstractAssignmentExpression) res;
//    }

    @Override
    public LValue rewriteExpression(LValue lValue, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer, ExpressionRewriterFlags flags) {
        if (lValue instanceof StackSSALabel) {
            return rewriteExpression((StackSSALabel) lValue, ssaIdentifiers, statementContainer, flags);
        }
        return lValue.applyExpressionRewriter(this, ssaIdentifiers, statementContainer, flags);
    }

    @Override
    public StackSSALabel rewriteExpression(StackSSALabel lValue, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer, ExpressionRewriterFlags flags) {
        if (flags != ExpressionRewriterFlags.LVALUE) {
//            System.out.println("Use of [" + lValue + "] in " + statementContainer);
            count.put(lValue, count.get(lValue) + 1);
        }
        return lValue;
    }

    public void flush() {
        for (Map.Entry<StackSSALabel, Long> entry : count.entrySet()) {
//            System.out.println("Usage count of " + entry.getKey() + " = " + entry.getValue());
            StackSSALabel stackSSALabel = entry.getKey();
            stackSSALabel.getStackEntry().forceUsageCount(entry.getValue());
        }
    }
}

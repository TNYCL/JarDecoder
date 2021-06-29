package com.decompiler.bytecode.analysis.opgraph.op4rewriters;

import com.decompiler.bytecode.analysis.parse.Expression;
import com.decompiler.bytecode.analysis.parse.StatementContainer;
import com.decompiler.bytecode.analysis.parse.expression.ConditionalExpression;
import com.decompiler.bytecode.analysis.parse.rewriters.AbstractExpressionRewriter;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriterFlags;
import com.decompiler.bytecode.analysis.parse.utils.SSAIdentifiers;

public class ExpressionReplacingRewriter extends AbstractExpressionRewriter {
    private final Expression search;
    private final Expression replace;

    public ExpressionReplacingRewriter(Expression search, Expression replace) {
        this.search = search;
        this.replace = replace;
    }

    @Override
    public Expression rewriteExpression(Expression expression, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer, ExpressionRewriterFlags flags) {
        if (search.equals(expression)) {
            return replace;
        }
        return expression.applyExpressionRewriter(this, ssaIdentifiers, statementContainer, flags);
    }

    @Override
    public ConditionalExpression rewriteExpression(ConditionalExpression expression, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer, ExpressionRewriterFlags flags) {
        return (ConditionalExpression) rewriteExpression((Expression)expression, ssaIdentifiers, statementContainer, flags);
    }
}

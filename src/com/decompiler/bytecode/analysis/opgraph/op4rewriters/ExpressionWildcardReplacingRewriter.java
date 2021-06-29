package com.decompiler.bytecode.analysis.opgraph.op4rewriters;

import com.decompiler.bytecode.analysis.parse.Expression;
import com.decompiler.bytecode.analysis.parse.StatementContainer;
import com.decompiler.bytecode.analysis.parse.rewriters.AbstractExpressionRewriter;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriterFlags;
import com.decompiler.bytecode.analysis.parse.utils.SSAIdentifiers;
import com.decompiler.bytecode.analysis.parse.wildcard.WildcardMatch;
import com.decompiler.util.functors.NonaryFunction;

public class ExpressionWildcardReplacingRewriter extends AbstractExpressionRewriter {
    private final WildcardMatch wildcardMatch;
    private final Expression search;
    private final NonaryFunction<Expression> replacementFunction;

    ExpressionWildcardReplacingRewriter(WildcardMatch wildcardMatch, Expression search, NonaryFunction<Expression> replacementFunction) {
        this.wildcardMatch = wildcardMatch;
        this.search = search;
        this.replacementFunction = replacementFunction;
    }

    @Override
    public Expression rewriteExpression(Expression expression, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer, ExpressionRewriterFlags flags) {
        if (expression == null) return null;
        if (search.equals(expression)) {
            Expression replacement = replacementFunction.invoke();
            if (replacement != null) {
                wildcardMatch.reset();
                return replacement;
            }
        }
        wildcardMatch.reset();
        return expression.applyExpressionRewriter(this, ssaIdentifiers, statementContainer, flags);
    }

}

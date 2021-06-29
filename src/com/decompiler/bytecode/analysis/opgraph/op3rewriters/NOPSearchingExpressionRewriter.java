package com.decompiler.bytecode.analysis.opgraph.op3rewriters;

import java.util.Set;

import com.decompiler.bytecode.analysis.parse.Expression;
import com.decompiler.bytecode.analysis.parse.StatementContainer;
import com.decompiler.bytecode.analysis.parse.rewriters.AbstractExpressionRewriter;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriterFlags;
import com.decompiler.bytecode.analysis.parse.utils.SSAIdentifiers;

public class NOPSearchingExpressionRewriter extends AbstractExpressionRewriter {

    private final Expression needle;
    private final Set<Expression> poison;
    private boolean found = false;
    private boolean poisoned = false;

    public NOPSearchingExpressionRewriter(Expression needle, Set<Expression> poison) {
        this.needle = needle;
        this.poison = poison;
    }

    @Override
    public Expression rewriteExpression(Expression expression, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer, ExpressionRewriterFlags flags) {
        if (!found) {
            if (needle.equals(expression)) {
                found = true;
                return expression;
            }
        }
        if (poison.contains(expression)) {
            poisoned = true;
        }
        return expression.applyExpressionRewriter(this, ssaIdentifiers, statementContainer, flags);
    }

    public boolean isFound() {
        return found && !poisoned;
    }
}

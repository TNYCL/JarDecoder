package com.decompiler.bytecode.analysis.parse.rewriters;

import java.util.List;

import com.decompiler.bytecode.analysis.parse.Expression;
import com.decompiler.bytecode.analysis.parse.StatementContainer;
import com.decompiler.bytecode.analysis.parse.utils.SSAIdentifiers;

public class ExpressionRewriterHelper {
    public static void applyForwards(List<Expression> list, ExpressionRewriter expressionRewriter, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer, ExpressionRewriterFlags flags) {
        for (int x = 0; x < list.size(); ++x) {
            list.set(x, expressionRewriter.rewriteExpression(list.get(x), ssaIdentifiers, statementContainer, flags));
        }
    }

    public static void applyBackwards(List<Expression> list, ExpressionRewriter expressionRewriter, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer, ExpressionRewriterFlags flags) {
        for (int x = list.size()-1; x >= 0; --x) {
            list.set(x, expressionRewriter.rewriteExpression(list.get(x), ssaIdentifiers, statementContainer, flags));
        }
    }
}

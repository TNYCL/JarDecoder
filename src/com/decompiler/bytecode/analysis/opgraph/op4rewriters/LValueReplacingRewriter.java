package com.decompiler.bytecode.analysis.opgraph.op4rewriters;

import java.util.Map;

import com.decompiler.bytecode.analysis.parse.LValue;
import com.decompiler.bytecode.analysis.parse.StatementContainer;
import com.decompiler.bytecode.analysis.parse.rewriters.AbstractExpressionRewriter;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriterFlags;
import com.decompiler.bytecode.analysis.parse.utils.SSAIdentifiers;

public class LValueReplacingRewriter extends AbstractExpressionRewriter {
    private final Map<LValue, LValue> replacements;

    public LValueReplacingRewriter(Map<LValue, LValue> replacements) {
        this.replacements = replacements;
    }

    @Override
    public LValue rewriteExpression(LValue lValue, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer, ExpressionRewriterFlags flags) {
        LValue replacement = replacements.get(lValue);
        if (replacement != null) {
            return replacement;
        }
        return lValue.applyExpressionRewriter(this, ssaIdentifiers, statementContainer, flags);
    }

}

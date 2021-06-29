package com.decompiler.bytecode.analysis.parse.expression.rewriteinterface;

import com.decompiler.bytecode.analysis.opgraph.op4rewriters.PrimitiveBoxingRewriter;
import com.decompiler.bytecode.analysis.parse.StatementContainer;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriter;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriterFlags;
import com.decompiler.bytecode.analysis.parse.utils.SSAIdentifiers;

public interface BoxingProcessor {
    // return true if boxing finished.
    boolean rewriteBoxing(PrimitiveBoxingRewriter boxingRewriter);

    void applyNonArgExpressionRewriter(ExpressionRewriter expressionRewriter, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer, ExpressionRewriterFlags flags);
}

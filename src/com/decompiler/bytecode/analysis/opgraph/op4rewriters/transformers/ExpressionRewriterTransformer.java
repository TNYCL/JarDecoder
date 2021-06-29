package com.decompiler.bytecode.analysis.opgraph.op4rewriters.transformers;

import com.decompiler.bytecode.analysis.opgraph.Op04StructuredStatement;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriter;
import com.decompiler.bytecode.analysis.structured.StructuredScope;
import com.decompiler.bytecode.analysis.structured.StructuredStatement;

public class ExpressionRewriterTransformer implements StructuredStatementTransformer {
    private final ExpressionRewriter expressionRewriter;

    public ExpressionRewriterTransformer(ExpressionRewriter expressionRewriter) {
        this.expressionRewriter = expressionRewriter;
    }

    public void transform(Op04StructuredStatement root) {
        StructuredScope structuredScope = new StructuredScope();
        root.transform(this, structuredScope);
    }

    @Override
    public StructuredStatement transform(StructuredStatement in, StructuredScope scope) {
        // This is incapable of fundamentally changing the statement type.
        // Need a different rewriter if we're going to do that.
        in.rewriteExpressions(expressionRewriter);
        in.transformStructuredChildren(this, scope);
        return in;
    }
}

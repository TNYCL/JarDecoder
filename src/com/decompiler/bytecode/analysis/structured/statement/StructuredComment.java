package com.decompiler.bytecode.analysis.structured.statement;

import java.util.List;

import com.decompiler.bytecode.analysis.loc.BytecodeLoc;
import com.decompiler.bytecode.analysis.opgraph.op4rewriters.transformers.StructuredStatementTransformer;
import com.decompiler.bytecode.analysis.parse.Expression;
import com.decompiler.bytecode.analysis.parse.expression.Literal;
import com.decompiler.bytecode.analysis.parse.literal.TypedLiteral;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriter;
import com.decompiler.bytecode.analysis.parse.utils.scope.LValueScopeDiscoverer;
import com.decompiler.bytecode.analysis.structured.StructuredScope;
import com.decompiler.bytecode.analysis.structured.StructuredStatement;
import com.decompiler.state.TypeUsageCollector;
import com.decompiler.util.output.Dumper;

public class StructuredComment extends AbstractStructuredStatement {
    public static final StructuredComment EMPTY_COMMENT = new StructuredComment("");

    private Expression expression;

    public StructuredComment(Expression expression) {
        super(BytecodeLoc.NONE);
        this.expression = expression;
    }

    public StructuredComment(String text) {
        super(BytecodeLoc.NONE);
        this.expression = new Literal(TypedLiteral.getString(text));
    }

    @Override
    public BytecodeLoc getCombinedLoc() {
        return getLoc();
    }

    @Override
    public void collectTypeUsages(TypeUsageCollector collector) {
    }

    @Override
    public Dumper dump(Dumper dumper) {
        String comment = expression.toString();
        if (comment.length() > 0) {
            dumper.comment(comment);
        }
        return dumper;
    }

    @Override
    public void traceLocalVariableScope(LValueScopeDiscoverer scopeDiscoverer) {
    }

    @Override
    public void transformStructuredChildren(StructuredStatementTransformer transformer, StructuredScope scope) {
    }

    // Lose the comments.
    @Override
    public void linearizeInto(List<StructuredStatement> out) {
    }

    @Override
    public void rewriteExpressions(ExpressionRewriter expressionRewriter) {
    }

    @Override
    public boolean isEffectivelyNOP() {
        return true;
    }
}

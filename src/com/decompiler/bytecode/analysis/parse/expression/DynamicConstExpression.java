package com.decompiler.bytecode.analysis.parse.expression;

import com.decompiler.bytecode.analysis.loc.BytecodeLoc;
import com.decompiler.bytecode.analysis.parse.Expression;
import com.decompiler.bytecode.analysis.parse.StatementContainer;
import com.decompiler.bytecode.analysis.parse.expression.misc.Precedence;
import com.decompiler.bytecode.analysis.parse.rewriters.CloneHelper;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriter;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriterFlags;
import com.decompiler.bytecode.analysis.parse.utils.EquivalenceConstraint;
import com.decompiler.bytecode.analysis.parse.utils.LValueRewriter;
import com.decompiler.bytecode.analysis.parse.utils.LValueUsageCollector;
import com.decompiler.bytecode.analysis.parse.utils.SSAIdentifiers;
import com.decompiler.util.output.Dumper;

/*
 * A dynamic constant expression isn't currently emitted by javac, so the best we can do is produce
 * something that *LOOKS* like it, and is legible.
 */
public class DynamicConstExpression extends AbstractExpression {
    private Expression content;

    public DynamicConstExpression(BytecodeLoc loc, Expression content) {
        super(loc, content.getInferredJavaType());
        this.content = content;
    }

    @Override
    public BytecodeLoc getCombinedLoc() {
        return BytecodeLoc.combine(this, content);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof DynamicConstExpression)) return false;
        return content.equals(((DynamicConstExpression) o).content);
    }

    @Override
    public Precedence getPrecedence() {
        return Precedence.WEAKEST;
    }

    @Override
    public Dumper dumpInner(Dumper d) {
        d.print(" /* dynamic constant */ ").separator("(").dump(content.getInferredJavaType().getJavaTypeInstance()).separator(")").dump(content);
        return d;
    }

    @Override
    public Expression replaceSingleUsageLValues(LValueRewriter lValueRewriter, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer) {
        content = content.replaceSingleUsageLValues(lValueRewriter, ssaIdentifiers, statementContainer);
        return this;
    }

    @Override
    public Expression applyExpressionRewriter(ExpressionRewriter expressionRewriter, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer, ExpressionRewriterFlags flags) {
        Expression newContent = content.applyExpressionRewriter(expressionRewriter, ssaIdentifiers, statementContainer, flags);
        if (newContent == content) return this;
        return new DynamicConstExpression(getLoc(), newContent);
    }

    @Override
    public Expression applyReverseExpressionRewriter(ExpressionRewriter expressionRewriter, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer, ExpressionRewriterFlags flags) {
        Expression newContent = content.applyReverseExpressionRewriter(expressionRewriter, ssaIdentifiers, statementContainer, flags);
        if (newContent == content) return this;
        return new DynamicConstExpression(getLoc(), newContent);
    }

    @Override
    public void collectUsedLValues(LValueUsageCollector lValueUsageCollector) {
        content.collectUsedLValues(lValueUsageCollector);
    }

    @Override
    public boolean equivalentUnder(Object o, EquivalenceConstraint constraint) {
        if (o == this) return true;
        if (o == null) return false;
        if (!(o instanceof  DynamicConstExpression)) return false;
        return content.equivalentUnder(((DynamicConstExpression) o).content, constraint);
    }

    @Override
    public Expression deepClone(CloneHelper cloneHelper) {
        return new DynamicConstExpression(getLoc(), content.deepClone(cloneHelper));
    }
}

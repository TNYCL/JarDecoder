package com.decompiler.bytecode.analysis.parse.expression;

import java.util.Map;
import java.util.Set;

import com.decompiler.bytecode.analysis.loc.BytecodeLoc;
import com.decompiler.bytecode.analysis.parse.Expression;
import com.decompiler.bytecode.analysis.parse.LValue;
import com.decompiler.bytecode.analysis.parse.StatementContainer;
import com.decompiler.bytecode.analysis.parse.expression.misc.Precedence;
import com.decompiler.bytecode.analysis.parse.literal.TypedLiteral;
import com.decompiler.bytecode.analysis.parse.rewriters.CloneHelper;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriter;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriterFlags;
import com.decompiler.bytecode.analysis.parse.utils.*;
import com.decompiler.state.TypeUsageCollector;
import com.decompiler.util.Troolean;
import com.decompiler.util.output.Dumper;

public class NotOperation extends AbstractExpression implements ConditionalExpression {
    private ConditionalExpression inner;

    public NotOperation(BytecodeLoc loc, ConditionalExpression lhs) {
        super(loc, lhs.getInferredJavaType());
        this.inner = lhs;
    }

    @Override
    public BytecodeLoc getCombinedLoc() {
        return BytecodeLoc.combine(this, inner);
    }

    @Override
    public void collectTypeUsages(TypeUsageCollector collector) {
        inner.collectTypeUsages(collector);
    }

    @Override
    public Expression deepClone(CloneHelper cloneHelper) {
        return new NotOperation(getLoc(), (ConditionalExpression) cloneHelper.replaceOrClone(inner));
    }

    @Override
    public int getSize(Precedence outerPrecedence) {
        return 1 + inner.getSize(getPrecedence());
    }

    @Override
    public Expression replaceSingleUsageLValues(LValueRewriter lValueRewriter, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer) {
        return this;
    }

    @Override
    public Expression applyExpressionRewriter(ExpressionRewriter expressionRewriter, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer, ExpressionRewriterFlags flags) {
        inner = expressionRewriter.rewriteExpression(inner, ssaIdentifiers, statementContainer, flags);
        return this;
    }

    @Override
    public Expression applyReverseExpressionRewriter(ExpressionRewriter expressionRewriter, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer, ExpressionRewriterFlags flags) {
        return applyExpressionRewriter(expressionRewriter, ssaIdentifiers, statementContainer, flags);
    }

    @Override
    public Precedence getPrecedence() {
        return Precedence.UNARY_OTHER;
    }

    @Override
    public Dumper dumpInner(Dumper d) {
        d.operator("!");
        inner.dumpWithOuterPrecedence(d, getPrecedence(), Troolean.NEITHER);
        return d;
    }

    @Override
    public ConditionalExpression getNegated() {
        return inner;
    }

    @Override
    public ConditionalExpression getDemorganApplied(boolean amNegating) {
        return inner.getDemorganApplied(!amNegating);
    }

    @Override
    public ConditionalExpression getRightDeep() {
        inner = inner.getRightDeep();
        return this;
    }

    @Override
    public Set<LValue> getLoopLValues() {
        return inner.getLoopLValues();
    }

    @Override
    public void collectUsedLValues(LValueUsageCollector lValueUsageCollector) {
        inner.collectUsedLValues(lValueUsageCollector);
    }

    @Override
    public ConditionalExpression optimiseForType() {
        return this;
    }

    @Override
    public ConditionalExpression simplify() {
        return ConditionalUtils.simplify(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;

        if (!(obj instanceof NotOperation)) return false;

        NotOperation other = (NotOperation) obj;
        return inner.equals(other.inner);
    }

    @Override
    public final boolean equivalentUnder(Object o, EquivalenceConstraint constraint) {
        if (o == null) return false;
        if (o == this) return true;
        if (getClass() != o.getClass()) return false;
        NotOperation other = (NotOperation) o;
        if (!constraint.equivalent(inner, other.inner)) return false;
        return true;
    }

    @Override
    public Literal getComputedLiteral(Map<LValue, Literal> display) {
        Literal lv = inner.getComputedLiteral(display);
        if (lv == null) return null;
        TypedLiteral typedLiteral = lv.getValue();
        Boolean boolVal = typedLiteral.getMaybeBoolValue();
        if (boolVal == null) return null;
        return boolVal ? Literal.TRUE : Literal.FALSE;
    }
}

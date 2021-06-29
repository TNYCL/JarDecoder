package com.decompiler.bytecode.analysis.parse.expression;

import java.util.Map;
import java.util.Set;

import com.decompiler.bytecode.analysis.loc.BytecodeLoc;
import com.decompiler.bytecode.analysis.parse.Expression;
import com.decompiler.bytecode.analysis.parse.LValue;
import com.decompiler.bytecode.analysis.parse.StatementContainer;
import com.decompiler.bytecode.analysis.parse.expression.misc.Precedence;
import com.decompiler.bytecode.analysis.parse.rewriters.CloneHelper;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriter;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriterFlags;
import com.decompiler.bytecode.analysis.parse.utils.*;
import com.decompiler.bytecode.analysis.types.RawJavaType;
import com.decompiler.bytecode.analysis.types.discovery.InferredJavaType;
import com.decompiler.state.TypeUsageCollector;
import com.decompiler.util.collections.SetFactory;
import com.decompiler.util.output.Dumper;

public class BooleanExpression extends AbstractExpression implements ConditionalExpression {
    private Expression inner;
    public static final ConditionalExpression TRUE = new BooleanExpression(Literal.TRUE);
    public static final ConditionalExpression FALSE = new BooleanExpression(Literal.FALSE);

    public BooleanExpression(Expression inner) {
        super(BytecodeLoc.NONE, new InferredJavaType(RawJavaType.BOOLEAN, InferredJavaType.Source.EXPRESSION));
        this.inner = inner;
    }

    @Override
    public BytecodeLoc getCombinedLoc() {
        return inner.getCombinedLoc();
    }

    @Override
    public int getSize(Precedence outer) {
        return 1;
    }

    public Expression getInner() {
        return inner;
    }

    @Override
    public void collectTypeUsages(TypeUsageCollector collector) {
        inner.collectTypeUsages(collector);
    }

    @Override
    public Expression deepClone(CloneHelper cloneHelper) {
        return new BooleanExpression(cloneHelper.replaceOrClone(inner));
    }

    @Override
    public Expression replaceSingleUsageLValues(LValueRewriter lValueRewriter, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer) {
        inner = inner.replaceSingleUsageLValues(lValueRewriter, ssaIdentifiers, statementContainer);
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
        return inner.getPrecedence();
    }

    /*
     * TODO : Should this just forward?
     */
    @Override
    public Dumper dumpInner(Dumper d) {
        return inner.dump(d);
    }

    @Override
    public ConditionalExpression getNegated() {
        return new NotOperation(BytecodeLoc.NONE, this);
    }

    @Override
    public ConditionalExpression getDemorganApplied(boolean amNegating) {
        if (!amNegating) return this;
        return getNegated();
    }

    @Override
    public ConditionalExpression getRightDeep() {
        return this;
    }

    protected void addIfLValue(Expression expression, Set<LValue> res) {
        if (expression instanceof LValueExpression) {
            res.add(((LValueExpression) expression).getLValue());
        }
    }

    @Override
    public Set<LValue> getLoopLValues() {
        Set<LValue> res = SetFactory.newSet();
        addIfLValue(inner, res);
        return res;
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
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof BooleanExpression)) return false;
        BooleanExpression other = (BooleanExpression) o;
        return inner.equals(other.inner);
    }

    @Override
    public final boolean equivalentUnder(Object o, EquivalenceConstraint constraint) {
        if (o == null) return false;
        if (o == this) return true;
        if (getClass() != o.getClass()) return false;
        BooleanExpression other = (BooleanExpression) o;
        if (!constraint.equivalent(inner, other.inner)) return false;
        return true;
    }

    @Override
    public Literal getComputedLiteral(Map<LValue, Literal> display) {
        return inner.getComputedLiteral(display);
    }
}

package com.decompiler.bytecode.analysis.parse.expression;

import java.util.Map;

import com.decompiler.bytecode.analysis.loc.BytecodeLoc;
import com.decompiler.bytecode.analysis.parse.Expression;
import com.decompiler.bytecode.analysis.parse.LValue;
import com.decompiler.bytecode.analysis.parse.StatementContainer;
import com.decompiler.bytecode.analysis.parse.expression.misc.Precedence;
import com.decompiler.bytecode.analysis.parse.lvalue.LocalVariable;
import com.decompiler.bytecode.analysis.parse.lvalue.StackSSALabel;
import com.decompiler.bytecode.analysis.parse.rewriters.CloneHelper;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriter;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriterFlags;
import com.decompiler.bytecode.analysis.parse.utils.*;
import com.decompiler.state.TypeUsageCollector;
import com.decompiler.util.Troolean;
import com.decompiler.util.output.Dumper;

public class AssignmentExpression extends AbstractAssignmentExpression {
    private LValue lValue;
    private Expression rValue;

    public AssignmentExpression(BytecodeLoc loc, LValue lValue, Expression rValue) {
        super(loc, lValue.getInferredJavaType());
        this.lValue = lValue;
        this.rValue = rValue;
    }

    @Override
    public Expression deepClone(CloneHelper cloneHelper) {
        return new AssignmentExpression(getLoc(), cloneHelper.replaceOrClone(lValue), cloneHelper.replaceOrClone(rValue));
    }

    @Override
    public BytecodeLoc getCombinedLoc() {
        return BytecodeLoc.combine(this, rValue);
    }

    @Override
    public void collectTypeUsages(TypeUsageCollector collector) {
        lValue.collectTypeUsages(collector);
        collector.collectFrom(rValue);
    }

    @Override
    public Precedence getPrecedence() {
        return Precedence.ASSIGNMENT;
    }

    @Override
    public Dumper dumpInner(Dumper d) {
        d.dump(lValue).operator(" = ");
        rValue.dumpWithOuterPrecedence(d, getPrecedence(), Troolean.NEITHER);
        return d;
    }

    @Override
    public Expression replaceSingleUsageLValues(LValueRewriter lValueRewriter, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer) {
        rValue = rValue.replaceSingleUsageLValues(lValueRewriter, ssaIdentifiers, statementContainer);
        lValue = lValue.replaceSingleUsageLValues(lValueRewriter, ssaIdentifiers, statementContainer);
        return this;
    }

    @Override
    public Expression applyExpressionRewriter(ExpressionRewriter expressionRewriter, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer, ExpressionRewriterFlags flags) {
        lValue = expressionRewriter.rewriteExpression(lValue, ssaIdentifiers, statementContainer, ExpressionRewriterFlags.LVALUE);
        rValue = expressionRewriter.rewriteExpression(rValue, ssaIdentifiers, statementContainer, flags);
        return this;
    }

    @Override
    public Expression applyReverseExpressionRewriter(ExpressionRewriter expressionRewriter, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer, ExpressionRewriterFlags flags) {
        return applyExpressionRewriter(expressionRewriter, ssaIdentifiers, statementContainer, flags);
    }

    // Could directly call above, but this is an aide memoir to not change behaviour of above.
    public Expression applyRValueOnlyExpressionRewriter(ExpressionRewriter expressionRewriter, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer, ExpressionRewriterFlags flags) {
        rValue = expressionRewriter.rewriteExpression(rValue, ssaIdentifiers, statementContainer, flags);
        return this;
    }

    @Override
    public boolean isValidStatement() {
        return true;
    }

    @Override
    public boolean isSelfMutatingOp1(LValue lValue, ArithOp arithOp) {
        return false;
    }

    @Override
    public ArithmeticPostMutationOperation getPostMutation() {
        throw new IllegalStateException();
    }

    @Override
    public ArithmeticPreMutationOperation getPreMutation() {
        throw new IllegalStateException();
    }

    @Override
    public LValue getUpdatedLValue() {
        return lValue;
    }

    @Override
    public void collectUsedLValues(LValueUsageCollector lValueUsageCollector) {
        lValueUsageCollector.collect(lValue, ReadWrite.WRITE);
        rValue.collectUsedLValues(lValueUsageCollector);
    }

    public LValue getlValue() {
        return lValue;
    }

    public Expression getrValue() {
        return rValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AssignmentExpression that = (AssignmentExpression) o;

        if (lValue != null ? !lValue.equals(that.lValue) : that.lValue != null) return false;
        if (rValue != null ? !rValue.equals(that.rValue) : that.rValue != null) return false;

        return true;
    }

    @Override
    public final boolean equivalentUnder(Object o, EquivalenceConstraint constraint) {
        if (o == null) return false;
        if (o == this) return true;
        if (getClass() != o.getClass()) return false;
        AssignmentExpression other = (AssignmentExpression) o;
        if (!constraint.equivalent(lValue, other.lValue)) return false;
        if (!constraint.equivalent(rValue, other.rValue)) return false;
        return true;
    }

    @Override
    public Literal getComputedLiteral(Map<LValue, Literal> display) {
        if (!(lValue instanceof StackSSALabel || lValue instanceof LocalVariable)) return null;
        Literal literal = rValue.getComputedLiteral(display);
        if (literal == null) return null;
        display.put(lValue, literal);
        return literal;
    }
}

package com.decompiler.bytecode.analysis.parse.expression;

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

public class ArithmeticMutationOperation extends AbstractMutatingAssignmentExpression {
    private LValue mutated;
    private final ArithOp op;
    private Expression mutation;

    public ArithmeticMutationOperation(BytecodeLoc loc, LValue mutated, Expression mutation, ArithOp op) {
        super(loc, mutated.getInferredJavaType());
        this.mutated = mutated;
        this.op = op;
        this.mutation = mutation;
    }

    @Override
    public BytecodeLoc getCombinedLoc() {
        return BytecodeLoc.combine(this, mutation);
    }

    @Override
    public Expression deepClone(CloneHelper cloneHelper) {
        return new ArithmeticMutationOperation(getLoc(), cloneHelper.replaceOrClone(mutated), cloneHelper.replaceOrClone(mutation), op);
    }

    @Override
    public void collectTypeUsages(TypeUsageCollector collector) {
        mutated.collectTypeUsages(collector);
        mutation.collectTypeUsages(collector);
    }

    @Override
    public Precedence getPrecedence() {
        return Precedence.ASSIGNMENT;
    }

    @Override
    public Dumper dumpInner(Dumper d) {
        d.dump(mutated).print(' ').operator(op.getShowAs() + "=").print(' ');
        mutation.dumpWithOuterPrecedence(d, getPrecedence(), Troolean.NEITHER);
        return d;
    }

    @Override
    public Expression replaceSingleUsageLValues(LValueRewriter lValueRewriter, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer) {
        Set fixed = statementContainer.getSSAIdentifiers().getFixedHere();
        // anything in fixed CANNOT be assigned to inside rvalue.
        lValueRewriter = lValueRewriter.getWithFixed(fixed);
        mutation = mutation.replaceSingleUsageLValues(lValueRewriter, ssaIdentifiers, statementContainer);
        return this;
    }

    @Override
    public Expression applyExpressionRewriter(ExpressionRewriter expressionRewriter, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer, ExpressionRewriterFlags flags) {
        mutated = expressionRewriter.rewriteExpression(mutated, ssaIdentifiers, statementContainer, ExpressionRewriterFlags.LANDRVALUE);
        mutation = expressionRewriter.rewriteExpression(mutation, ssaIdentifiers, statementContainer, flags);
        return this;
    }

    @Override
    public Expression applyReverseExpressionRewriter(ExpressionRewriter expressionRewriter, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer, ExpressionRewriterFlags flags) {
        mutation = expressionRewriter.rewriteExpression(mutation, ssaIdentifiers, statementContainer, flags);
        mutated = expressionRewriter.rewriteExpression(mutated, ssaIdentifiers, statementContainer, ExpressionRewriterFlags.LANDRVALUE);
        return this;
    }

    @Override
    public boolean isSelfMutatingOp1(LValue lValue, ArithOp arithOp) {
        return this.mutated.equals(lValue) &&
                this.op == arithOp &&
                this.mutation.equals(new Literal(TypedLiteral.getInt(1)));
    }

    @Override
    public LValue getUpdatedLValue() {
        return mutated;
    }

    public ArithOp getOp() {
        return op;
    }

    public Expression getMutation() {
        return mutation;
    }

    @Override
    public ArithmeticPostMutationOperation getPostMutation() {
        return new ArithmeticPostMutationOperation(getLoc(), mutated, op);
    }

    @Override
    public ArithmeticPreMutationOperation getPreMutation() {
        return new ArithmeticPreMutationOperation(getLoc(), mutated, op);
    }

    @Override
    public void collectUsedLValues(LValueUsageCollector lValueUsageCollector) {
        mutation.collectUsedLValues(lValueUsageCollector);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof ArithmeticMutationOperation)) return false;

        ArithmeticMutationOperation other = (ArithmeticMutationOperation) o;

        return mutated.equals(other.mutated) &&
                op.equals(other.op) &&
                mutation.equals(other.mutation);
    }

    @Override
    public final boolean equivalentUnder(Object o, EquivalenceConstraint constraint) {
        if (o == null) return false;
        if (o == this) return true;
        if (getClass() != o.getClass()) return false;
        ArithmeticMutationOperation other = (ArithmeticMutationOperation) o;
        if (!constraint.equivalent(op, other.op)) return false;
        if (!constraint.equivalent(mutated, other.mutated)) return false;
        if (!constraint.equivalent(mutation, other.mutation)) return false;
        return true;
    }

}

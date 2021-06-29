package com.decompiler.bytecode.analysis.parse.expression;

import com.decompiler.bytecode.analysis.loc.BytecodeLoc;
import com.decompiler.bytecode.analysis.parse.Expression;
import com.decompiler.bytecode.analysis.parse.LValue;
import com.decompiler.bytecode.analysis.parse.StatementContainer;
import com.decompiler.bytecode.analysis.parse.expression.misc.Precedence;
import com.decompiler.bytecode.analysis.parse.rewriters.CloneHelper;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriter;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriterFlags;
import com.decompiler.bytecode.analysis.parse.utils.*;
import com.decompiler.state.TypeUsageCollector;
import com.decompiler.util.output.Dumper;

public class ArithmeticPreMutationOperation extends AbstractMutatingAssignmentExpression {
    private LValue mutated;
    private final ArithOp op;

    public ArithmeticPreMutationOperation(BytecodeLoc loc, LValue mutated, ArithOp op) {
        super(loc, mutated.getInferredJavaType());
        this.mutated = mutated;
        this.op = op;
    }

    @Override
    public Expression deepClone(CloneHelper cloneHelper) {
        return new ArithmeticPreMutationOperation(getLoc(), cloneHelper.replaceOrClone(mutated), op);
    }

    @Override
    public BytecodeLoc getCombinedLoc() {
        return this.getLoc();
    }

    @Override
    public void collectTypeUsages(TypeUsageCollector collector) {
    }

    @Override
    public Dumper dumpInner(Dumper d) {
        return d.operator(op == ArithOp.PLUS ? "++" : "--").dump(mutated);
    }

    @Override
    public Expression replaceSingleUsageLValues(LValueRewriter lValueRewriter, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer) {
        return this;
    }

    @Override
    public Expression applyExpressionRewriter(ExpressionRewriter expressionRewriter, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer, ExpressionRewriterFlags flags) {
        mutated = expressionRewriter.rewriteExpression(mutated, ssaIdentifiers, statementContainer, ExpressionRewriterFlags.LANDRVALUE);
        return this;
    }

    @Override
    public Expression applyReverseExpressionRewriter(ExpressionRewriter expressionRewriter, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer, ExpressionRewriterFlags flags) {
        return applyExpressionRewriter(expressionRewriter, ssaIdentifiers, statementContainer, flags);
    }

    @Override
    public boolean isSelfMutatingOp1(LValue lValue, ArithOp arithOp) {
        return this.mutated.equals(lValue) &&
                this.op == arithOp;
    }

    @Override
    public Precedence getPrecedence() {
        return Precedence.UNARY_OTHER;
    }

    @Override
    public ArithmeticPostMutationOperation getPostMutation() {
        return new ArithmeticPostMutationOperation(getLoc(), mutated, op);
    }

    @Override
    public ArithmeticPreMutationOperation getPreMutation() {
        return this;
    }

    @Override
    public void collectUsedLValues(LValueUsageCollector lValueUsageCollector) {
        lValueUsageCollector.collect(mutated, ReadWrite.READ_WRITE);
    }

    @Override
    public LValue getUpdatedLValue() {
        return mutated;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof ArithmeticPreMutationOperation)) return false;

        ArithmeticPreMutationOperation other = (ArithmeticPreMutationOperation) o;

        return mutated.equals(other.mutated) &&
                op.equals(other.op);
    }

    @Override
    public final boolean equivalentUnder(Object o, EquivalenceConstraint constraint) {
        if (o == null) return false;
        if (o == this) return true;
        if (getClass() != o.getClass()) return false;
        ArithmeticPreMutationOperation other = (ArithmeticPreMutationOperation) o;
        if (op != other.op) return false;
        if (!constraint.equivalent(mutated, other.mutated)) return false;
        return true;
    }

}

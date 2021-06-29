package com.decompiler.bytecode.analysis.parse.expression;

import com.decompiler.bytecode.analysis.loc.BytecodeLoc;
import com.decompiler.bytecode.analysis.opgraph.op4rewriters.PrimitiveBoxingRewriter;
import com.decompiler.bytecode.analysis.parse.Expression;
import com.decompiler.bytecode.analysis.parse.LValue;
import com.decompiler.bytecode.analysis.parse.StatementContainer;
import com.decompiler.bytecode.analysis.parse.expression.misc.Precedence;
import com.decompiler.bytecode.analysis.parse.expression.rewriteinterface.BoxingProcessor;
import com.decompiler.bytecode.analysis.parse.lvalue.StackSSALabel;
import com.decompiler.bytecode.analysis.parse.rewriters.CloneHelper;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriter;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriterFlags;
import com.decompiler.bytecode.analysis.parse.utils.*;
import com.decompiler.bytecode.analysis.types.RawJavaType;
import com.decompiler.bytecode.analysis.types.discovery.InferredJavaType;
import com.decompiler.state.TypeUsageCollector;
import com.decompiler.util.Troolean;
import com.decompiler.util.output.Dumper;

public class ArrayIndex extends AbstractExpression implements BoxingProcessor {
    private Expression array;
    private Expression index;

    public ArrayIndex(BytecodeLoc loc, Expression array, Expression index) {
        super(loc, new InferredJavaType(array.getInferredJavaType().getJavaTypeInstance().removeAnArrayIndirection(), InferredJavaType.Source.OPERATION));
        this.array = array;
        this.index = index;
        index.getInferredJavaType().useAsWithoutCasting(RawJavaType.INT);
    }

    private ArrayIndex(BytecodeLoc loc, InferredJavaType inferredJavaType, Expression array, Expression index) {
        super(loc, inferredJavaType);
        this.array = array;
        this.index = index;
    }

    @Override
    public BytecodeLoc getCombinedLoc() {
        return BytecodeLoc.combine(this, array, index);
    }

    @Override
    public void collectTypeUsages(TypeUsageCollector collector) {
        array.collectTypeUsages(collector);
        index.collectTypeUsages(collector);
    }

    @Override
    public Expression deepClone(CloneHelper cloneHelper) {
        return new ArrayIndex(getLoc(), getInferredJavaType(), cloneHelper.replaceOrClone(array), cloneHelper.replaceOrClone(index));
    }

    @Override
    public Precedence getPrecedence() {
        return Precedence.PAREN_SUB_MEMBER;
    }

    @Override
    public Dumper dumpInner(Dumper d) {
        // This is a little dirty - we don't want to have 'correct' precedence for new
        // arrays, as it would cause
        // new int[3].length
        // etc to gain additional braces.
        // (compare ArrayTest5*).
        Precedence p = (array instanceof AbstractNewArray) ? Precedence.HIGHEST : getPrecedence();
        array.dumpWithOuterPrecedence(d, p, Troolean.NEITHER);
        d.separator("[").dump(index).separator("]");
        return d;
    }

    @Override
    public Expression replaceSingleUsageLValues(LValueRewriter lValueRewriter, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer) {
        index = index.replaceSingleUsageLValues(lValueRewriter, ssaIdentifiers, statementContainer);
        array = array.replaceSingleUsageLValues(lValueRewriter, ssaIdentifiers, statementContainer);
        return this;
    }

    public boolean doesBlackListLValueReplacement(LValue replace, Expression with) {
        if (replace instanceof StackSSALabel && array instanceof StackValue) {
            StackSSALabel referred = ((StackValue)array).getStackValue();
            if (referred.equals(replace)) {
                if (with.isSimple()) return false;
                return true;
            }
        }
        return false;
    }

    @Override
    public Expression applyExpressionRewriter(ExpressionRewriter expressionRewriter, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer, ExpressionRewriterFlags flags) {
        array = expressionRewriter.rewriteExpression(array, ssaIdentifiers, statementContainer, flags);
        index = expressionRewriter.rewriteExpression(index, ssaIdentifiers, statementContainer, flags);
        return this;
    }

    @Override
    public Expression applyReverseExpressionRewriter(ExpressionRewriter expressionRewriter, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer, ExpressionRewriterFlags flags) {
        index = expressionRewriter.rewriteExpression(index, ssaIdentifiers, statementContainer, flags);
        array = expressionRewriter.rewriteExpression(array, ssaIdentifiers, statementContainer, flags);
        return this;
    }

    @Override
    public void collectUsedLValues(LValueUsageCollector lValueUsageCollector) {
        array.collectUsedLValues(lValueUsageCollector);
        index.collectUsedLValues(lValueUsageCollector);
    }

    public Expression getArray() {
        return array;
    }

    public Expression getIndex() {
        return index;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof ArrayIndex)) return false;
        ArrayIndex other = (ArrayIndex) o;
        return array.equals(other.array) &&
                index.equals(other.index);
    }

    @Override
    public boolean equivalentUnder(Object o, EquivalenceConstraint constraint) {
        if (o == this) return true;
        if (!(o instanceof ArrayIndex)) return false;
        ArrayIndex other = (ArrayIndex) o;
        if (!constraint.equivalent(array, other.array)) return false;
        if (!constraint.equivalent(index, other.index)) return false;
        return true;
    }

    @Override
    public boolean rewriteBoxing(PrimitiveBoxingRewriter boxingRewriter) {
        index = boxingRewriter.sugarUnboxing(index);
        return false;
    }

    @Override
    public void applyNonArgExpressionRewriter(ExpressionRewriter expressionRewriter, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer, ExpressionRewriterFlags flags) {
    }

}

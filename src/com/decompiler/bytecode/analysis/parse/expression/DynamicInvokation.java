package com.decompiler.bytecode.analysis.parse.expression;

import java.util.List;

import com.decompiler.bytecode.analysis.loc.BytecodeLoc;
import com.decompiler.bytecode.analysis.parse.Expression;
import com.decompiler.bytecode.analysis.parse.StatementContainer;
import com.decompiler.bytecode.analysis.parse.expression.misc.Precedence;
import com.decompiler.bytecode.analysis.parse.rewriters.CloneHelper;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriter;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriterFlags;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriterHelper;
import com.decompiler.bytecode.analysis.parse.utils.*;
import com.decompiler.bytecode.analysis.types.discovery.InferredJavaType;
import com.decompiler.state.TypeUsageCollector;
import com.decompiler.util.output.Dumper;

public class DynamicInvokation extends AbstractExpression {
    private Expression innerInvokation;
    private List<Expression> dynamicArgs;

    public DynamicInvokation(BytecodeLoc loc, InferredJavaType castJavaType, Expression innerInvokation, List<Expression> dynamicArgs) {
        super(loc, castJavaType);
        this.innerInvokation = innerInvokation;
        this.dynamicArgs = dynamicArgs;
    }

    @Override
    public BytecodeLoc getCombinedLoc() {
        return BytecodeLoc.combine(this, dynamicArgs, innerInvokation);
    }

    @Override
    public Expression deepClone(CloneHelper cloneHelper) {
        return new DynamicInvokation(getLoc(), getInferredJavaType(), cloneHelper.replaceOrClone(innerInvokation), cloneHelper.replaceOrClone(dynamicArgs));
    }

    @Override
    public void collectTypeUsages(TypeUsageCollector collector) {
        collector.collectFrom(innerInvokation);
        collector.collectFrom(dynamicArgs);
    }

    @Override
    public Expression replaceSingleUsageLValues(LValueRewriter lValueRewriter, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer) {
        innerInvokation.replaceSingleUsageLValues(lValueRewriter, ssaIdentifiers, statementContainer);
        for (int x = dynamicArgs.size()-1; x >=0; --x) {
            dynamicArgs.set(x, dynamicArgs.get(x).replaceSingleUsageLValues(lValueRewriter, ssaIdentifiers, statementContainer));
        }
        return this;
    }

    @Override
    public Expression applyExpressionRewriter(ExpressionRewriter expressionRewriter, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer, ExpressionRewriterFlags flags) {
        innerInvokation.applyExpressionRewriter(expressionRewriter, ssaIdentifiers, statementContainer, flags);
        ExpressionRewriterHelper.applyForwards(dynamicArgs, expressionRewriter, ssaIdentifiers, statementContainer, flags);
        return this;
    }

    @Override
    public Expression applyReverseExpressionRewriter(ExpressionRewriter expressionRewriter, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer, ExpressionRewriterFlags flags) {
        ExpressionRewriterHelper.applyBackwards(dynamicArgs, expressionRewriter, ssaIdentifiers, statementContainer, flags);
        innerInvokation.applyExpressionRewriter(expressionRewriter, ssaIdentifiers, statementContainer, flags);
        return this;
    }

    @Override
    public Precedence getPrecedence() {
        return Precedence.PAREN_SUB_MEMBER;
    }

    @Override
    public Dumper dumpInner(Dumper d) {
        d.separator("(").dump(getInferredJavaType().getJavaTypeInstance()).separator(")");
        d.dump(innerInvokation);
        d.separator("(");
        boolean first = true;
        for (Expression arg : dynamicArgs) {
            if (!first) d.separator(", ");
            first = false;
            d.dump(arg);
        }
        d.separator(")");
        return d;
    }

    @Override
    public void collectUsedLValues(LValueUsageCollector lValueUsageCollector) {
        innerInvokation.collectUsedLValues(lValueUsageCollector);
        for (Expression expression : dynamicArgs) {
            expression.collectUsedLValues(lValueUsageCollector);
        }
    }

    public Expression getInnerInvokation() {
        return innerInvokation;
    }

    public List<Expression> getDynamicArgs() {
        return dynamicArgs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DynamicInvokation that = (DynamicInvokation) o;

        if (dynamicArgs != null ? !dynamicArgs.equals(that.dynamicArgs) : that.dynamicArgs != null) return false;
        if (innerInvokation != null ? !innerInvokation.equals(that.innerInvokation) : that.innerInvokation != null)
            return false;

        return true;
    }

    @Override
    public final boolean equivalentUnder(Object o, EquivalenceConstraint constraint) {
        if (o == null) return false;
        if (o == this) return true;
        if (getClass() != o.getClass()) return false;
        DynamicInvokation other = (DynamicInvokation) o;
        if (!constraint.equivalent(innerInvokation, other.innerInvokation)) return false;
        if (!constraint.equivalent(dynamicArgs, other.dynamicArgs)) return false;
        return true;
    }

}

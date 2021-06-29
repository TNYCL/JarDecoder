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
import com.decompiler.bytecode.analysis.types.JavaTypeInstance;
import com.decompiler.bytecode.analysis.types.RawJavaType;
import com.decompiler.bytecode.analysis.types.discovery.InferredJavaType;
import com.decompiler.entities.constantpool.ConstantPoolEntry;
import com.decompiler.entities.constantpool.ConstantPoolEntryClass;
import com.decompiler.state.TypeUsageCollector;
import com.decompiler.util.Troolean;
import com.decompiler.util.output.Dumper;

public class InstanceOfExpression extends AbstractExpression {
    private Expression lhs;
    private JavaTypeInstance typeInstance;

    public InstanceOfExpression(BytecodeLoc loc, Expression lhs, ConstantPoolEntry cpe) {
        super(loc, new InferredJavaType(RawJavaType.BOOLEAN, InferredJavaType.Source.EXPRESSION));
        this.lhs = lhs;
        ConstantPoolEntryClass cpec = (ConstantPoolEntryClass) cpe;
        this.typeInstance = cpec.getTypeInstance();
    }

    public InstanceOfExpression(BytecodeLoc loc, InferredJavaType inferredJavaType, Expression lhs, JavaTypeInstance typeInstance) {
        super(loc, inferredJavaType);
        this.lhs = lhs;
        this.typeInstance = typeInstance;
    }

    @Override
    public BytecodeLoc getCombinedLoc() {
        return BytecodeLoc.combine(this, lhs);
    }

    @Override
    public void collectTypeUsages(TypeUsageCollector collector) {
        lhs.collectTypeUsages(collector);
        collector.collect(typeInstance);
    }

    @Override
    public Expression deepClone(CloneHelper cloneHelper) {
        return new InstanceOfExpression(getLoc(), getInferredJavaType(), cloneHelper.replaceOrClone(lhs), typeInstance);
    }

    @Override
    public Precedence getPrecedence() {
        return Precedence.REL_CMP_INSTANCEOF;
    }

    public Expression getLhs() {
        return lhs;
    }

    @Override
    public Dumper dumpInner(Dumper d) {
        lhs.dumpWithOuterPrecedence(d, getPrecedence(), Troolean.NEITHER);
        return d.print(" instanceof ").dump(typeInstance);
    }

    @Override
    public Expression replaceSingleUsageLValues(LValueRewriter lValueRewriter, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer) {
        lhs = lhs.replaceSingleUsageLValues(lValueRewriter, ssaIdentifiers, statementContainer);
        return this;
    }

    @Override
    public Expression applyExpressionRewriter(ExpressionRewriter expressionRewriter, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer, ExpressionRewriterFlags flags) {
        lhs = expressionRewriter.rewriteExpression(lhs, ssaIdentifiers, statementContainer, flags);
        return this;
    }

    @Override
    public Expression applyReverseExpressionRewriter(ExpressionRewriter expressionRewriter, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer, ExpressionRewriterFlags flags) {
        return applyExpressionRewriter(expressionRewriter, ssaIdentifiers, statementContainer, flags);
    }


    @Override
    public void collectUsedLValues(LValueUsageCollector lValueUsageCollector) {
        lhs.collectUsedLValues(lValueUsageCollector);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (!(o instanceof InstanceOfExpression)) return false;
        InstanceOfExpression other = (InstanceOfExpression) o;
        if (!lhs.equals(other.lhs)) return false;
        if (!typeInstance.equals(other.typeInstance)) return false;
        return true;
    }

    @Override
    public final boolean equivalentUnder(Object o, EquivalenceConstraint constraint) {
        if (o == null) return false;
        if (o == this) return true;
        if (getClass() != o.getClass()) return false;
        InstanceOfExpression other = (InstanceOfExpression) o;
        if (!constraint.equivalent(lhs, other.lhs)) return false;
        if (!constraint.equivalent(typeInstance, other.typeInstance)) return false;
        return true;
    }

}

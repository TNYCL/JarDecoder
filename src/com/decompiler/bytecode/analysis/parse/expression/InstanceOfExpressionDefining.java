package com.decompiler.bytecode.analysis.parse.expression;

import com.decompiler.bytecode.analysis.loc.BytecodeLoc;
import com.decompiler.bytecode.analysis.parse.Expression;
import com.decompiler.bytecode.analysis.parse.LValue;
import com.decompiler.bytecode.analysis.parse.StatementContainer;
import com.decompiler.bytecode.analysis.parse.expression.misc.Precedence;
import com.decompiler.bytecode.analysis.parse.rewriters.CloneHelper;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriter;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriterFlags;
import com.decompiler.bytecode.analysis.parse.utils.EquivalenceConstraint;
import com.decompiler.bytecode.analysis.parse.utils.LValueRewriter;
import com.decompiler.bytecode.analysis.parse.utils.LValueUsageCollector;
import com.decompiler.bytecode.analysis.parse.utils.SSAIdentifiers;
import com.decompiler.bytecode.analysis.types.JavaTypeInstance;
import com.decompiler.bytecode.analysis.types.RawJavaType;
import com.decompiler.bytecode.analysis.types.discovery.InferredJavaType;
import com.decompiler.entities.constantpool.ConstantPoolEntry;
import com.decompiler.entities.constantpool.ConstantPoolEntryClass;
import com.decompiler.state.TypeUsageCollector;
import com.decompiler.util.Troolean;
import com.decompiler.util.output.Dumper;

public class InstanceOfExpressionDefining extends AbstractExpression {
    private Expression lhs;
    private JavaTypeInstance typeInstance;
    private LValue defines;

    public InstanceOfExpressionDefining(BytecodeLoc loc, InferredJavaType inferredJavaType, Expression lhs, JavaTypeInstance typeInstance, LValue defines) {
        super(loc, inferredJavaType);
        this.lhs = lhs;
        this.typeInstance = typeInstance;
        this.defines = defines;
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

    public InstanceOfExpressionDefining withReplacedExpression(Expression e) {
        return new InstanceOfExpressionDefining(getLoc(), this.getInferredJavaType(), e, typeInstance, defines);
    }

    @Override
    public Expression deepClone(CloneHelper cloneHelper) {
        return new InstanceOfExpressionDefining(getLoc(), getInferredJavaType(), cloneHelper.replaceOrClone(lhs), typeInstance, defines);
    }

    @Override
    public Precedence getPrecedence() {
        return Precedence.REL_CMP_INSTANCEOF;
    }

    @Override
    public Dumper dumpInner(Dumper d) {
        lhs.dumpWithOuterPrecedence(d, getPrecedence(), Troolean.NEITHER);
        d.print(" instanceof ").dump(typeInstance);
        d.print(" ").dump(defines);
        return d;
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

    public Expression getLhs() {
        return lhs;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (!(o instanceof InstanceOfExpressionDefining)) return false;
        InstanceOfExpressionDefining other = (InstanceOfExpressionDefining) o;
        if (!lhs.equals(other.lhs)) return false;
        if (!typeInstance.equals(other.typeInstance)) return false;
        if (!defines.equals(other.defines)) return false;
        return true;
    }

    @Override
    public final boolean equivalentUnder(Object o, EquivalenceConstraint constraint) {
        if (o == null) return false;
        if (o == this) return true;
        if (getClass() != o.getClass()) return false;
        InstanceOfExpressionDefining other = (InstanceOfExpressionDefining) o;
        if (!constraint.equivalent(lhs, other.lhs)) return false;
        if (!constraint.equivalent(typeInstance, other.typeInstance)) return false;
        if (!constraint.equivalent(defines, other.defines)) return false;
        return true;
    }

}

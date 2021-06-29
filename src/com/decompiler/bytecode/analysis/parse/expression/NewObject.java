package com.decompiler.bytecode.analysis.parse.expression;

import com.decompiler.bytecode.analysis.loc.BytecodeLoc;
import com.decompiler.bytecode.analysis.parse.Expression;
import com.decompiler.bytecode.analysis.parse.StatementContainer;
import com.decompiler.bytecode.analysis.parse.expression.misc.Precedence;
import com.decompiler.bytecode.analysis.parse.rewriters.CloneHelper;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriter;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriterFlags;
import com.decompiler.bytecode.analysis.parse.utils.*;
import com.decompiler.bytecode.analysis.types.JavaTypeInstance;
import com.decompiler.bytecode.analysis.types.discovery.InferredJavaType;
import com.decompiler.entities.constantpool.ConstantPoolEntry;
import com.decompiler.entities.constantpool.ConstantPoolEntryClass;
import com.decompiler.entities.exceptions.ExceptionCheck;
import com.decompiler.state.TypeUsageCollector;
import com.decompiler.util.output.Dumper;

public class NewObject extends AbstractExpression {
    private final ConstantPoolEntryClass type;

    public NewObject(BytecodeLoc loc, ConstantPoolEntry type) {
        // TODO : we have more information than this...
        super(loc, new InferredJavaType(((ConstantPoolEntryClass) type).getTypeInstance(), InferredJavaType.Source.EXPRESSION));
        this.type = (ConstantPoolEntryClass) type;
    }

    @Override
    public BytecodeLoc getCombinedLoc() {
        return getLoc();
    }

    @Override
    public void collectTypeUsages(TypeUsageCollector collector) {
        collector.collect(getTypeInstance());
    }

    @Override
    public Expression deepClone(CloneHelper cloneHelper) {
        return this;
    }

    @Override
    public Precedence getPrecedence() {
        return Precedence.PAREN_SUB_MEMBER;
    }

    @Override
    public Dumper dumpInner(Dumper d) {
        return d.keyword("new ").dump(getTypeInstance());
    }

    @Override
    public Expression replaceSingleUsageLValues(LValueRewriter lValueRewriter, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer) {
        return this;
    }

    @Override
    public Expression applyExpressionRewriter(ExpressionRewriter expressionRewriter, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer, ExpressionRewriterFlags flags) {
        return this;
    }

    @Override
    public Expression applyReverseExpressionRewriter(ExpressionRewriter expressionRewriter, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer, ExpressionRewriterFlags flags) {
        return this;
    }

    public ConstantPoolEntryClass getType() {
        return type;
    }

    public JavaTypeInstance getTypeInstance() {
        return getInferredJavaType().getJavaTypeInstance();
    }

    @Override
    public void collectUsedLValues(LValueUsageCollector lValueUsageCollector) {
    }

    @Override
    public boolean isValidStatement() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (!(o instanceof NewObject)) return false;
        NewObject other = (NewObject) o;
        if (!getTypeInstance().equals(other.getTypeInstance())) return false;
        return true;
    }

    @Override
    public boolean canThrow(ExceptionCheck caught) {
        return false;
    }

    @Override
    public final boolean equivalentUnder(Object o, EquivalenceConstraint constraint) {
        if (o == null) return false;
        if (o == this) return true;
        if (o.getClass() != getClass()) return false;
        NewObject other = (NewObject) o;
        if (!constraint.equivalent(getTypeInstance(), other.getTypeInstance())) return false;
        return true;
    }
}

package com.decompiler.bytecode.analysis.parse.statement;

import com.decompiler.bytecode.analysis.loc.BytecodeLoc;
import com.decompiler.bytecode.analysis.parse.LValue;
import com.decompiler.bytecode.analysis.parse.Statement;
import com.decompiler.bytecode.analysis.parse.rewriters.CloneHelper;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriter;
import com.decompiler.bytecode.analysis.parse.utils.*;
import com.decompiler.bytecode.analysis.structured.StructuredStatement;
import com.decompiler.bytecode.analysis.structured.statement.UnstructuredFinally;
import com.decompiler.util.output.Dumper;

public class FinallyStatement extends AbstractStatement {
    private BlockIdentifier finallyBlockIdent;

    public FinallyStatement(BytecodeLoc loc, BlockIdentifier finallyBlockIdent) {
        super(loc);
        this.finallyBlockIdent = finallyBlockIdent;
    }

    @Override
    public BytecodeLoc getCombinedLoc() {
        return getLoc();
    }

    @Override
    public Dumper dump(Dumper dumper) {
        return dumper.keyword("finally ").separator("{").newln();
    }

    @Override
    public Statement deepClone(CloneHelper cloneHelper) {
        return new FinallyStatement(getLoc(), finallyBlockIdent);
    }

    @Override
    public void replaceSingleUsageLValues(LValueRewriter lValueRewriter, SSAIdentifiers ssaIdentifiers) {
    }

    @Override
    public void rewriteExpressions(ExpressionRewriter expressionRewriter, SSAIdentifiers ssaIdentifiers) {
    }

    @Override
    public void collectLValueUsage(LValueUsageCollector lValueUsageCollector) {
    }

    @Override
    public LValue getCreatedLValue() {
        return null;
    }

    @Override
    public StructuredStatement getStructuredStatement() {
        return new UnstructuredFinally(finallyBlockIdent);
    }

    public BlockIdentifier getFinallyBlockIdent() {
        return finallyBlockIdent;
    }

    @Override
    public final boolean equivalentUnder(Object o, EquivalenceConstraint constraint) {
        if (o == null) return false;
        if (o == this) return true;
        if (getClass() != o.getClass()) return false;
        FinallyStatement other = (FinallyStatement) o;
//        if (!constraint.equivalent(finallyBlockIdent, other.finallyBlockIdent)) return false;
        return true;
    }

}

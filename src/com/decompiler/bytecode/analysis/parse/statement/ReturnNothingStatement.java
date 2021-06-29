package com.decompiler.bytecode.analysis.parse.statement;

import com.decompiler.bytecode.analysis.loc.BytecodeLoc;
import com.decompiler.bytecode.analysis.parse.rewriters.CloneHelper;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriter;
import com.decompiler.bytecode.analysis.parse.utils.*;
import com.decompiler.bytecode.analysis.structured.StructuredStatement;
import com.decompiler.bytecode.analysis.structured.statement.StructuredReturn;
import com.decompiler.entities.exceptions.ExceptionCheck;
import com.decompiler.util.output.Dumper;

public class ReturnNothingStatement extends ReturnStatement {
    public ReturnNothingStatement(BytecodeLoc loc) {
        super(loc);
    }

    @Override
    public BytecodeLoc getCombinedLoc() {
        return getLoc();
    }

    @Override
    public ReturnStatement deepClone(CloneHelper cloneHelper) {
        return new ReturnNothingStatement(getLoc());
    }

    @Override
    public Dumper dump(Dumper dumper) {
        return dumper.keyword("return").print(";").newln();
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
    public StructuredStatement getStructuredStatement() {
        return new StructuredReturn(getLoc());
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof ReturnNothingStatement);
    }

    @Override
    public final boolean equivalentUnder(Object o, EquivalenceConstraint constraint) {
        if (o == null) return false;
        if (o == this) return true;
        if (getClass() != o.getClass()) return false;
        return true;
    }

    @Override
    public boolean canThrow(ExceptionCheck caught) {
        return false;
    }
}

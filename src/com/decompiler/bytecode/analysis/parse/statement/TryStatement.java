package com.decompiler.bytecode.analysis.parse.statement;

import java.util.List;
import java.util.Set;

import com.decompiler.bytecode.analysis.loc.BytecodeLoc;
import com.decompiler.bytecode.analysis.parse.Expression;
import com.decompiler.bytecode.analysis.parse.Statement;
import com.decompiler.bytecode.analysis.parse.rewriters.CloneHelper;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriter;
import com.decompiler.bytecode.analysis.parse.utils.*;
import com.decompiler.bytecode.analysis.structured.StructuredStatement;
import com.decompiler.bytecode.analysis.structured.statement.UnstructuredTry;
import com.decompiler.entities.exceptions.ExceptionGroup;
import com.decompiler.util.collections.SetFactory;
import com.decompiler.util.output.Dumper;

public class TryStatement extends AbstractStatement {
    private final ExceptionGroup exceptionGroup;
    // This is a hack. :(
    // We keep track of what mutexes this finally leaves.
    private final Set<Expression> monitors = SetFactory.newSet();

    public TryStatement(BytecodeLoc loc, ExceptionGroup exceptionGroup) {
        super(loc);
        this.exceptionGroup = exceptionGroup;
    }

    @Override
    public BytecodeLoc getCombinedLoc() {
        return getLoc();
    }

    public void addExitMutex(Expression e) {
        monitors.add(e);
    }

    public Set<Expression> getMonitors() {
        return monitors;
    }

    @Override
    public Statement deepClone(CloneHelper cloneHelper) {
        TryStatement res = new TryStatement(getLoc(), exceptionGroup);
        for (Expression monitor : monitors) {
            res.monitors.add(cloneHelper.replaceOrClone(monitor));
        }
        return res;
    }

    @Override
    public Dumper dump(Dumper dumper) {
        return dumper.print("try { ").print(exceptionGroup.getTryBlockIdentifier().toString()).newln();
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
        return new UnstructuredTry(exceptionGroup);
    }

    public BlockIdentifier getBlockIdentifier() {
        return exceptionGroup.getTryBlockIdentifier();
    }

    public List<ExceptionGroup.Entry> getEntries() {
        return exceptionGroup.getEntries();
    }

    public boolean equivalentUnder(Object other, EquivalenceConstraint constraint) {
        return this.getClass() == other.getClass();
    }
}

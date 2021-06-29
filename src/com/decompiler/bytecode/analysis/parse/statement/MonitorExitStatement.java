package com.decompiler.bytecode.analysis.parse.statement;

import com.decompiler.bytecode.analysis.loc.BytecodeLoc;
import com.decompiler.bytecode.analysis.parse.Expression;
import com.decompiler.bytecode.analysis.parse.Statement;
import com.decompiler.bytecode.analysis.parse.rewriters.CloneHelper;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriter;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriterFlags;
import com.decompiler.bytecode.analysis.parse.utils.EquivalenceConstraint;
import com.decompiler.bytecode.analysis.parse.utils.LValueRewriter;
import com.decompiler.bytecode.analysis.parse.utils.LValueUsageCollector;
import com.decompiler.bytecode.analysis.parse.utils.SSAIdentifiers;
import com.decompiler.bytecode.analysis.structured.StructuredStatement;
import com.decompiler.bytecode.analysis.structured.statement.StructuredComment;
import com.decompiler.util.output.Dumper;

public class MonitorExitStatement extends MonitorStatement {
    private Expression monitor;

    public MonitorExitStatement(BytecodeLoc loc, Expression monitor) {
        super(loc);
        this.monitor = monitor;
    }

    @Override
    public BytecodeLoc getCombinedLoc() {
        return BytecodeLoc.combine(this, monitor);
    }

    @Override
    public Dumper dump(Dumper dumper) {
        return dumper.print("MONITOREXIT : ").dump(monitor);
    }

    @Override
    public Statement deepClone(CloneHelper cloneHelper) {
        return new MonitorExitStatement(getLoc(), cloneHelper.replaceOrClone(monitor));
    }

    @Override
    public void replaceSingleUsageLValues(LValueRewriter lValueRewriter, SSAIdentifiers ssaIdentifiers) {
        monitor = monitor.replaceSingleUsageLValues(lValueRewriter, ssaIdentifiers, getContainer());
    }

    @Override
    public void rewriteExpressions(ExpressionRewriter expressionRewriter, SSAIdentifiers ssaIdentifiers) {
        monitor = expressionRewriter.rewriteExpression(monitor, ssaIdentifiers, getContainer(), ExpressionRewriterFlags.RVALUE);
    }

    @Override
    public void collectLValueUsage(LValueUsageCollector lValueUsageCollector) {
        monitor.collectUsedLValues(lValueUsageCollector);
    }

    @Override
    public StructuredStatement getStructuredStatement() {
        return new StructuredComment("** MonitorExit[" + monitor + "] (shouldn't be in output)");
    }

    public Expression getMonitor() {
        return monitor;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof MonitorExitStatement)) return false;
        MonitorExitStatement other = (MonitorExitStatement) o;
        return monitor.equals(other.monitor);
    }

    @Override
    public final boolean equivalentUnder(Object o, EquivalenceConstraint constraint) {
        if (o == null) return false;
        if (o == this) return true;
        if (getClass() != o.getClass()) return false;
        MonitorExitStatement other = (MonitorExitStatement) o;
        if (!constraint.equivalent(monitor, other.monitor)) return false;
        return true;
    }

}

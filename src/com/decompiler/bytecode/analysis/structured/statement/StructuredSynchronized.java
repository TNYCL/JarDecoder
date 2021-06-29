package com.decompiler.bytecode.analysis.structured.statement;

import java.util.List;

import com.decompiler.bytecode.analysis.loc.BytecodeLoc;
import com.decompiler.bytecode.analysis.opgraph.Op04StructuredStatement;
import com.decompiler.bytecode.analysis.opgraph.op4rewriters.matchutil.MatchIterator;
import com.decompiler.bytecode.analysis.opgraph.op4rewriters.matchutil.MatchResultCollector;
import com.decompiler.bytecode.analysis.parse.Expression;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriter;
import com.decompiler.bytecode.analysis.parse.utils.scope.LValueScopeDiscoverer;
import com.decompiler.bytecode.analysis.structured.StructuredStatement;
import com.decompiler.state.TypeUsageCollector;
import com.decompiler.util.output.Dumper;

public class StructuredSynchronized extends AbstractStructuredBlockStatement {
    private Expression monitor;

    StructuredSynchronized(BytecodeLoc loc, Expression monitor, Op04StructuredStatement body) {
        super(loc, body);
        this.monitor = monitor;
    }

    @Override
    public BytecodeLoc getCombinedLoc() {
        return BytecodeLoc.combine(this, monitor);
    }

    @Override
    public void collectTypeUsages(TypeUsageCollector collector) {
        monitor.collectTypeUsages(collector);
        super.collectTypeUsages(collector);
    }

    @Override
    public Dumper dump(Dumper dumper) {
        dumper.print("synchronized (").dump(monitor).print(") ");
        getBody().dump(dumper);
        return dumper;
    }

    @Override
    public boolean isProperlyStructured() {
        return true;
    }

    @Override
    public boolean fallsNopToNext() {
        return true;
    }

    @Override
    public boolean isScopeBlock() {
        return true;
    }

    @Override
    public void linearizeInto(List<StructuredStatement> out) {
        out.add(this);
        getBody().linearizeStatementsInto(out);
    }

    @Override
    public void traceLocalVariableScope(LValueScopeDiscoverer scopeDiscoverer) {
        monitor.collectUsedLValues(scopeDiscoverer);
        scopeDiscoverer.processOp04Statement(getBody());
    }

    @Override
    public boolean match(MatchIterator<StructuredStatement> matchIterator, MatchResultCollector matchResultCollector) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void rewriteExpressions(ExpressionRewriter expressionRewriter) {
        monitor = expressionRewriter.rewriteExpression(monitor, null, this.getContainer(), null);
    }

}

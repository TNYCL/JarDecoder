package com.decompiler.bytecode.analysis.structured.statement;

import java.util.List;

import com.decompiler.bytecode.analysis.loc.BytecodeLoc;
import com.decompiler.bytecode.analysis.opgraph.Op04StructuredStatement;
import com.decompiler.bytecode.analysis.opgraph.op4rewriters.matchutil.MatchIterator;
import com.decompiler.bytecode.analysis.opgraph.op4rewriters.matchutil.MatchResultCollector;
import com.decompiler.bytecode.analysis.opgraph.op4rewriters.transformers.StructuredStatementTransformer;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriter;
import com.decompiler.bytecode.analysis.parse.utils.scope.LValueScopeDiscoverer;
import com.decompiler.bytecode.analysis.structured.StructuredScope;
import com.decompiler.bytecode.analysis.structured.StructuredStatement;
import com.decompiler.state.TypeUsageCollector;
import com.decompiler.util.output.Dumper;

public class StructuredFinally extends AbstractStructuredStatement {
    private final Op04StructuredStatement catchBlock;

    public StructuredFinally(Op04StructuredStatement catchBlock) {
        super(BytecodeLoc.NONE);
        this.catchBlock = catchBlock;
    }

    @Override
    public Dumper dump(Dumper dumper) {
        dumper.keyword("finally ");
        catchBlock.dump(dumper);
        return dumper;
    }

    @Override
    public BytecodeLoc getCombinedLoc() {
        return getLoc();
    }

    @Override
    public void collectTypeUsages(TypeUsageCollector collector) {
        collector.collectFrom(catchBlock);
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
    public void transformStructuredChildren(StructuredStatementTransformer transformer, StructuredScope scope) {
        catchBlock.transform(transformer, scope);
    }

    @Override
    public void linearizeInto(List<StructuredStatement> out) {
        out.add(this);
        catchBlock.linearizeStatementsInto(out);
    }

    public Op04StructuredStatement getCatchBlock() {
        return catchBlock;
    }

    @Override
    public boolean isRecursivelyStructured() {
        return catchBlock.isFullyStructured();
    }


    @Override
    public boolean match(MatchIterator<StructuredStatement> matchIterator, MatchResultCollector matchResultCollector) {
        StructuredStatement o = matchIterator.getCurrent();
        if (!(o instanceof StructuredFinally)) return false;
        StructuredFinally other = (StructuredFinally) o;
        // we don't actually check any equality for a match.
        matchIterator.advance();
        return true;
    }

    @Override
    public void traceLocalVariableScope(LValueScopeDiscoverer scopeDiscoverer) {
        scopeDiscoverer.processOp04Statement(catchBlock);
    }


    @Override
    public void rewriteExpressions(ExpressionRewriter expressionRewriter) {
    }
}

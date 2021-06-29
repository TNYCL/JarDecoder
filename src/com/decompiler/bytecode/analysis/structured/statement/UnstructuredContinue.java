package com.decompiler.bytecode.analysis.structured.statement;

import java.util.List;
import java.util.Vector;

import com.decompiler.bytecode.analysis.loc.BytecodeLoc;
import com.decompiler.bytecode.analysis.opgraph.op4rewriters.matchutil.MatchIterator;
import com.decompiler.bytecode.analysis.opgraph.op4rewriters.matchutil.MatchResultCollector;
import com.decompiler.bytecode.analysis.opgraph.op4rewriters.transformers.StructuredStatementTransformer;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriter;
import com.decompiler.bytecode.analysis.parse.utils.BlockIdentifier;
import com.decompiler.bytecode.analysis.parse.utils.scope.LValueScopeDiscoverer;
import com.decompiler.bytecode.analysis.structured.StructuredScope;
import com.decompiler.bytecode.analysis.structured.StructuredStatement;
import com.decompiler.state.TypeUsageCollector;
import com.decompiler.util.output.Dumper;

public class UnstructuredContinue extends AbstractStructuredContinue {

    private final BlockIdentifier continueTgt;

    public UnstructuredContinue(BytecodeLoc loc, BlockIdentifier continueTgt) {
        super(loc);
        this.continueTgt = continueTgt;
    }

    @Override
    public BytecodeLoc getCombinedLoc() {
        return getLoc();
    }

    @Override
    public Dumper dump(Dumper dumper) {
        return dumper.print("** continue;").newln();
    }

    @Override
    public BlockIdentifier getContinueTgt() {
        return continueTgt;
    }

    @Override
    public void collectTypeUsages(TypeUsageCollector collector) {
    }

    @Override
    public StructuredStatement informBlockHeirachy(Vector<BlockIdentifier> blockIdentifiers) {

        boolean localBreak = false;
        BlockIdentifier outermostBreakable = BlockIdentifier.getInnermostBreakable(blockIdentifiers);
        if (!blockIdentifiers.contains(continueTgt)) {
            return null;
        }
        if (outermostBreakable == continueTgt) {
            localBreak = true;
        } else {
            continueTgt.addForeignRef();
        }
        return new StructuredContinue(getLoc(), continueTgt, localBreak);
    }

    @Override
    public boolean isProperlyStructured() {
        return false;
    }

    @Override
    public boolean isRecursivelyStructured() {
        return false;
    }

    @Override
    public void transformStructuredChildren(StructuredStatementTransformer transformer, StructuredScope scope) {
    }

    @Override
    public void linearizeInto(List<StructuredStatement> out) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean match(MatchIterator<StructuredStatement> matchIterator, MatchResultCollector matchResultCollector) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void traceLocalVariableScope(LValueScopeDiscoverer scopeDiscoverer) {
    }

    @Override
    public void rewriteExpressions(ExpressionRewriter expressionRewriter) {
    }

}

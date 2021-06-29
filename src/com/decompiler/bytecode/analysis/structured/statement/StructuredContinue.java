package com.decompiler.bytecode.analysis.structured.statement;

import java.util.List;

import com.decompiler.bytecode.analysis.loc.BytecodeLoc;
import com.decompiler.bytecode.analysis.opgraph.op4rewriters.transformers.StructuredStatementTransformer;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriter;
import com.decompiler.bytecode.analysis.parse.utils.BlockIdentifier;
import com.decompiler.bytecode.analysis.parse.utils.scope.LValueScopeDiscoverer;
import com.decompiler.bytecode.analysis.structured.StructuredScope;
import com.decompiler.bytecode.analysis.structured.StructuredStatement;
import com.decompiler.state.TypeUsageCollector;
import com.decompiler.util.output.Dumper;

public class StructuredContinue extends AbstractStructuredContinue {

    private final BlockIdentifier continueTgt;
    private final boolean localContinue;

    StructuredContinue(BytecodeLoc loc, BlockIdentifier continueTgt, boolean localContinue) {
        super(loc);
        this.continueTgt = continueTgt;
        this.localContinue = localContinue;
    }

    @Override
    public Dumper dump(Dumper dumper) {
        if (localContinue) {
            dumper.keyword("continue").print(";");
        } else {
            dumper.keyword("continue ").print(continueTgt.getName() + ";");
        }
        dumper.newln();
        return dumper;
    }

    @Override
    public BytecodeLoc getCombinedLoc() {
        return getLoc();
    }

    @Override
    public void collectTypeUsages(TypeUsageCollector collector) {
    }

    @Override
    public BlockIdentifier getContinueTgt() {
        return continueTgt;
    }

    @Override
    public void transformStructuredChildren(StructuredStatementTransformer transformer, StructuredScope scope) {
    }

    @Override
    public void linearizeInto(List<StructuredStatement> out) {
        out.add(this);
    }

    @Override
    public void traceLocalVariableScope(LValueScopeDiscoverer scopeDiscoverer) {
    }

    @Override
    public void rewriteExpressions(ExpressionRewriter expressionRewriter) {
    }

}

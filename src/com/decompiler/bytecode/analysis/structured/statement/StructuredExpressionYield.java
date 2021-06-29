package com.decompiler.bytecode.analysis.structured.statement;

import java.util.List;

import com.decompiler.bytecode.analysis.loc.BytecodeLoc;
import com.decompiler.bytecode.analysis.opgraph.op4rewriters.matchutil.MatchIterator;
import com.decompiler.bytecode.analysis.opgraph.op4rewriters.matchutil.MatchResultCollector;
import com.decompiler.bytecode.analysis.opgraph.op4rewriters.transformers.StructuredStatementTransformer;
import com.decompiler.bytecode.analysis.parse.Expression;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriter;
import com.decompiler.bytecode.analysis.parse.utils.scope.LValueScopeDiscoverer;
import com.decompiler.bytecode.analysis.structured.StructuredScope;
import com.decompiler.bytecode.analysis.structured.StructuredStatement;
import com.decompiler.state.TypeUsageCollector;
import com.decompiler.util.output.Dumper;

public class StructuredExpressionYield extends AbstractStructuredStatement {

    private Expression value;

    public StructuredExpressionYield(BytecodeLoc loc, Expression value) {
        super(loc);
        this.value = value;
    }

    @Override
    public Dumper dump(Dumper dumper) {
        return dumper.print("yield ").dump(value).endCodeln();
    }

    @Override
    public void collectTypeUsages(TypeUsageCollector collector) {
        value.collectTypeUsages(collector);
    }

    @Override
    public BytecodeLoc getCombinedLoc() {
        return BytecodeLoc.combine(this, value);
    }

    @Override
    public void transformStructuredChildren(StructuredStatementTransformer transformer, StructuredScope scope) {
    }

    @Override
    public void linearizeInto(List<StructuredStatement> out) {
        out.add(this);
    }

    @Override
    public boolean match(MatchIterator<StructuredStatement> matchIterator, MatchResultCollector matchResultCollector) {
        StructuredStatement o = matchIterator.getCurrent();
        if (!(o instanceof StructuredExpressionYield)) return false;
        StructuredExpressionYield other = (StructuredExpressionYield) o;
        if (!value.equals(other.value)) return false;
        // Don't check locality.
        matchIterator.advance();
        return true;
    }

    public Expression getValue() {
        return value;
    }

    @Override
    public void traceLocalVariableScope(LValueScopeDiscoverer scopeDiscoverer) {
        value.collectUsedLValues(scopeDiscoverer);
    }

    @Override
    public void rewriteExpressions(ExpressionRewriter expressionRewriter) {
        value = expressionRewriter.rewriteExpression(value, null, this.getContainer(), null);
    }
}

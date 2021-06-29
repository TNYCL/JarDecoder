package com.decompiler.bytecode.analysis.structured.statement;

import java.util.List;
import java.util.Vector;

import com.decompiler.bytecode.analysis.loc.BytecodeLoc;
import com.decompiler.bytecode.analysis.opgraph.op4rewriters.matchutil.MatchIterator;
import com.decompiler.bytecode.analysis.opgraph.op4rewriters.matchutil.MatchResultCollector;
import com.decompiler.bytecode.analysis.opgraph.op4rewriters.transformers.StructuredStatementTransformer;
import com.decompiler.bytecode.analysis.parse.Expression;
import com.decompiler.bytecode.analysis.parse.expression.CastExpression;
import com.decompiler.bytecode.analysis.parse.expression.ConditionalExpression;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriter;
import com.decompiler.bytecode.analysis.parse.utils.BlockIdentifier;
import com.decompiler.bytecode.analysis.parse.utils.scope.LValueScopeDiscoverer;
import com.decompiler.bytecode.analysis.structured.StructuredScope;
import com.decompiler.bytecode.analysis.structured.StructuredStatement;
import com.decompiler.state.TypeUsageCollector;
import com.decompiler.util.output.Dumper;

public class StructuredAssert extends AbstractStructuredStatement {

    private ConditionalExpression conditionalExpression;
    private Expression arg;

    private StructuredAssert(BytecodeLoc loc, ConditionalExpression conditionalExpression, Expression arg) {
        super(loc);
        this.conditionalExpression = conditionalExpression;
        this.arg = arg;
    }

    public static StructuredAssert mkStructuredAssert(BytecodeLoc loc, ConditionalExpression conditionalExpression, Expression arg) {
        return new StructuredAssert(loc, conditionalExpression, CastExpression.tryRemoveCast(arg));
    }

    @Override
    public BytecodeLoc getCombinedLoc() {
        return BytecodeLoc.combine(this, conditionalExpression, arg);
    }

    @Override
    public void collectTypeUsages(TypeUsageCollector collector) {
        conditionalExpression.collectTypeUsages(collector);
    }

    @Override
    public Dumper dump(Dumper dumper) {
        dumper.print("assert (").dump(conditionalExpression).separator(")");
        if (arg != null) {
            dumper.print(" : ").dump(arg);
        }
        dumper.endCodeln();
        return dumper;
    }

    @Override
    public StructuredStatement informBlockHeirachy(Vector<BlockIdentifier> blockIdentifiers) {
        throw new UnsupportedOperationException();
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
        conditionalExpression.collectUsedLValues(scopeDiscoverer);
    }

    @Override
    public boolean isRecursivelyStructured() {
        return true;
    }

    @Override
    public boolean match(MatchIterator<StructuredStatement> matchIterator, MatchResultCollector matchResultCollector) {
        StructuredStatement o = matchIterator.getCurrent();
        if (!(o instanceof StructuredAssert)) return false;
        StructuredAssert other = (StructuredAssert) o;
        if (!conditionalExpression.equals(other.conditionalExpression)) return false;

        matchIterator.advance();
        return true;
    }

    @Override
    public void rewriteExpressions(ExpressionRewriter expressionRewriter) {
        conditionalExpression = expressionRewriter.rewriteExpression(conditionalExpression, null, this.getContainer(), null);
    }

}

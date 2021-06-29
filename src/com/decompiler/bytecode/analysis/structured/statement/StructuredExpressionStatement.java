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

public class StructuredExpressionStatement extends AbstractStructuredStatement {
    private Expression expression;
    private boolean inline;

    public StructuredExpressionStatement(BytecodeLoc loc, Expression expression, boolean inline) {
        super(loc);
        this.expression = expression;
        this.inline = inline;
    }

    @Override
    public Dumper dump(Dumper dumper) {
        dumper.dump(expression);
        if (!inline) dumper.endCodeln();
        return dumper;
    }

    @Override
    public BytecodeLoc getCombinedLoc() {
        return BytecodeLoc.combine(this, expression);
    }

    @Override
    public void collectTypeUsages(TypeUsageCollector collector) {
        collector.collectFrom(expression);
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
        if (!(o instanceof StructuredExpressionStatement)) return false;
        StructuredExpressionStatement other = (StructuredExpressionStatement) o;
        if (!expression.equals(other.expression)) return false;
        matchIterator.advance();
        return true;
    }

    @Override
    public void traceLocalVariableScope(LValueScopeDiscoverer scopeDiscoverer) {
        expression.collectUsedLValues(scopeDiscoverer);
    }

    @Override
    public void rewriteExpressions(ExpressionRewriter expressionRewriter) {
        expression = expressionRewriter.rewriteExpression(expression, null, this.getContainer(), null);
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o == null) return false;

        if (!(o instanceof StructuredExpressionStatement)) return false;

        StructuredExpressionStatement other = (StructuredExpressionStatement) o;
        return expression.equals(other.expression);
    }

}

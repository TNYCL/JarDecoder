package com.decompiler.bytecode.analysis.structured.expression;

import com.decompiler.bytecode.analysis.loc.BytecodeLoc;
import com.decompiler.bytecode.analysis.opgraph.op4rewriters.transformers.ExpressionRewriterTransformer;
import com.decompiler.bytecode.analysis.parse.Expression;
import com.decompiler.bytecode.analysis.parse.StatementContainer;
import com.decompiler.bytecode.analysis.parse.expression.AbstractExpression;
import com.decompiler.bytecode.analysis.parse.expression.misc.Precedence;
import com.decompiler.bytecode.analysis.parse.rewriters.CloneHelper;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriter;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriterFlags;
import com.decompiler.bytecode.analysis.parse.utils.EquivalenceConstraint;
import com.decompiler.bytecode.analysis.parse.utils.LValueRewriter;
import com.decompiler.bytecode.analysis.parse.utils.LValueUsageCollector;
import com.decompiler.bytecode.analysis.parse.utils.SSAIdentifiers;
import com.decompiler.bytecode.analysis.parse.utils.scope.LValueScopeDiscoverer;
import com.decompiler.bytecode.analysis.structured.StructuredScope;
import com.decompiler.bytecode.analysis.structured.StructuredStatement;
import com.decompiler.bytecode.analysis.types.discovery.InferredJavaType;
import com.decompiler.state.TypeUsageCollector;
import com.decompiler.util.output.Dumper;

public class StructuredStatementExpression extends AbstractExpression {

    private StructuredStatement content;

    public StructuredStatementExpression(InferredJavaType inferredJavaType, StructuredStatement content) {
        super(BytecodeLoc.TODO, inferredJavaType);
        this.content = content;
    }

    @Override
    public BytecodeLoc getCombinedLoc() {
        return BytecodeLoc.TODO;
    }

    /*
     * This is sub optimal - we shouldn't be shallow copying here, but I don't
     * want to add deepClone to the structuredStatement.
     */
    @Override
    public Expression deepClone(CloneHelper cloneHelper) {
        return this;
    }

    @Override
    public void collectTypeUsages(TypeUsageCollector collector) {
        content.collectTypeUsages(collector);
    }

    @Override
    public Expression replaceSingleUsageLValues(LValueRewriter lValueRewriter, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Expression applyExpressionRewriter(ExpressionRewriter expressionRewriter, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer, ExpressionRewriterFlags flags) {
        StructuredScope scope = new StructuredScope();
        scope.add(content);
        new ExpressionRewriterTransformer(expressionRewriter).transform(content, scope);
        return this;
    }

    @Override
    public Expression applyReverseExpressionRewriter(ExpressionRewriter expressionRewriter, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer, ExpressionRewriterFlags flags) {
        return applyExpressionRewriter(expressionRewriter, ssaIdentifiers, statementContainer, flags);
    }

    @Override
    public void collectUsedLValues(LValueUsageCollector lValueUsageCollector) {
        // Fugly.  TODO: Fix interface.
        if (lValueUsageCollector instanceof LValueScopeDiscoverer) {
            LValueScopeDiscoverer scopeDiscoverer = (LValueScopeDiscoverer) lValueUsageCollector;
//            scopeDiscoverer.enterBlock(content);
            content.traceLocalVariableScope(scopeDiscoverer);
//            scopeDiscoverer.leaveBlock(content);
        }
    }


    @Override
    public Precedence getPrecedence() {
        return Precedence.WEAKEST;
    }

    public StructuredStatement getContent() {
        return content;
    }

    @Override
    public Dumper dumpInner(Dumper d) {
        return content.dump(d);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StructuredStatementExpression that = (StructuredStatementExpression) o;

        if (!content.equals(that.content)) return false;

        return true;
    }

    @Override
    public final boolean equivalentUnder(Object o, EquivalenceConstraint constraint) {
        if (o == null) return false;
        if (o == this) return true;
        if (getClass() != o.getClass()) return false;
        StructuredStatementExpression other = (StructuredStatementExpression) o;
        if (!constraint.equivalent(content, other.content)) return false;
        return true;
    }

}

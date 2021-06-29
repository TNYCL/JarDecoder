package com.decompiler.bytecode.analysis.structured.statement;

import java.util.List;
import java.util.Set;
import java.util.Stack;

import com.decompiler.bytecode.analysis.loc.BytecodeLoc;
import com.decompiler.bytecode.analysis.opgraph.Op04StructuredStatement;
import com.decompiler.bytecode.analysis.opgraph.op4rewriters.matchutil.MatchIterator;
import com.decompiler.bytecode.analysis.opgraph.op4rewriters.matchutil.MatchResultCollector;
import com.decompiler.bytecode.analysis.opgraph.op4rewriters.transformers.StructuredStatementTransformer;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriter;
import com.decompiler.bytecode.analysis.parse.utils.BlockIdentifier;
import com.decompiler.bytecode.analysis.parse.utils.Triplet;
import com.decompiler.bytecode.analysis.parse.utils.scope.LValueScopeDiscoverer;
import com.decompiler.bytecode.analysis.structured.StructuredScope;
import com.decompiler.bytecode.analysis.structured.StructuredStatement;
import com.decompiler.state.TypeUsageCollector;
import com.decompiler.util.output.Dumper;

public class StructuredBreak extends AbstractStructuredStatement {

    private final BlockIdentifier breakBlock;

    private final boolean localBreak;

    public StructuredBreak(BytecodeLoc loc, BlockIdentifier breakBlock, boolean localBreak) {
        super(loc);
        this.breakBlock = breakBlock;
        this.localBreak = localBreak;
    }

    @Override
    public Dumper dump(Dumper dumper) {
        if (localBreak) {
            dumper.keyword("break").print(";");
        } else {
            dumper.keyword("break ").print(breakBlock.getName() + ";");
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

    public boolean isLocalBreak() {
        return localBreak;
    }

    public BlockIdentifier getBreakBlock() {
        return breakBlock;
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
        if (!(o instanceof StructuredBreak)) return false;
        StructuredBreak other = (StructuredBreak) o;
        if (!breakBlock.equals(other.breakBlock)) return false;
        // Don't check locality.
        matchIterator.advance();
        return true;
    }

    @Override
    public void traceLocalVariableScope(LValueScopeDiscoverer scopeDiscoverer) {
    }

    @Override
    public void rewriteExpressions(ExpressionRewriter expressionRewriter) {
    }

    public StructuredBreak maybeTightenToLocal(Stack<Triplet<StructuredStatement, BlockIdentifier, Set<Op04StructuredStatement>>> scopes) {
        if (localBreak) return this;
        /*
         * Can't improve if no targets.
         */
        if (scopes.isEmpty()) {
            return this;
        }
        /*
         * ok, it's not local.  Go up the scopes, and find the enclosing block, then see if the innermost breakable also
         * falls through to the same target.  If so, we can convert it to a local break.
         */
        Triplet<StructuredStatement, BlockIdentifier, Set<Op04StructuredStatement>> local = scopes.peek();
        if (local.getSecond() == breakBlock) {
            // well this is wrong.  Should be marked as a local break!
            return this;
        }
        for (int i = scopes.size() - 2; i >= 0; i--) {
            Triplet<StructuredStatement, BlockIdentifier, Set<Op04StructuredStatement>> scope = scopes.get(i);
            if (scope.getSecond() == breakBlock) {
                // Ok, this is the actual block we're breaking out of.
                Set<Op04StructuredStatement> localNext = local.getThird();
                Set<Op04StructuredStatement> actualNext = scope.getThird();
                if (localNext.containsAll(actualNext)) {
                    breakBlock.releaseForeignRef();
                    return new StructuredBreak(getLoc(), local.getSecond(), true);
                } else {
                    return this;
                }
            }
        }
        return this;
    }
}

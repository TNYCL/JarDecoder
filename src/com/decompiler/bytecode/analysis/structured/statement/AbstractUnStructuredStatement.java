package com.decompiler.bytecode.analysis.structured.statement;

import java.util.List;

import com.decompiler.bytecode.analysis.loc.BytecodeLoc;
import com.decompiler.bytecode.analysis.opgraph.op4rewriters.matchutil.MatchIterator;
import com.decompiler.bytecode.analysis.opgraph.op4rewriters.matchutil.MatchResultCollector;
import com.decompiler.bytecode.analysis.opgraph.op4rewriters.transformers.StructuredStatementTransformer;
import com.decompiler.bytecode.analysis.parse.LValue;
import com.decompiler.bytecode.analysis.parse.lvalue.LocalVariable;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriter;
import com.decompiler.bytecode.analysis.parse.utils.BlockIdentifier;
import com.decompiler.bytecode.analysis.parse.utils.scope.LValueScopeDiscoverer;
import com.decompiler.bytecode.analysis.structured.StructuredScope;
import com.decompiler.bytecode.analysis.structured.StructuredStatement;
import com.decompiler.util.functors.Predicate;

public abstract class AbstractUnStructuredStatement extends AbstractStructuredStatement {

    public AbstractUnStructuredStatement(BytecodeLoc loc) {
        super(loc);
    }

    @Override
    public final void transformStructuredChildren(StructuredStatementTransformer transformer, StructuredScope scope) {
    }

    @Override
    public final void transformStructuredChildrenInReverse(StructuredStatementTransformer transformer, StructuredScope scope) {
    }

    @Override
    public final boolean isProperlyStructured() {
        return false;
    }

    @Override
    public BlockIdentifier getBreakableBlockOrNull() {
        return null;
    }

    @Override
    public final boolean isRecursivelyStructured() {
        return false;
    }

    @Override
    public void linearizeInto(List<StructuredStatement> out) {
        throw new UnsupportedOperationException("Can't linearise an unstructured statement");
    }

    @Override
    public void traceLocalVariableScope(LValueScopeDiscoverer scopeDiscoverer) {
    }

    @Override
    public boolean match(MatchIterator<StructuredStatement> matchIterator, MatchResultCollector matchResultCollector) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void rewriteExpressions(ExpressionRewriter expressionRewriter) {
    }

    @Override
    public boolean isEffectivelyNOP() {
        return false;
    }

    @Override
    public List<LValue> findCreatedHere() {
        return null;
    }

    @Override
    public String suggestName(LocalVariable createdHere, Predicate<String> testNameUsedFn) {
        return null;
    }
}

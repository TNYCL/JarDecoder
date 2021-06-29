package com.decompiler.bytecode.analysis.structured.statement.placeholder;

import java.util.List;
import java.util.Vector;

import com.decompiler.bytecode.analysis.loc.BytecodeLoc;
import com.decompiler.bytecode.analysis.loc.HasByteCodeLoc;
import com.decompiler.bytecode.analysis.opgraph.Op04StructuredStatement;
import com.decompiler.bytecode.analysis.opgraph.op4rewriters.transformers.StructuredStatementTransformer;
import com.decompiler.bytecode.analysis.parse.LValue;
import com.decompiler.bytecode.analysis.parse.StatementContainer;
import com.decompiler.bytecode.analysis.parse.lvalue.LocalVariable;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriter;
import com.decompiler.bytecode.analysis.parse.utils.BlockIdentifier;
import com.decompiler.bytecode.analysis.parse.utils.scope.LValueScopeDiscoverer;
import com.decompiler.bytecode.analysis.parse.utils.scope.ScopeDiscoverInfoCache;
import com.decompiler.bytecode.analysis.structured.StructuredScope;
import com.decompiler.bytecode.analysis.structured.StructuredStatement;
import com.decompiler.state.TypeUsageCollector;
import com.decompiler.util.functors.Predicate;
import com.decompiler.util.output.Dumper;

public abstract class AbstractPlaceholder implements StructuredStatement {
    @Override
    public void linearizeInto(List<StructuredStatement> out) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void collectTypeUsages(TypeUsageCollector collector) {
    }

    @Override
    public BytecodeLoc getCombinedLoc() {
        return BytecodeLoc.NONE;
    }

    @Override
    public BytecodeLoc getLoc() {
        return BytecodeLoc.NONE;
    }

    @Override
    public void addLoc(HasByteCodeLoc loc) {
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
    public BlockIdentifier getBreakableBlockOrNull() {
        return null;
    }

    @Override
    public boolean fallsNopToNext() {
        return false;
    }

    @Override
    public boolean isScopeBlock() {
        return false;
    }

    @Override
    public void transformStructuredChildren(StructuredStatementTransformer transformer, StructuredScope scope) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void transformStructuredChildrenInReverse(StructuredStatementTransformer transformer, StructuredScope scope) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void rewriteExpressions(ExpressionRewriter expressionRewriter) {
    }

    @Override
    public StructuredStatement informBlockHeirachy(Vector<BlockIdentifier> blockIdentifiers) {
        throw new UnsupportedOperationException();
    }

    @Override
    public StructuredStatement claimBlock(Op04StructuredStatement innerBlock, BlockIdentifier blockIdentifier, Vector<BlockIdentifier> blocksCurrentlyIn) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Op04StructuredStatement getContainer() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setContainer(Op04StructuredStatement container) {
        throw new UnsupportedOperationException();
    }

    // These should never make it into generated code.
    @Override
    public void traceLocalVariableScope(LValueScopeDiscoverer scopeDiscoverer) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void markCreator(LValue scopedEntity, StatementContainer<StructuredStatement> hint) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean alwaysDefines(LValue scopedEntity) {
        return false;
    }

    @Override
    public boolean canDefine(LValue scopedEntity, ScopeDiscoverInfoCache factCache) {
        return false;
    }

    @Override
    public boolean canFall() {
        return true;
    }

    @Override
    public List<LValue> findCreatedHere() {
        return null;
    }

    @Override
    public String suggestName(LocalVariable createdHere, Predicate<String> testNameUsedFn) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Dumper dump(Dumper dumper) {
        return dumper;
    }

    @Override
    public boolean inlineable() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Op04StructuredStatement getInline() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEffectivelyNOP() {
        throw new UnsupportedOperationException();
    }
}

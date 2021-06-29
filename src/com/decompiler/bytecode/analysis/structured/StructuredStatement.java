package com.decompiler.bytecode.analysis.structured;

import java.util.List;
import java.util.Vector;

import com.decompiler.bytecode.analysis.loc.HasByteCodeLoc;
import com.decompiler.bytecode.analysis.opgraph.Op04StructuredStatement;
import com.decompiler.bytecode.analysis.opgraph.op4rewriters.matchutil.Matcher;
import com.decompiler.bytecode.analysis.opgraph.op4rewriters.transformers.StructuredStatementTransformer;
import com.decompiler.bytecode.analysis.parse.LValue;
import com.decompiler.bytecode.analysis.parse.StatementContainer;
import com.decompiler.bytecode.analysis.parse.lvalue.LocalVariable;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriter;
import com.decompiler.bytecode.analysis.parse.utils.BlockIdentifier;
import com.decompiler.bytecode.analysis.parse.utils.scope.LValueScopeDiscoverer;
import com.decompiler.bytecode.analysis.parse.utils.scope.ScopeDiscoverInfoCache;
import com.decompiler.util.TypeUsageCollectable;
import com.decompiler.util.functors.Predicate;
import com.decompiler.util.output.Dumpable;

public interface StructuredStatement extends Dumpable, TypeUsageCollectable, HasByteCodeLoc, Matcher<StructuredStatement> {

    Op04StructuredStatement getContainer();

    void setContainer(Op04StructuredStatement container);

    StructuredStatement claimBlock(Op04StructuredStatement innerBlock, BlockIdentifier blockIdentifier, Vector<BlockIdentifier> blocksCurrentlyIn);

    StructuredStatement informBlockHeirachy(Vector<BlockIdentifier> blockIdentifiers);

    void transformStructuredChildren(StructuredStatementTransformer transformer, StructuredScope scope);

    void transformStructuredChildrenInReverse(StructuredStatementTransformer transformer, StructuredScope scope);

    // This isn't recursive - maybe it should be.
    void rewriteExpressions(ExpressionRewriter expressionRewriter);

    /*
     * Is THIS a structured statement?
     */
    boolean isProperlyStructured();

    /*
     * Is this and its children structured?
     */
    boolean isRecursivelyStructured();

    BlockIdentifier getBreakableBlockOrNull();

    void linearizeInto(List<StructuredStatement> out);

    void traceLocalVariableScope(LValueScopeDiscoverer scopeDiscoverer);

    void markCreator(LValue scopedEntity, StatementContainer<StructuredStatement> hint);

    boolean alwaysDefines(LValue scopedEntity);

    boolean canDefine(LValue scopedEntity, ScopeDiscoverInfoCache factCache);

    boolean supportsContinueBreak();

    boolean supportsBreak();

    boolean isScopeBlock();

    boolean inlineable();

    Op04StructuredStatement getInline();

    // Is it a comment, or a block containing nothign but comments?
    boolean isEffectivelyNOP();

    boolean fallsNopToNext();

    boolean canFall();

    List<LValue> findCreatedHere();

    String suggestName(LocalVariable createdHere, Predicate<String> testNameUsedFn);
}
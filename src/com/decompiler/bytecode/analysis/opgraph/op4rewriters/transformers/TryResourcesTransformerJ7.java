package com.decompiler.bytecode.analysis.opgraph.op4rewriters.transformers;

import java.util.List;

import com.decompiler.bytecode.analysis.loc.BytecodeLoc;
import com.decompiler.bytecode.analysis.opgraph.Op04StructuredStatement;
import com.decompiler.bytecode.analysis.opgraph.op4rewriters.ResourceReleaseDetector;
import com.decompiler.bytecode.analysis.opgraph.op4rewriters.matchutil.*;
import com.decompiler.bytecode.analysis.opgraph.op4rewriters.util.MiscStatementTools;
import com.decompiler.bytecode.analysis.parse.LValue;
import com.decompiler.bytecode.analysis.parse.expression.*;
import com.decompiler.bytecode.analysis.parse.wildcard.WildcardMatch;
import com.decompiler.bytecode.analysis.structured.StructuredStatement;
import com.decompiler.bytecode.analysis.structured.statement.*;
import com.decompiler.bytecode.analysis.structured.statement.placeholder.BeginBlock;
import com.decompiler.bytecode.analysis.structured.statement.placeholder.EndBlock;
import com.decompiler.entities.ClassFile;

public class TryResourcesTransformerJ7 extends TryResourceTransformerFinally {
    public TryResourcesTransformerJ7(ClassFile classFile) {
        super(classFile);
    }

    @Override
    protected ResourceMatch findResourceFinally(Op04StructuredStatement finallyBlock) {
        if (finallyBlock == null) return null;
        StructuredFinally finalli = (StructuredFinally)finallyBlock.getStatement();
        Op04StructuredStatement content = finalli.getCatchBlock();

        WildcardMatch wcm = new WildcardMatch();
        List<StructuredStatement> structuredStatements = MiscStatementTools.linearise(content);
        if (structuredStatements == null) return null;

        WildcardMatch.LValueWildcard throwableLValue = wcm.getLValueWildCard("throwable");
        WildcardMatch.LValueWildcard autoclose = wcm.getLValueWildCard("resource");

        Matcher<StructuredStatement> subMatch = ResourceReleaseDetector.getStructuredStatementMatcher(wcm, throwableLValue, autoclose);

        //noinspection unchecked
        Matcher<StructuredStatement> m = new MatchOneOf(
                new ResetAfterTest(wcm,
                    new MatchSequence(
                        new BeginBlock(null),
                        new StructuredIf(BytecodeLoc.NONE, new ComparisonOperation(BytecodeLoc.TODO, new LValueExpression(autoclose), Literal.NULL, CompOp.NE), null),
                        subMatch,
                        new EndBlock(null)
                    )
                ),
                new ResetAfterTest(wcm, subMatch));

        MatchIterator<StructuredStatement> mi = new MatchIterator<StructuredStatement>(structuredStatements);

        TryResourcesMatchResultCollector collector = new TryResourcesMatchResultCollector();
        mi.advance();
        boolean res = m.match(mi, collector);
        if (!res) return null;

        LValue resource = collector.resource;
        LValue throwable = collector.throwable;

        // Because we don't have an explicit close method, we need to check types of arguments.
        // resource must cast back to AutoClosable.
        // except, prior to J9, closable didn't inherit from Autoclosable, so test for closable.
        return new ResourceMatch(null, resource, throwable);
    }
}

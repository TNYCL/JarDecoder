package com.decompiler.bytecode.analysis.structured.statement.placeholder;

import com.decompiler.bytecode.analysis.opgraph.op4rewriters.matchutil.MatchIterator;
import com.decompiler.bytecode.analysis.opgraph.op4rewriters.matchutil.MatchResultCollector;
import com.decompiler.bytecode.analysis.structured.StructuredStatement;

public class ElseBlock extends AbstractPlaceholder {
    @Override
    public boolean match(MatchIterator<StructuredStatement> matchIterator, MatchResultCollector matchResultCollector) {
        if (matchIterator.getCurrent() instanceof ElseBlock) {
            matchIterator.advance();
            return true;
        }
        return false;
    }

    @Override
    public boolean supportsContinueBreak() {
        return false;
    }

    @Override
    public boolean supportsBreak() {
        return false;
    }
}

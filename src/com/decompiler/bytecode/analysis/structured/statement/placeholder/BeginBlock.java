package com.decompiler.bytecode.analysis.structured.statement.placeholder;

import com.decompiler.bytecode.analysis.opgraph.op4rewriters.matchutil.MatchIterator;
import com.decompiler.bytecode.analysis.opgraph.op4rewriters.matchutil.MatchResultCollector;
import com.decompiler.bytecode.analysis.structured.StructuredStatement;
import com.decompiler.bytecode.analysis.structured.statement.Block;

public class BeginBlock extends AbstractPlaceholder {
    private final Block block;

    public BeginBlock(Block block) {
        this.block = block;
    }

    public Block getBlock() {
        return block;
    }

    @Override
    public boolean match(MatchIterator<StructuredStatement> matchIterator, MatchResultCollector matchResultCollector) {
        StructuredStatement current = matchIterator.getCurrent();
        if (current instanceof BeginBlock) {
            BeginBlock other = (BeginBlock) current;
            if (block == null || block.equals(other.block)) {
                matchIterator.advance();
                return true;
            }
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

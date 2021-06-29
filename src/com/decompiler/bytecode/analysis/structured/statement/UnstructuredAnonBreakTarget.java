package com.decompiler.bytecode.analysis.structured.statement;

import java.util.List;

import com.decompiler.bytecode.analysis.loc.BytecodeLoc;
import com.decompiler.bytecode.analysis.parse.utils.BlockIdentifier;
import com.decompiler.bytecode.analysis.structured.StructuredStatement;
import com.decompiler.state.TypeUsageCollector;
import com.decompiler.util.output.Dumper;

public class UnstructuredAnonBreakTarget extends AbstractUnStructuredStatement {
    private BlockIdentifier blockIdentifier;

    public UnstructuredAnonBreakTarget(BlockIdentifier blockIdentifier) {
        super(BytecodeLoc.NONE);
        this.blockIdentifier = blockIdentifier;
    }

    @Override
    public BytecodeLoc getCombinedLoc() {
        return getLoc();
    }

    @Override
    public void collectTypeUsages(TypeUsageCollector collector) {
    }

    @Override
    public Dumper dump(Dumper dumper) {
        return dumper;
    }

    // Lose the comments.
    @Override
    public void linearizeInto(List<StructuredStatement> out) {
    }

    public BlockIdentifier getBlockIdentifier() {
        return blockIdentifier;
    }

    @Override
    public boolean isEffectivelyNOP() {
        return true;
    }
}

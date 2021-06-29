package com.decompiler.bytecode.analysis.structured.statement;

import java.util.Vector;

import com.decompiler.bytecode.analysis.loc.BytecodeLoc;
import com.decompiler.bytecode.analysis.parse.utils.BlockIdentifier;
import com.decompiler.bytecode.analysis.structured.StructuredStatement;
import com.decompiler.state.TypeUsageCollector;
import com.decompiler.util.output.Dumper;

public class UnstructuredAnonymousBreak extends AbstractUnStructuredStatement {

    private final BlockIdentifier blockEnding;

    public UnstructuredAnonymousBreak(BytecodeLoc loc, BlockIdentifier blockEnding) {
        super(loc);
        this.blockEnding = blockEnding;
    }

    @Override
    public BytecodeLoc getCombinedLoc() {
        return getLoc();
    }

    @Override
    public Dumper dump(Dumper dumper) {
        return dumper.print("** break ").print(blockEnding.getName()).newln();
    }

    @Override
    public void collectTypeUsages(TypeUsageCollector collector) {
    }

    @Override
    public StructuredStatement informBlockHeirachy(Vector<BlockIdentifier> blockIdentifiers) {
        return null;
    }

    StructuredStatement tryExplicitlyPlaceInBlock(BlockIdentifier block) {
        if (block != blockEnding) {
            return this;
        }
        block.addForeignRef();
        return new StructuredBreak(getLoc(), block, false);
    }
}

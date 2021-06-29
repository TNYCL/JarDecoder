package com.decompiler.bytecode.analysis.structured.statement;

import java.util.Vector;

import com.decompiler.bytecode.analysis.loc.BytecodeLoc;
import com.decompiler.bytecode.analysis.opgraph.Op04StructuredStatement;
import com.decompiler.bytecode.analysis.parse.utils.BlockIdentifier;
import com.decompiler.bytecode.analysis.structured.StructuredStatement;
import com.decompiler.entities.exceptions.ExceptionGroup;
import com.decompiler.state.TypeUsageCollector;
import com.decompiler.util.output.Dumper;

public class UnstructuredTry extends AbstractUnStructuredStatement {
    private final ExceptionGroup exceptionGroup;

    public UnstructuredTry(ExceptionGroup exceptionGroup) {
        super(BytecodeLoc.NONE);
        this.exceptionGroup = exceptionGroup;
    }

    @Override
    public BytecodeLoc getCombinedLoc() {
        return getLoc();
    }

    @Override
    public Dumper dump(Dumper dumper) {
        return dumper.print("** try " + exceptionGroup + " { ").newln();
    }

    @Override
    public void collectTypeUsages(TypeUsageCollector collector) {
    }

    StructuredStatement getEmptyTry() {
        return new StructuredTry(new Op04StructuredStatement(Block.getEmptyBlock(true)), exceptionGroup.getTryBlockIdentifier());
    }

    @Override
    public StructuredStatement claimBlock(Op04StructuredStatement innerBlock, BlockIdentifier blockIdentifier, Vector<BlockIdentifier> blocksCurrentlyIn) {
        if (blockIdentifier == exceptionGroup.getTryBlockIdentifier()) {
            return new StructuredTry(innerBlock, blockIdentifier);
        } else {
            return null;
        }
    }
}

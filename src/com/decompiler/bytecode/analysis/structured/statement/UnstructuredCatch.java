package com.decompiler.bytecode.analysis.structured.statement;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import com.decompiler.bytecode.analysis.loc.BytecodeLoc;
import com.decompiler.bytecode.analysis.opgraph.Op04StructuredStatement;
import com.decompiler.bytecode.analysis.parse.LValue;
import com.decompiler.bytecode.analysis.parse.utils.BlockIdentifier;
import com.decompiler.bytecode.analysis.structured.StructuredStatement;
import com.decompiler.bytecode.analysis.types.JavaRefTypeInstance;
import com.decompiler.entities.exceptions.ExceptionGroup;
import com.decompiler.state.TypeUsageCollector;
import com.decompiler.util.collections.MapFactory;
import com.decompiler.util.collections.SetFactory;
import com.decompiler.util.output.Dumper;

public class UnstructuredCatch extends AbstractUnStructuredStatement {
    private final List<ExceptionGroup.Entry> exceptions;
    private final BlockIdentifier blockIdentifier;
    private final LValue catching;

    public UnstructuredCatch(List<ExceptionGroup.Entry> exceptions, BlockIdentifier blockIdentifier, LValue catching) {
        super(BytecodeLoc.NONE);
        this.exceptions = exceptions;
        this.blockIdentifier = blockIdentifier;
        this.catching = catching;
    }

    @Override
    public Dumper dump(Dumper dumper) {
        dumper.print("** catch " + exceptions + " { ").newln();
        return dumper;
    }

    @Override
    public BytecodeLoc getCombinedLoc() {
        return getLoc();
    }

    @Override
    public void collectTypeUsages(TypeUsageCollector collector) {
        for (ExceptionGroup.Entry entry : exceptions) {
            collector.collect(entry.getCatchType());
        }
    }

    private StructuredStatement getCatchFor(Op04StructuredStatement innerBlock) {
        /*
         * Get the unique set of exception types.
         */
        Map<String, JavaRefTypeInstance> catchTypes = MapFactory.newTreeMap();
        Set<BlockIdentifier> possibleTryBlocks = SetFactory.newSet();
        for (ExceptionGroup.Entry entry : exceptions) {
            JavaRefTypeInstance typ = entry.getCatchType();
            catchTypes.put(typ.getRawName(), typ);
            possibleTryBlocks.add(entry.getTryBlockIdentifier());
        }
        return new StructuredCatch(catchTypes.values(), innerBlock, catching, possibleTryBlocks);
    }

    public StructuredStatement getCatchForEmpty() {
        return getCatchFor(new Op04StructuredStatement(Block.getEmptyBlock(true)));
    }

    @Override
    public StructuredStatement claimBlock(Op04StructuredStatement innerBlock, BlockIdentifier blockIdentifier, Vector<BlockIdentifier> blocksCurrentlyIn) {
        if (blockIdentifier == this.blockIdentifier) {
            /*
             * Convert to types (should verify elsewhere that there's only 1.
             */
            return getCatchFor(innerBlock);
        } else {
            return null;
        }
    }
}

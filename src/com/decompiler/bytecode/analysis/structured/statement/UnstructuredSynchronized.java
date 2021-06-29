package com.decompiler.bytecode.analysis.structured.statement;

import java.util.Vector;

import com.decompiler.bytecode.analysis.loc.BytecodeLoc;
import com.decompiler.bytecode.analysis.opgraph.Op04StructuredStatement;
import com.decompiler.bytecode.analysis.parse.Expression;
import com.decompiler.bytecode.analysis.parse.utils.BlockIdentifier;
import com.decompiler.bytecode.analysis.structured.StructuredStatement;
import com.decompiler.state.TypeUsageCollector;
import com.decompiler.util.output.Dumper;

public class UnstructuredSynchronized extends AbstractUnStructuredStatement {
    private Expression monitor;
    private BlockIdentifier blockIdentifier;

    public UnstructuredSynchronized(BytecodeLoc loc, Expression monitor, BlockIdentifier blockIdentifier) {
        super(loc);
        this.monitor = monitor;
        this.blockIdentifier = blockIdentifier;
    }

    @Override
    public BytecodeLoc getCombinedLoc() {
        return BytecodeLoc.combine(this, monitor);
    }

    @Override
    public void collectTypeUsages(TypeUsageCollector collector) {
        monitor.collectTypeUsages(collector);
    }

    @Override
    public Dumper dump(Dumper dumper) {
        return dumper.print("** synchronized (").dump(monitor).separator(")").newln();
    }

    @Override
    public StructuredStatement claimBlock(Op04StructuredStatement innerBlock, BlockIdentifier blockIdentifier, Vector<BlockIdentifier> blocksCurrentlyIn) {
        if (blockIdentifier != this.blockIdentifier) {
            throw new RuntimeException("MONITOREXIT statement claiming wrong block");
        }

        return new StructuredSynchronized(getLoc(), monitor, innerBlock);
    }

}

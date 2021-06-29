package com.decompiler.bytecode.analysis.structured.statement;

import java.util.Vector;

import com.decompiler.bytecode.analysis.loc.BytecodeLoc;
import com.decompiler.bytecode.analysis.opgraph.Op04StructuredStatement;
import com.decompiler.bytecode.analysis.parse.Expression;
import com.decompiler.bytecode.analysis.parse.LValue;
import com.decompiler.bytecode.analysis.parse.utils.BlockIdentifier;
import com.decompiler.bytecode.analysis.structured.StructuredStatement;
import com.decompiler.state.TypeUsageCollector;
import com.decompiler.util.output.Dumper;

public class UnstructuredIter extends AbstractUnStructuredStatement {
    private BlockIdentifier blockIdentifier;
    private LValue iterator;
    private Expression list;

    public UnstructuredIter(BytecodeLoc loc, BlockIdentifier blockIdentifier, LValue iterator, Expression list) {
        super(loc);
        this.blockIdentifier = blockIdentifier;
        this.iterator = iterator;
        this.list = list;
    }

    @Override
    public Dumper dump(Dumper dumper) {
        return dumper.print("** for (").dump(iterator).print(" : ").dump(list).separator(")").newln();
    }

    @Override
    public BytecodeLoc getCombinedLoc() {
        return BytecodeLoc.combine(this, list);
    }

    @Override
    public void collectTypeUsages(TypeUsageCollector collector) {
        iterator.collectTypeUsages(collector);
        collector.collectFrom(list);
    }

    @Override
    public StructuredStatement claimBlock(Op04StructuredStatement innerBlock, BlockIdentifier blockIdentifier, Vector<BlockIdentifier> blocksCurrentlyIn) {
        if (blockIdentifier != this.blockIdentifier) {
            throw new RuntimeException("ForIter statement claiming wrong block");
        }
        innerBlock.removeLastContinue(blockIdentifier);
        return new StructuredIter(getLoc(), blockIdentifier, iterator, list, innerBlock);
    }


}

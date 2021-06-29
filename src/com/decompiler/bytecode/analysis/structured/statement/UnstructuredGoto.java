package com.decompiler.bytecode.analysis.structured.statement;

import com.decompiler.bytecode.analysis.loc.BytecodeLoc;
import com.decompiler.state.TypeUsageCollector;
import com.decompiler.util.output.Dumper;

public class UnstructuredGoto extends AbstractUnStructuredStatement {

    public UnstructuredGoto(BytecodeLoc loc) {
        super(loc);
    }

    @Override
    public BytecodeLoc getCombinedLoc() {
        return getLoc();
    }

    @Override
    public Dumper dump(Dumper dumper) {
        return dumper.print("** GOTO " + getContainer().getTargetLabel(0)).newln();
    }

    @Override
    public void collectTypeUsages(TypeUsageCollector collector) {
    }
}

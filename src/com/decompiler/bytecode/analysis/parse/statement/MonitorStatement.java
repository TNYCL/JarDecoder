package com.decompiler.bytecode.analysis.parse.statement;

import com.decompiler.bytecode.analysis.loc.BytecodeLoc;

public abstract class MonitorStatement extends AbstractStatement {
    public MonitorStatement(BytecodeLoc loc) {
        super(loc);
    }
}

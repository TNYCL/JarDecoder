package com.decompiler.bytecode.analysis.stack;

import com.decompiler.bytecode.analysis.types.StackTypes;

public interface StackDelta {
    boolean isNoOp();

    StackTypes getConsumed();

    StackTypes getProduced();

    long getChange();
}

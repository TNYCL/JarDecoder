package com.decompiler.bytecode.analysis.parse.utils;

import java.util.Set;

import com.decompiler.bytecode.analysis.parse.LValue;
import com.decompiler.util.collections.SetFactory;

public class LValueUsageCollectorSimpleRW implements LValueUsageCollector {
    private final Set<LValue> read = SetFactory.newSet();
    private final Set<LValue> write = SetFactory.newSet();

    @Override
    public void collect(LValue lValue, ReadWrite rw) {
        switch (rw) {
            case READ:
                read.add(lValue);
                break;
            case READ_WRITE:
                read.add(lValue);
            case WRITE:
                write.add(lValue);
                break;
        }
    }

    public Set<LValue> getRead() {
        return read;
    }

    public Set<LValue> getWritten() {
        return write;
    }
}

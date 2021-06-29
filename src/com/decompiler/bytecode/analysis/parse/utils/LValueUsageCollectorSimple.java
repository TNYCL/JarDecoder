package com.decompiler.bytecode.analysis.parse.utils;

import java.util.Collection;
import java.util.Set;

import com.decompiler.bytecode.analysis.parse.LValue;
import com.decompiler.util.collections.SetFactory;

public class LValueUsageCollectorSimple implements LValueUsageCollector {
    private final Set<LValue> used = SetFactory.newSet();

    @Override
    public void collect(LValue lValue, ReadWrite rw) {
        used.add(lValue);
    }

    public Collection<LValue> getUsedLValues() {
        return used;
    }

    public boolean isUsed(LValue lValue) {
        return used.contains(lValue);
    }
}

package com.decompiler.bytecode.analysis.parse.utils;

import com.decompiler.bytecode.analysis.parse.LValue;

public interface LValueUsageCollector {
    void collect(LValue lValue, ReadWrite rw);
}

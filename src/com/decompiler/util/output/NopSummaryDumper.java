package com.decompiler.util.output;

import com.decompiler.bytecode.analysis.types.JavaTypeInstance;
import com.decompiler.entities.Method;

public class NopSummaryDumper implements SummaryDumper {

    @Override
    public void notify(String message) {
    }

    @Override
    public void notifyError(JavaTypeInstance controllingType, Method method, String error) {
    }

    @Override
    public void close() {
    }
}

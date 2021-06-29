package com.decompiler.util.output;

import com.decompiler.bytecode.analysis.types.JavaTypeInstance;
import com.decompiler.entities.Method;

public interface SummaryDumper {
    void notify(String message);

    void notifyError(JavaTypeInstance controllingType, Method method, String error);

    void close();
}

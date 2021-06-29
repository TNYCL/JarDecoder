package com.decompiler.util.output;

import com.decompiler.bytecode.analysis.types.JavaTypeInstance;

public interface ProgressDumper {
    void analysingType(JavaTypeInstance type);
    void analysingPath(String path);
}

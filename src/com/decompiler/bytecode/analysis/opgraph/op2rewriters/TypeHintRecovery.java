package com.decompiler.bytecode.analysis.opgraph.op2rewriters;

import com.decompiler.bytecode.analysis.types.discovery.InferredJavaType;

public interface TypeHintRecovery {
    void improve(InferredJavaType type);
}

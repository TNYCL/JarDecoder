package com.decompiler.bytecode.analysis.opgraph.op2rewriters;

import com.decompiler.bytecode.analysis.types.discovery.InferredJavaType;

public class TypeHintRecoveryNone implements TypeHintRecovery {
    public static final TypeHintRecoveryNone INSTANCE = new TypeHintRecoveryNone();

    private TypeHintRecoveryNone() {
    }

    @Override
    public void improve(InferredJavaType type) {}
}

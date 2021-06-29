package com.decompiler.entities.constantpool;

import com.decompiler.bytecode.analysis.types.StackType;

public interface ConstantPoolEntryLiteral {
    StackType getStackType();
}

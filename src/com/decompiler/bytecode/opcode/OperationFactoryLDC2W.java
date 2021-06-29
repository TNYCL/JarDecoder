package com.decompiler.bytecode.opcode;

public class OperationFactoryLDC2W extends OperationFactoryLDCW {
    @Override
    protected int getRequiredComputationCategory() {
        return 2;
    }
}

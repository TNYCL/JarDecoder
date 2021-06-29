package com.decompiler.bytecode.opcode;

import com.decompiler.bytecode.analysis.opgraph.Op01WithProcessedDataAndByteJumps;
import com.decompiler.entities.constantpool.ConstantPool;
import com.decompiler.util.bytestream.ByteData;

public class OperationFactoryGoto extends OperationFactoryDefault {

    private static final long OFFSET_OF_TARGET = 1;


    @Override
    public Op01WithProcessedDataAndByteJumps createOperation(JVMInstr instr, ByteData bd, ConstantPool cp, int offset) {
        byte[] args = bd.getBytesAt(instr.getRawLength(), 1);

        short targetOffset = bd.getS2At(OFFSET_OF_TARGET);

        int[] targetOffsets = new int[1];
        targetOffsets[0] = targetOffset;

        return new Op01WithProcessedDataAndByteJumps(instr, args, targetOffsets, offset);
    }
}

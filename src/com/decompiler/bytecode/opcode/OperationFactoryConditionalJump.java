package com.decompiler.bytecode.opcode;

import com.decompiler.bytecode.analysis.opgraph.Op01WithProcessedDataAndByteJumps;
import com.decompiler.entities.constantpool.ConstantPool;
import com.decompiler.util.bytestream.ByteData;

public class OperationFactoryConditionalJump extends OperationFactoryDefault {

    private static final long OFFSET_OF_TARGET = 1;

    @Override
    public Op01WithProcessedDataAndByteJumps createOperation(JVMInstr instr, ByteData bd, ConstantPool cp, int offset) {
        byte[] args = bd.getBytesAt(instr.getRawLength(), 1);

        short targetOffset = bd.getS2At(OFFSET_OF_TARGET);

        int[] targetOffsets = new int[2]; // next instr is either successor, or targetOffset.
        targetOffsets[1] = targetOffset;
        targetOffsets[0] = instr.getRawLength() + 1;

        return new Op01WithProcessedDataAndByteJumps(instr, args, targetOffsets, offset);
    }
}

package com.decompiler.bytecode.opcode;

import com.decompiler.bytecode.analysis.opgraph.Op01WithProcessedDataAndByteJumps;
import com.decompiler.entities.constantpool.ConstantPool;
import com.decompiler.entities.constantpool.ConstantPoolEntry;
import com.decompiler.util.bytestream.ByteData;

public class OperationFactoryNew extends OperationFactoryDefault {
    private static final int LENGTH_OF_CLASS_INDEX = 2;

    @Override
    public Op01WithProcessedDataAndByteJumps createOperation(JVMInstr instr, ByteData bd, ConstantPool cp, int offset) {
        byte[] args = bd.getBytesAt(LENGTH_OF_CLASS_INDEX, 1);
        ConstantPoolEntry[] cpEntries = new ConstantPoolEntry[]{cp.getEntry(bd.getU2At(1))};
        return new Op01WithProcessedDataAndByteJumps(instr, args, null, offset, cpEntries);
    }
}

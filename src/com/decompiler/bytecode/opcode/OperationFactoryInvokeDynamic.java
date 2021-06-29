package com.decompiler.bytecode.opcode;

import com.decompiler.bytecode.analysis.opgraph.Op01WithProcessedDataAndByteJumps;
import com.decompiler.bytecode.analysis.stack.StackDelta;
import com.decompiler.bytecode.analysis.stack.StackSim;
import com.decompiler.entities.*;
import com.decompiler.entities.constantpool.ConstantPool;
import com.decompiler.entities.constantpool.ConstantPoolEntry;
import com.decompiler.entities.constantpool.ConstantPoolEntryInvokeDynamic;
import com.decompiler.entities.constantpool.ConstantPoolEntryNameAndType;
import com.decompiler.util.bytestream.ByteData;

public class OperationFactoryInvokeDynamic extends OperationFactoryDefault {
    private static final int LENGTH_OF_FIELD_INDEX = 2;

    OperationFactoryInvokeDynamic() {
    }

    @Override
    public Op01WithProcessedDataAndByteJumps createOperation(JVMInstr instr, ByteData bd, ConstantPool cp, int offset) {
        byte[] args = bd.getBytesAt(LENGTH_OF_FIELD_INDEX + 2 /* padded with 0,0 */, 1);
        ConstantPoolEntry[] cpEntries = new ConstantPoolEntry[]{cp.getEntry(bd.getU2At(1))};
        return new Op01WithProcessedDataAndByteJumps(instr, args, null, offset, cpEntries);
    }

    @Override
    public StackDelta getStackDelta(JVMInstr instr, byte[] data, ConstantPoolEntry[] cpEntries,
                                    StackSim stackSim, Method method) {
        ConstantPoolEntryInvokeDynamic invokeDynamic = (ConstantPoolEntryInvokeDynamic) cpEntries[0];

        ConstantPoolEntryNameAndType nameAndType = invokeDynamic.getNameAndTypeEntry();
        return nameAndType.getStackDelta(false);
    }
}

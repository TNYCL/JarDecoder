package com.decompiler.bytecode.opcode;

import com.decompiler.bytecode.analysis.opgraph.Op01WithProcessedDataAndByteJumps;
import com.decompiler.bytecode.analysis.stack.StackDelta;
import com.decompiler.bytecode.analysis.stack.StackSim;
import com.decompiler.entities.Method;
import com.decompiler.entities.constantpool.ConstantPool;
import com.decompiler.entities.constantpool.ConstantPoolEntry;
import com.decompiler.entities.constantpool.ConstantPoolEntryMethodRef;
import com.decompiler.util.bytestream.ByteData;

public class OperationFactoryInvoke extends OperationFactoryDefault {
    private static final int LENGTH_OF_DATA = 2;
    private static final int OFFSET_OF_METHOD_INDEX = 1;
    private final boolean instance;

    OperationFactoryInvoke(boolean instance) {
        this.instance = instance;
    }

    @Override
    public Op01WithProcessedDataAndByteJumps createOperation(JVMInstr instr, ByteData bd, ConstantPool cp, int offset) {
        byte[] args = bd.getBytesAt(LENGTH_OF_DATA, 1);
        ConstantPoolEntry[] cpEntries = new ConstantPoolEntry[]{cp.getEntry(bd.getU2At(OFFSET_OF_METHOD_INDEX))};
        return new Op01WithProcessedDataAndByteJumps(instr, args, null, offset, cpEntries);
    }

    @Override
    public StackDelta getStackDelta(JVMInstr instr, byte[] data, ConstantPoolEntry[] cpEntries,
                                    StackSim stackSim, Method method) {
        ConstantPoolEntryMethodRef methodRef = (ConstantPoolEntryMethodRef) cpEntries[0];

        return methodRef.getNameAndTypeEntry().getStackDelta(instance);
    }
}

package com.decompiler.bytecode.opcode;

import com.decompiler.bytecode.analysis.opgraph.Op01WithProcessedDataAndByteJumps;
import com.decompiler.bytecode.analysis.stack.StackDelta;
import com.decompiler.bytecode.analysis.stack.StackDeltaImpl;
import com.decompiler.bytecode.analysis.stack.StackSim;
import com.decompiler.bytecode.analysis.types.StackType;
import com.decompiler.bytecode.analysis.types.StackTypes;
import com.decompiler.entities.Method;
import com.decompiler.entities.constantpool.ConstantPool;
import com.decompiler.entities.constantpool.ConstantPoolEntry;
import com.decompiler.util.bytestream.ByteData;

public class OperationFactoryPop2 extends OperationFactoryDefault {

    @Override
    public StackDelta getStackDelta(JVMInstr instr, byte[] data, ConstantPoolEntry[] cpEntries,
                                    StackSim stackSim, Method method) {
        StackType topStackEntry = stackSim.getEntry(0).getType();
        if (topStackEntry.getComputationCategory() == 2) {
            return new StackDeltaImpl(topStackEntry.asList(), StackTypes.EMPTY);
        } else {
            StackType nextStackEntry = stackSim.getEntry(1).getType();
            StackTypes stackTypesPopped = new StackTypes(topStackEntry, nextStackEntry);
            return new StackDeltaImpl(stackTypesPopped, StackTypes.EMPTY);
        }
    }

    @Override
    public Op01WithProcessedDataAndByteJumps createOperation(JVMInstr instr, ByteData bd, ConstantPool cp, int offset) {
        return new Op01WithProcessedDataAndByteJumps(instr, null, null, offset);
    }
}

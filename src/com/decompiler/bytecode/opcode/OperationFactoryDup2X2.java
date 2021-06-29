package com.decompiler.bytecode.opcode;

import com.decompiler.bytecode.analysis.opgraph.Op01WithProcessedDataAndByteJumps;
import com.decompiler.bytecode.analysis.stack.StackDelta;
import com.decompiler.bytecode.analysis.stack.StackDeltaImpl;
import com.decompiler.bytecode.analysis.stack.StackSim;
import com.decompiler.entities.Method;
import com.decompiler.entities.constantpool.ConstantPool;
import com.decompiler.entities.constantpool.ConstantPoolEntry;
import com.decompiler.util.bytestream.ByteData;

public class OperationFactoryDup2X2 extends OperationFactoryDupBase {

    @Override
    public StackDelta getStackDelta(JVMInstr instr, byte[] data, ConstantPoolEntry[] cpEntries,
                                    StackSim stackSim, Method method) {
        if (getCat(stackSim, 0) == 2) {
            if (getCat(stackSim, 1) == 2) {
                return new StackDeltaImpl(
                        getStackTypes(stackSim, 0, 1),
                        getStackTypes(stackSim, 0, 1, 0)
                );
            } else {
                checkCat(stackSim, 2, 1);
                return new StackDeltaImpl(
                        getStackTypes(stackSim, 0, 1, 2),
                        getStackTypes(stackSim, 0, 1, 2, 0)
                );
            }
        } else {
            if (getCat(stackSim, 2) == 2) {
                return new StackDeltaImpl(
                        getStackTypes(stackSim, 0, 1, 2),
                        getStackTypes(stackSim, 0, 1, 2, 0, 1)
                );
            } else {
                return new StackDeltaImpl(
                        getStackTypes(stackSim, 0, 1, 2, 3),
                        getStackTypes(stackSim, 0, 1, 2, 3, 0, 1)
                );
            }
        }
    }

    @Override
    public Op01WithProcessedDataAndByteJumps createOperation(JVMInstr instr, ByteData bd, ConstantPool cp, int offset) {
        return new Op01WithProcessedDataAndByteJumps(instr, null, null, offset);
    }
}

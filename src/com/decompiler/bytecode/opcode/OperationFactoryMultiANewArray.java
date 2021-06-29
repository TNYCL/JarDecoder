package com.decompiler.bytecode.opcode;

import java.util.List;

import com.decompiler.bytecode.analysis.opgraph.Op01WithProcessedDataAndByteJumps;
import com.decompiler.bytecode.analysis.stack.StackDelta;
import com.decompiler.bytecode.analysis.stack.StackDeltaImpl;
import com.decompiler.bytecode.analysis.stack.StackSim;
import com.decompiler.bytecode.analysis.types.StackType;
import com.decompiler.bytecode.analysis.types.StackTypes;
import com.decompiler.entities.Method;
import com.decompiler.entities.constantpool.ConstantPool;
import com.decompiler.entities.constantpool.ConstantPoolEntry;
import com.decompiler.util.ConfusedDecompilerException;
import com.decompiler.util.bytestream.ByteData;
import com.decompiler.util.collections.ListFactory;

public class OperationFactoryMultiANewArray extends OperationFactoryDefault {
    private static final int LENGTH_OF_DATA = 3;
    public static final int OFFSET_OF_DIMS = 2;

    @Override
    public Op01WithProcessedDataAndByteJumps createOperation(JVMInstr instr, ByteData bd, ConstantPool cp, int offset) {
        byte[] args = bd.getBytesAt(LENGTH_OF_DATA, 1);
        ConstantPoolEntry[] cpEntries = new ConstantPoolEntry[]{cp.getEntry(bd.getU2At(1))};
        return new Op01WithProcessedDataAndByteJumps(instr, args, null, offset, cpEntries);
    }

    @Override
    public StackDelta getStackDelta(JVMInstr instr, byte[] data, ConstantPoolEntry[] cpEntries,
                                    StackSim stackSim, Method method) {
        short numDims = data[OFFSET_OF_DIMS];
        if (numDims < 0) {
            throw new ConfusedDecompilerException("NYI : Unsupported num of dims, should be using a short not a byte.");
        }

        List<StackType> stackTypeList = ListFactory.newList();
        for (int x = 0; x < numDims; ++x) {
            stackTypeList.add(StackType.INT);
        }
        return new StackDeltaImpl(new StackTypes(stackTypeList), StackType.REF.asList());
    }
}

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

public class OperationFactoryFakeCatch extends OperationFactoryDefault {

    @Override
    public StackDelta getStackDelta(JVMInstr instr, byte[] data, ConstantPoolEntry[] cpEntries,
                                    StackSim stackSim, Method method) {
        StackTypes pushed = StackType.REF.asList();
        List<StackType> popped = ListFactory.newList();
        for (int x = 0; x < stackSim.getDepth(); ++x) {
            popped.add(stackSim.getEntry(x).getType());
        }
        return new StackDeltaImpl(new StackTypes(popped), pushed);
    }

    @Override
    public Op01WithProcessedDataAndByteJumps createOperation(JVMInstr instr, ByteData bd, ConstantPool cp, int offset) {
        throw new ConfusedDecompilerException("Fake catch should never be created from bytecode");
    }
}

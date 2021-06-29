package com.decompiler.bytecode.opcode;

import com.decompiler.bytecode.analysis.opgraph.Op01WithProcessedDataAndByteJumps;
import com.decompiler.bytecode.analysis.stack.StackDelta;
import com.decompiler.bytecode.analysis.stack.StackSim;
import com.decompiler.entities.Method;
import com.decompiler.entities.constantpool.ConstantPool;
import com.decompiler.entities.constantpool.ConstantPoolEntry;
import com.decompiler.util.bytestream.ByteData;

public interface OperationFactory {
    /*
     * When an operation is being decoded, it may need to know about the method / class it's in
     * (eg invokedynamic);
     */
    StackDelta getStackDelta(JVMInstr instr, byte[] data, ConstantPoolEntry[] cpEntries,
                             StackSim stackSim, Method method);

    Op01WithProcessedDataAndByteJumps createOperation(JVMInstr instr, ByteData bd, ConstantPool cp, int offset);
}

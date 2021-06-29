package com.decompiler.bytecode.opcode;

import com.decompiler.bytecode.analysis.stack.StackDelta;
import com.decompiler.bytecode.analysis.stack.StackDeltaImpl;
import com.decompiler.bytecode.analysis.stack.StackSim;
import com.decompiler.bytecode.analysis.types.StackType;
import com.decompiler.entities.Method;
import com.decompiler.entities.constantpool.ConstantPoolEntry;
import com.decompiler.entities.constantpool.ConstantPoolEntryFieldRef;
import com.decompiler.util.ConfusedDecompilerException;

public class OperationFactoryGetField extends OperationFactoryCPEntryW {

    @Override
    public StackDelta getStackDelta(JVMInstr instr, byte[] data, ConstantPoolEntry[] cpEntries,
                                    StackSim stackSim, Method method) {
        ConstantPoolEntryFieldRef fieldRef = (ConstantPoolEntryFieldRef) cpEntries[0];
        if (fieldRef == null) throw new ConfusedDecompilerException("Expecting fieldRef");
        StackType stackType = fieldRef.getStackType();
        return new StackDeltaImpl(StackType.REF.asList(), stackType.asList());
    }

}

package com.decompiler.bytecode.opcode;

import com.decompiler.bytecode.analysis.stack.StackDelta;
import com.decompiler.bytecode.analysis.stack.StackDeltaImpl;
import com.decompiler.bytecode.analysis.stack.StackSim;
import com.decompiler.bytecode.analysis.types.StackType;
import com.decompiler.bytecode.analysis.types.StackTypes;
import com.decompiler.entities.Method;
import com.decompiler.entities.constantpool.ConstantPoolEntry;
import com.decompiler.util.ConfusedDecompilerException;

public class OperationFactoryLDCW extends OperationFactoryCPEntryW {

    @Override
    public StackDelta getStackDelta(JVMInstr instr, byte[] data, ConstantPoolEntry[] cpEntries,
                                    StackSim stackSim, Method method) {
        StackType stackType = OperationFactoryLDC.getStackType(cpEntries[0]);
        int requiredComputationCategory = getRequiredComputationCategory();
        if (stackType.getComputationCategory() != requiredComputationCategory) {
            throw new ConfusedDecompilerException("Got a literal, but expected a different category");
        }

        return new StackDeltaImpl(StackTypes.EMPTY, stackType.asList());
    }

    protected int getRequiredComputationCategory() {
        return 1;
    }

}

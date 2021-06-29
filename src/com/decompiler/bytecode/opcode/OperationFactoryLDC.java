package com.decompiler.bytecode.opcode;

import com.decompiler.bytecode.analysis.stack.StackDelta;
import com.decompiler.bytecode.analysis.stack.StackDeltaImpl;
import com.decompiler.bytecode.analysis.stack.StackSim;
import com.decompiler.bytecode.analysis.types.JavaTypeInstance;
import com.decompiler.bytecode.analysis.types.StackType;
import com.decompiler.bytecode.analysis.types.StackTypes;
import com.decompiler.bytecode.analysis.types.TypeConstants;
import com.decompiler.entities.Method;
import com.decompiler.entities.constantpool.*;
import com.decompiler.util.ConfusedDecompilerException;

public class OperationFactoryLDC extends OperationFactoryCPEntry {

    @Override
    public StackDelta getStackDelta(JVMInstr instr, byte[] data, ConstantPoolEntry[] cpEntries,
                                    StackSim stackSim, Method method) {
        StackType stackType = getStackType(cpEntries[0]);
        int requiredComputationCategory = 1;
        if (stackType.getComputationCategory() != requiredComputationCategory) {
            throw new ConfusedDecompilerException("Got a literal, but expected a different category");
        }
        return new StackDeltaImpl(StackTypes.EMPTY, stackType.asList());
    }

    static StackType getStackType(ConstantPoolEntry cpe) {
        if (cpe instanceof ConstantPoolEntryLiteral) {
            ConstantPoolEntryLiteral constantPoolEntryLiteral = (ConstantPoolEntryLiteral) cpe;
            return constantPoolEntryLiteral.getStackType();
        }
        if (cpe instanceof ConstantPoolEntryDynamicInfo) {
            ConstantPoolEntryDynamicInfo di = (ConstantPoolEntryDynamicInfo) cpe;
            ConstantPoolEntryNameAndType nt = di.getNameAndTypeEntry();
            JavaTypeInstance type = nt.decodeTypeTok();
            return type.getStackType();
        }
        if (cpe instanceof ConstantPoolEntryMethodHandle) {
            return TypeConstants.METHOD_HANDLE.getStackType();
        }
        if (cpe instanceof ConstantPoolEntryMethodType) {
            return TypeConstants.METHOD_TYPE.getStackType();
        }
        throw new ConfusedDecompilerException("Expecting a ConstantPoolEntryLiteral or ConstantPoolEntryDynamicInfo");
    }
}

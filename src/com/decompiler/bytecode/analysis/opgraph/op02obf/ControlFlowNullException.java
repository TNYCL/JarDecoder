package com.decompiler.bytecode.analysis.opgraph.op02obf;

import java.util.List;
import java.util.SortedMap;

import com.decompiler.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs;
import com.decompiler.bytecode.analysis.opgraph.Op03SimpleStatement;
import com.decompiler.bytecode.analysis.types.JavaTypeInstance;
import com.decompiler.bytecode.analysis.types.MethodPrototype;
import com.decompiler.bytecode.analysis.types.TypeConstants;
import com.decompiler.bytecode.opcode.JVMInstr;
import com.decompiler.entities.Method;
import com.decompiler.entities.constantpool.ConstantPoolEntryMethodRef;
import com.decompiler.entities.exceptions.ExceptionAggregator;
import com.decompiler.entities.exceptions.ExceptionGroup;
import com.decompiler.util.MiscConstants;
import com.decompiler.util.collections.ListFactory;

public class ControlFlowNullException extends SimpleControlFlowBase {
    public static ControlFlowNullException Instance = new ControlFlowNullException();

    @Override
    protected boolean checkTry(List<Op02WithProcessedDataAndRefs> op2list, int from, int to, Op02WithProcessedDataAndRefs handlerJmp) {
        Op02WithProcessedDataAndRefs start = op2list.get(from);
        if (start.getInstr() != JVMInstr.INVOKEVIRTUAL) return false;
        Op02WithProcessedDataAndRefs tgt = start.getTargets().get(0);
        if (tgt.getInstr() != JVMInstr.POP) return false;

        ConstantPoolEntryMethodRef function = (ConstantPoolEntryMethodRef)start.getCpEntries()[0];
        MethodPrototype mp = function.getMethodPrototype();
        if (mp.getClassType() != TypeConstants.OBJECT) return false;
        if (!mp.getName().equals(MiscConstants.GET_CLASS_NAME)) return false;

        // nothing destructive till here - we could use this as test.
        start.replaceInstr(JVMInstr.IFNULL);
        tgt.nop();
        start.addTarget(handlerJmp);
        handlerJmp.addSource(start);
        return true;
    }

    @Override
    protected Op02WithProcessedDataAndRefs checkHandler(List<Op02WithProcessedDataAndRefs> op2list, int idx) {
        return getLastTargetIf(op2list, idx, JVMInstr.POP, JVMInstr.GOTO);
    }
}

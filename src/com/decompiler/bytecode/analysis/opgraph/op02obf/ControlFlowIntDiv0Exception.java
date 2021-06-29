package com.decompiler.bytecode.analysis.opgraph.op02obf;

import java.util.List;
import java.util.SortedMap;

import com.decompiler.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs;
import com.decompiler.bytecode.opcode.JVMInstr;
import com.decompiler.entities.Method;
import com.decompiler.entities.exceptions.ExceptionAggregator;
import com.decompiler.entities.exceptions.ExceptionGroup;
import com.decompiler.util.collections.ListFactory;

/*
 * Undo a very simple control flow obfuscation where integer division by 0 is used with an exception handler.
 */
public class ControlFlowIntDiv0Exception extends SimpleControlFlowBase {
    public static ControlFlowIntDiv0Exception Instance = new ControlFlowIntDiv0Exception();

    @Override
    protected boolean checkTry(List<Op02WithProcessedDataAndRefs> op2list, int from, int to, Op02WithProcessedDataAndRefs handlerJmp) {
        Op02WithProcessedDataAndRefs tgt = getLastTargetIf(op2list, from, JVMInstr.DUP, JVMInstr.IDIV, JVMInstr.POP);
        if (tgt == null) tgt = getLastTargetIf(op2list, from, JVMInstr.DUP, JVMInstr.IREM, JVMInstr.POP);
        if (tgt == null) return false;
        for (int x = from + 3; x < to; ++x) {
            if (!op2list.get(x).getInstr().isNoThrow()) return false;
        }

        // nothing destructive till here - we could use this as test.
        op2list.get(from).replaceInstr(JVMInstr.NOP);
        op2list.get(from+1).replaceInstr(JVMInstr.ICONST_0);
        Op02WithProcessedDataAndRefs op2 = op2list.get(from + 2);
        op2.replaceInstr(JVMInstr.IF_ICMPEQ);
        op2.getTargets().add(handlerJmp);
        handlerJmp.getSources().add(op2);
        return true;
    }

    @Override
    protected Op02WithProcessedDataAndRefs checkHandler(List<Op02WithProcessedDataAndRefs> op2list, int idx) {
        return getLastTargetIf(op2list, idx, JVMInstr.POP, JVMInstr.GOTO);
    }
}

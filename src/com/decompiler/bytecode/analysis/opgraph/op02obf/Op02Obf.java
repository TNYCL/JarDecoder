package com.decompiler.bytecode.analysis.opgraph.op02obf;

import java.util.List;
import java.util.SortedMap;

import com.decompiler.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs;
import com.decompiler.entities.Method;
import com.decompiler.entities.exceptions.ExceptionAggregator;

public class Op02Obf {
    public static void removeControlFlowExceptions(Method method, ExceptionAggregator exceptions, List<Op02WithProcessedDataAndRefs> op2list, SortedMap<Integer, Integer> lutByOffset) {
        ControlFlowIntDiv0Exception.Instance.process(method, exceptions, op2list, lutByOffset);
        ControlFlowNullException.Instance.process(method, exceptions, op2list, lutByOffset);
    }

    public static void removeNumericObf(Method method, List<Op02WithProcessedDataAndRefs> op2list) {
        ControlFlowNumericObf.Instance.process(method, op2list);
    }

    public static boolean detectObfuscations(Method method, ExceptionAggregator exceptions, List<Op02WithProcessedDataAndRefs> op2list, SortedMap<Integer, Integer> lutByOffset) {
        if (ControlFlowIntDiv0Exception.Instance.check(exceptions, op2list, lutByOffset)) return true;
        if (ControlFlowNullException.Instance.check(exceptions, op2list, lutByOffset)) return true;
        return false;
    }
}

package com.decompiler.bytecode.analysis.opgraph.op2rewriters;

import java.util.List;

import com.decompiler.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs;
import com.decompiler.bytecode.analysis.types.MethodPrototype;
import com.decompiler.bytecode.analysis.types.TypeConstants;
import com.decompiler.bytecode.opcode.JVMInstr;
import com.decompiler.entities.ClassFile;
import com.decompiler.entities.constantpool.ConstantPoolEntry;
import com.decompiler.entities.constantpool.ConstantPoolEntryClass;
import com.decompiler.entities.constantpool.ConstantPoolEntryMethodRef;
import com.decompiler.util.MiscConstants;

public class Op02GetClassRewriter {

    private static final Op02GetClassRewriter INSTANCE = new Op02GetClassRewriter();

    private Op02WithProcessedDataAndRefs getSinglePrev(Op02WithProcessedDataAndRefs item) {
        if (item.getSources().size() != 1) return null;
        Op02WithProcessedDataAndRefs prev = item.getSources().get(0);
        if (prev.getTargets().size() != 1) return null;
        return prev;
    }

    /*
     * This is all a very cheap and dirty way of doing things - replace with a regex?
     * Note that some getClass / requireNonNull is removed later when we make more explicit use of types
     * (see InstanceConstants).
     */
    private void tryRemove(ClassFile classFile, Op02WithProcessedDataAndRefs item, GetClassTest classTest) {

        Op02WithProcessedDataAndRefs pop = getSinglePrev(item);
        if (pop == null) return;

        if (pop.getInstr() != JVMInstr.POP) return;
        Op02WithProcessedDataAndRefs getClass = getSinglePrev(pop);
        if (getClass == null) return;
        if (!(isGetClass(getClass) || isRequireNonNull(getClass))) {
            return;
        }

        Op02WithProcessedDataAndRefs dup = getSinglePrev(getClass);
        if (dup == null) return;
        if (dup.getInstr() != JVMInstr.DUP) return;

        if (!classTest.test(classFile, item)) return;

        dup.nop();
        getClass.nop();
        pop.nop();
    }

    private boolean isGetClass(Op02WithProcessedDataAndRefs item) {
        ConstantPoolEntry[] cpEntries = item.getCpEntries();
        if(cpEntries == null || cpEntries.length == 0) return false;
        ConstantPoolEntry entry = cpEntries[0];
        if (!(entry instanceof ConstantPoolEntryMethodRef)) return false;
        ConstantPoolEntryMethodRef function = (ConstantPoolEntryMethodRef)entry;

        MethodPrototype methodPrototype = function.getMethodPrototype();
        if (!methodPrototype.getName().equals(MiscConstants.GET_CLASS_NAME)) return false;
        if (methodPrototype.getArgs().size() != 0) return false;
        if (!methodPrototype.getReturnType().getDeGenerifiedType().getRawName().equals(TypeConstants.className))
            return false;
        return true;
    }

    private boolean isRequireNonNull(Op02WithProcessedDataAndRefs item) {
        ConstantPoolEntry[] cpEntries = item.getCpEntries();
        if(cpEntries == null || cpEntries.length == 0) return false;
        ConstantPoolEntry entry = cpEntries[0];
        if (!(entry instanceof ConstantPoolEntryMethodRef)) return false;
        ConstantPoolEntryMethodRef function = (ConstantPoolEntryMethodRef)entry;
        ConstantPoolEntryClass classEntry = function.getClassEntry();
        String className = classEntry.getTypeInstance().getRawName();
        if (!className.equals(TypeConstants.objectsName)) return false;
        MethodPrototype methodPrototype = function.getMethodPrototype();
        if (!methodPrototype.getName().equals(MiscConstants.REQUIRE_NON_NULL)) return false;
        if (methodPrototype.getArgs().size() != 1) return false;
        if (!methodPrototype.getReturnType().getDeGenerifiedType().equals(TypeConstants.OBJECT)) {
            return false;
        }
        return true;
    }

    public static void removeInvokeGetClass(ClassFile classFile, List<Op02WithProcessedDataAndRefs> op02list, GetClassTest classTest) {
        JVMInstr testInstr = classTest.getInstr();
        for (Op02WithProcessedDataAndRefs item : op02list) {
            if (item.getInstr() == testInstr) {
                INSTANCE.tryRemove(classFile, item, classTest);
            }
        }
    }

}

package com.decompiler.bytecode.analysis.opgraph.op2rewriters;

import com.decompiler.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs;
import com.decompiler.bytecode.analysis.types.InnerClassInfo;
import com.decompiler.bytecode.analysis.types.JavaTypeInstance;
import com.decompiler.bytecode.opcode.JVMInstr;
import com.decompiler.entities.ClassFile;
import com.decompiler.entities.constantpool.ConstantPoolEntryMethodRef;
import com.decompiler.util.MiscConstants;

public class GetClassTestInnerConstructor implements GetClassTest {

    public static GetClassTest INSTANCE = new GetClassTestInnerConstructor();

    private GetClassTestInnerConstructor() {
    }

    @Override
    public JVMInstr getInstr() {
        return JVMInstr.INVOKESPECIAL;
    }

    @Override
    public boolean test(ClassFile classFile, Op02WithProcessedDataAndRefs item) {
        ConstantPoolEntryMethodRef function = (ConstantPoolEntryMethodRef) item.getCpEntries()[0];
        if (!function.getName().equals(MiscConstants.INIT_METHOD)) return false;
        JavaTypeInstance initType = function.getClassEntry().getTypeInstance();
        InnerClassInfo innerClassInfo = initType.getInnerClassHereInfo();
        if (!innerClassInfo.isInnerClass()) return false;
        return true;
    }
}

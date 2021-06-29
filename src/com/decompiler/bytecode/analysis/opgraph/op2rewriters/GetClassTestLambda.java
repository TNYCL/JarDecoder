package com.decompiler.bytecode.analysis.opgraph.op2rewriters;

import com.decompiler.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs;
import com.decompiler.bytecode.analysis.types.DynamicInvokeType;
import com.decompiler.bytecode.opcode.JVMInstr;
import com.decompiler.entities.ClassFile;
import com.decompiler.entities.bootstrap.BootstrapMethodInfo;
import com.decompiler.entities.constantpool.ConstantPoolEntry;
import com.decompiler.entities.constantpool.ConstantPoolEntryInvokeDynamic;
import com.decompiler.entities.constantpool.ConstantPoolEntryMethodRef;

public class GetClassTestLambda implements GetClassTest {

    public static GetClassTest INSTANCE = new GetClassTestLambda();

    private GetClassTestLambda() {
    }

    @Override
    public JVMInstr getInstr() {
        return JVMInstr.INVOKEDYNAMIC;
    }

    @Override
    public boolean test(ClassFile classFile, Op02WithProcessedDataAndRefs item) {
        ConstantPoolEntry[] cpEntries = item.getCpEntries();
        ConstantPoolEntryInvokeDynamic invokeDynamic = (ConstantPoolEntryInvokeDynamic) cpEntries[0];

        // Should have this as a member on name and type
        int idx = invokeDynamic.getBootstrapMethodAttrIndex();

        BootstrapMethodInfo bootstrapMethodInfo = classFile.getBootstrapMethods().getBootStrapMethodInfo(idx);
        ConstantPoolEntryMethodRef methodRef = bootstrapMethodInfo.getConstantPoolEntryMethodRef();
        String methodName = methodRef.getName();

        DynamicInvokeType dynamicInvokeType = DynamicInvokeType.lookup(methodName);
        if (dynamicInvokeType == DynamicInvokeType.UNKNOWN) return false;

        return true;
    }
}

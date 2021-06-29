package com.decompiler.bytecode.analysis.opgraph.op2rewriters;

import com.decompiler.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs;
import com.decompiler.bytecode.opcode.JVMInstr;
import com.decompiler.entities.ClassFile;

public interface GetClassTest {
    JVMInstr getInstr();

    boolean test(ClassFile classFile, Op02WithProcessedDataAndRefs item);
}

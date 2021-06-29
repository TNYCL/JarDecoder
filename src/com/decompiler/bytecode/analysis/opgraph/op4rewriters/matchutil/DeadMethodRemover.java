package com.decompiler.bytecode.analysis.opgraph.op4rewriters.matchutil;

import com.decompiler.bytecode.analysis.opgraph.Op04StructuredStatement;
import com.decompiler.bytecode.analysis.structured.StructuredStatement;
import com.decompiler.bytecode.analysis.structured.statement.Block;
import com.decompiler.bytecode.analysis.structured.statement.StructuredComment;
import com.decompiler.entities.ClassFile;
import com.decompiler.entities.Method;

public class DeadMethodRemover {

    public static void removeDeadMethod(ClassFile classFile, Method method) {
        Op04StructuredStatement code = method.getAnalysis();
        StructuredStatement statement = code.getStatement();
        if (!(statement instanceof Block)) return;

        Block block = (Block) statement;
        for (Op04StructuredStatement inner : block.getBlockStatements()) {
            StructuredStatement innerStatement = inner.getStatement();
            if (!(innerStatement instanceof StructuredComment)) {
                return;
            }
        }
        method.hideDead();
    }

}

package com.decompiler.bytecode.analysis.parse.expression.rewriteinterface;

import com.decompiler.bytecode.analysis.opgraph.op4rewriters.VarArgsRewriter;

public interface FunctionProcessor {
    // This feels like it should be refactored into a generalised visitor interface
    void rewriteVarArgs(VarArgsRewriter varArgsRewriter);
}

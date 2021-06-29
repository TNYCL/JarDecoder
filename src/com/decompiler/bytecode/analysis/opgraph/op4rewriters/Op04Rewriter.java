package com.decompiler.bytecode.analysis.opgraph.op4rewriters;

import com.decompiler.bytecode.analysis.opgraph.Op04StructuredStatement;

public interface Op04Rewriter {
    void rewrite(Op04StructuredStatement root);
}

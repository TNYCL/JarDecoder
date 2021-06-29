package com.decompiler.bytecode.analysis.opgraph.op4rewriters.checker;

import com.decompiler.bytecode.analysis.opgraph.op4rewriters.transformers.StructuredStatementTransformer;
import com.decompiler.util.DecompilerComments;

public interface Op04Checker extends StructuredStatementTransformer {
    void commentInto(DecompilerComments comments);
}

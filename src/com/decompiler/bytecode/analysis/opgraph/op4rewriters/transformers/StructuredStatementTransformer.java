package com.decompiler.bytecode.analysis.opgraph.op4rewriters.transformers;

import com.decompiler.bytecode.analysis.structured.StructuredScope;
import com.decompiler.bytecode.analysis.structured.StructuredStatement;

public interface StructuredStatementTransformer {
    StructuredStatement transform(StructuredStatement in, StructuredScope scope);
}

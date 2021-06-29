package com.decompiler.bytecode.analysis.opgraph.op4rewriters.transformers;

import com.decompiler.bytecode.analysis.structured.StructuredScope;

public interface CanRemovePointlessBlock {
    void removePointlessBlocks(StructuredScope scope);
}

package com.decompiler.bytecode.analysis.structured.statement;

import com.decompiler.bytecode.analysis.loc.BytecodeLoc;
import com.decompiler.bytecode.analysis.parse.utils.BlockIdentifier;

public abstract class AbstractStructuredContinue extends AbstractStructuredStatement {
    public AbstractStructuredContinue(BytecodeLoc loc) {
        super(loc);
    }

    public abstract BlockIdentifier getContinueTgt();
}

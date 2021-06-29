package com.decompiler.bytecode.analysis.parse.statement;

import com.decompiler.bytecode.analysis.loc.BytecodeLoc;
import com.decompiler.bytecode.analysis.parse.rewriters.CloneHelper;
import com.decompiler.bytecode.analysis.parse.rewriters.DeepCloneable;

public abstract class ReturnStatement extends AbstractStatement {

    public ReturnStatement(BytecodeLoc loc) {
        super(loc);
    }

    @Override
    public boolean fallsToNext() {
        return false;
    }

    @Override
    public ReturnStatement outerDeepClone(CloneHelper cloneHelper) {
        throw new UnsupportedOperationException();
    }
}

package com.decompiler.bytecode.analysis.parse.statement;

import com.decompiler.bytecode.analysis.loc.BytecodeLoc;
import com.decompiler.bytecode.analysis.parse.Statement;
import com.decompiler.bytecode.analysis.parse.utils.JumpType;

public abstract class JumpingStatement extends AbstractStatement {
    public JumpingStatement(BytecodeLoc loc) {
        super(loc);
    }

    public abstract Statement getJumpTarget();

    public abstract JumpType getJumpType();

    public abstract void setJumpType(JumpType jumpType);

    public abstract boolean isConditional();
}

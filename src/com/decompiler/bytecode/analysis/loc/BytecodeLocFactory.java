package com.decompiler.bytecode.analysis.loc;

import com.decompiler.entities.Method;

public interface BytecodeLocFactory {
    BytecodeLoc DISABLED = new BytecodeLocSpecific(BytecodeLocSpecific.Specific.DISABLED);
    BytecodeLoc NONE = new BytecodeLocSpecific(BytecodeLocSpecific.Specific.NONE);
    BytecodeLoc TODO = new BytecodeLocSpecific(BytecodeLocSpecific.Specific.TODO);

    BytecodeLoc at(int originalRawOffset, Method method);
}

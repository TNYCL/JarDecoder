package com.decompiler.bytecode.analysis.loc;

public interface HasByteCodeLoc {
    BytecodeLoc getCombinedLoc();

    BytecodeLoc getLoc();

    void addLoc(HasByteCodeLoc loc);
}

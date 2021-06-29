package com.decompiler.bytecode.analysis.parse.utils;

public class BlockIdentifierFactory {
    int idx = 0;

    public BlockIdentifier getNextBlockIdentifier(BlockType blockType) {
        return new BlockIdentifier(idx++, blockType);
    }
}

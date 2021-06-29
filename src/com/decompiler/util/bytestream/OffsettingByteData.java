package com.decompiler.util.bytestream;

public interface OffsettingByteData extends ByteData {
    void advance(long offset);
    long getOffset();
}

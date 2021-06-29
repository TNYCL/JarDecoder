package com.decompiler.entities.attributes;

import com.decompiler.util.bytestream.ByteData;
import com.decompiler.util.output.Dumper;

public class AttributeSourceFile extends Attribute {
    public static final String ATTRIBUTE_NAME = "SourceFile";

    private static final long OFFSET_OF_ATTRIBUTE_LENGTH = 2;
    private static final long OFFSET_OF_REMAINDER = 6;

    private final int length;

    public AttributeSourceFile(ByteData raw) {
        this.length = raw.getS4At(OFFSET_OF_ATTRIBUTE_LENGTH);
    }

    @Override
    public String getRawName() {
        return ATTRIBUTE_NAME;
    }

    @Override
    public Dumper dump(Dumper d) {
        return d.print(ATTRIBUTE_NAME);
    }

    @Override
    public long getRawByteLength() {
        return OFFSET_OF_REMAINDER + length;
    }

    @Override
    public String toString() {
        return ATTRIBUTE_NAME;
    }
}

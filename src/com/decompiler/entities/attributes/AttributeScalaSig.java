package com.decompiler.entities.attributes;

import com.decompiler.util.bytestream.ByteData;
import com.decompiler.util.output.Dumper;

public class AttributeScalaSig extends Attribute {
    public final static String ATTRIBUTE_NAME = "ScalaSig";

    private static final long OFFSET_OF_ATTRIBUTE_LENGTH = 2;
    private static final long OFFSET_OF_REMAINDER = 6;

    private final int length;

    public AttributeScalaSig(ByteData raw) {
        this.length = raw.getS4At(OFFSET_OF_ATTRIBUTE_LENGTH);
    }

    @Override
    public String getRawName() {
        return ATTRIBUTE_NAME;
    }

    @Override
    public Dumper dump(Dumper d) {
        return d;
    }

    @Override
    public long getRawByteLength() {
        return OFFSET_OF_REMAINDER + length;
    }
}

package com.decompiler.entities.attributes;

import com.decompiler.util.bytestream.ByteData;
import com.decompiler.util.output.Dumper;

public class AttributeEnclosingMethod extends Attribute {
    public static final String ATTRIBUTE_NAME = "EnclosingMethod";

    private static final long OFFSET_OF_ATTRIBUTE_LENGTH = 2;
    private static final long OFFSET_OF_REMAINDER = 6;

    private final int length;

    private final int classIndex;
    private final int methodIndex;

    public AttributeEnclosingMethod(ByteData raw) {
        this.length = raw.getS4At(OFFSET_OF_ATTRIBUTE_LENGTH);
        this.classIndex = raw.getU2At(OFFSET_OF_REMAINDER);
        this.methodIndex = raw.getU2At(OFFSET_OF_REMAINDER + 2);
    }

    @Override
    public String getRawName() {
        return ATTRIBUTE_NAME;
    }

    @Override
    public Dumper dump(Dumper d) {
        return d.print("EnclosingMethod");
    }

    @Override
    public long getRawByteLength() {
        return OFFSET_OF_REMAINDER + length;
    }

    @Override
    public String toString() {
        return "EnclosingMethod";
    }

    public int getClassIndex() {
        return classIndex;
    }

    public int getMethodIndex() {
        return methodIndex;
    }
}

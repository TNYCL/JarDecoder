package com.decompiler.entities.attributes;

import com.decompiler.entities.constantpool.ConstantPool;
import com.decompiler.util.bytestream.ByteData;

public class AttributeRuntimeVisibleTypeAnnotations extends AttributeTypeAnnotations {
    public static final String ATTRIBUTE_NAME = "RuntimeVisibleTypeAnnotations";

    public AttributeRuntimeVisibleTypeAnnotations(ByteData raw, ConstantPool cp) {
        super(raw, cp);
    }

    @Override
    public String getRawName() {
        return ATTRIBUTE_NAME;
    }

    @Override
    public String toString() {
        return ATTRIBUTE_NAME;
    }

}

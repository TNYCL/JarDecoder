package com.decompiler.entities.attributes;

import com.decompiler.entities.constantpool.ConstantPool;
import com.decompiler.util.bytestream.ByteData;

public class AttributeRuntimeVisibleParameterAnnotations extends AttributeParameterAnnotations {
    public static final String ATTRIBUTE_NAME = "RuntimeVisibleParameterAnnotations";

    public AttributeRuntimeVisibleParameterAnnotations(ByteData raw, ConstantPool cp) {
        super(raw, cp);
    }

    @Override
    public String getRawName() {
        return ATTRIBUTE_NAME;
    }
}

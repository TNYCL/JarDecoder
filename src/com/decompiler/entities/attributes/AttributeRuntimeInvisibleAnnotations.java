package com.decompiler.entities.attributes;

import com.decompiler.entities.constantpool.ConstantPool;
import com.decompiler.util.bytestream.ByteData;

public class AttributeRuntimeInvisibleAnnotations extends AttributeAnnotations {
    public static final String ATTRIBUTE_NAME = "RuntimeInvisibleAnnotations";

    public AttributeRuntimeInvisibleAnnotations(ByteData raw, ConstantPool cp) {
        super(raw, cp);
    }

    @Override
    public String getRawName() {
        return ATTRIBUTE_NAME;
    }
}

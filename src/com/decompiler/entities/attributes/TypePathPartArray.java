package com.decompiler.entities.attributes;

import com.decompiler.bytecode.analysis.types.JavaAnnotatedTypeIterator;
import com.decompiler.util.DecompilerComments;

public class TypePathPartArray implements TypePathPart {
    public static final TypePathPartArray INSTANCE = new TypePathPartArray();

    private TypePathPartArray() {
    }

    @Override
    public JavaAnnotatedTypeIterator apply(JavaAnnotatedTypeIterator it, DecompilerComments comments) {
        return it.moveArray(comments);
    }
}

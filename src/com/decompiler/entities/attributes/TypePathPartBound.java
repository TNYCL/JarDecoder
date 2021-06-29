package com.decompiler.entities.attributes;

import com.decompiler.bytecode.analysis.types.JavaAnnotatedTypeIterator;
import com.decompiler.util.DecompilerComments;

public class TypePathPartBound implements TypePathPart {
    public static final TypePathPartBound INSTANCE = new TypePathPartBound();

    private TypePathPartBound() {
    }

    @Override
    public JavaAnnotatedTypeIterator apply(JavaAnnotatedTypeIterator it, DecompilerComments comments) {
        return it.moveBound(comments);
    }
}

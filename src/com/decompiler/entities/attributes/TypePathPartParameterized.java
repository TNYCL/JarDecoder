package com.decompiler.entities.attributes;

import com.decompiler.bytecode.analysis.types.JavaAnnotatedTypeIterator;
import com.decompiler.util.DecompilerComments;

public class TypePathPartParameterized implements TypePathPart {
    private final int index;

    public TypePathPartParameterized(int index) {
        this.index = index;
    }

    @Override
    public JavaAnnotatedTypeIterator apply(JavaAnnotatedTypeIterator it, DecompilerComments comments) {
        return it.moveParameterized(index, comments);
    }
}

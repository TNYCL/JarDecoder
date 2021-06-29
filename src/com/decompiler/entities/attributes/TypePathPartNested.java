package com.decompiler.entities.attributes;

import com.decompiler.bytecode.analysis.types.JavaAnnotatedTypeIterator;
import com.decompiler.util.DecompilerComments;

public class TypePathPartNested implements TypePathPart {
    public static final TypePathPartNested INSTANCE = new TypePathPartNested();

    private TypePathPartNested() {
    }

    @Override
    public JavaAnnotatedTypeIterator apply(JavaAnnotatedTypeIterator it, DecompilerComments comments) {
        return it.moveNested(comments);
    }
}

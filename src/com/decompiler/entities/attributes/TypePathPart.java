package com.decompiler.entities.attributes;

import com.decompiler.bytecode.analysis.types.JavaAnnotatedTypeIterator;
import com.decompiler.util.DecompilerComments;

public interface TypePathPart {
    JavaAnnotatedTypeIterator apply(JavaAnnotatedTypeIterator it, DecompilerComments comments);
}

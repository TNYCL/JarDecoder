package com.decompiler.bytecode.analysis.types.annotated;

import com.decompiler.bytecode.analysis.types.JavaAnnotatedTypeIterator;
import com.decompiler.util.output.Dumpable;

public interface JavaAnnotatedTypeInstance extends Dumpable {
    JavaAnnotatedTypeIterator pathIterator();
}

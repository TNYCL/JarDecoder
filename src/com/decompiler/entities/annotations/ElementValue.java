package com.decompiler.entities.annotations;

import com.decompiler.bytecode.analysis.types.JavaTypeInstance;
import com.decompiler.util.TypeUsageCollectable;
import com.decompiler.util.output.Dumpable;

public interface ElementValue extends Dumpable, TypeUsageCollectable {
    ElementValue withTypeHint(JavaTypeInstance hint);
}

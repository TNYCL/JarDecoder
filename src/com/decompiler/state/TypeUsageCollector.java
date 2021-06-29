package com.decompiler.state;

import java.util.Collection;

import com.decompiler.bytecode.analysis.types.JavaRefTypeInstance;
import com.decompiler.bytecode.analysis.types.JavaTypeInstance;
import com.decompiler.util.TypeUsageCollectable;

public interface TypeUsageCollector {
    void collectRefType(JavaRefTypeInstance type);

    void collect(JavaTypeInstance type);

    void collect(Collection<? extends JavaTypeInstance> types);

    // Explicitly named helper to allow J8's less lenient bridging.
    void collectFromT(TypeUsageCollectable collectable);

    void collectFrom(TypeUsageCollectable collectable);

    void collectFrom(Collection<? extends TypeUsageCollectable> collectables);

    TypeUsageInformation getTypeUsageInformation();

    boolean isStatementRecursive();
}

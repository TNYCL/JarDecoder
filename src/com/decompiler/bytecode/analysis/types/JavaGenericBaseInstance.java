package com.decompiler.bytecode.analysis.types;

import java.util.List;
import java.util.Map;

import com.decompiler.entities.constantpool.ConstantPool;

public interface JavaGenericBaseInstance extends JavaTypeInstance {
    JavaTypeInstance getBoundInstance(GenericTypeBinder genericTypeBinder);

    boolean tryFindBinding(JavaTypeInstance other, GenericTypeBinder target);

    boolean hasUnbound();

    boolean hasL01Wildcard();

    JavaTypeInstance getWithoutL01Wildcard();

    boolean hasForeignUnbound(ConstantPool cp, int depth, boolean noWildcard, Map<String, FormalTypeParameter> externals);

    List<JavaTypeInstance> getGenericTypes();
}

package com.decompiler.state;

import java.util.List;

import com.decompiler.bytecode.analysis.types.JavaTypeInstance;
import com.decompiler.entities.innerclass.InnerClassAttributeInfo;
import com.decompiler.util.functors.UnaryFunction;

public interface ObfuscationTypeMap {
    boolean providesInnerClassInfo();

    JavaTypeInstance get(JavaTypeInstance type);

    UnaryFunction<JavaTypeInstance, JavaTypeInstance> getter();

    List<InnerClassAttributeInfo> getInnerClassInfo(JavaTypeInstance classType);
}

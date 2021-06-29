package com.decompiler.state;

import java.util.List;

import com.decompiler.bytecode.analysis.types.JavaTypeInstance;
import com.decompiler.util.output.Dumper;

public interface ObfuscationRewriter {
    Dumper wrap(Dumper d);

    JavaTypeInstance get(JavaTypeInstance t);

    List<JavaTypeInstance> get(List<JavaTypeInstance> types);
}

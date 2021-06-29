package com.decompiler.mapping;

import java.util.List;

import com.decompiler.bytecode.analysis.types.JavaTypeInstance;

public class MethodMapping {
    private final String name;
    private final String rename;
    private final JavaTypeInstance res;
    private final List<JavaTypeInstance> argTypes;

    public MethodMapping(String rename, String name, JavaTypeInstance res, List<JavaTypeInstance> argTypes) {
        this.name = name;
        this.rename = rename;
        this.res = res;
        this.argTypes = argTypes;
    }

    public String getName() {
        return name;
    }

    public String getRename() {
        return rename;
    }

    public JavaTypeInstance getResultType() {
        return res;
    }

    public List<JavaTypeInstance> getArgTypes() {
        return argTypes;
    }
}

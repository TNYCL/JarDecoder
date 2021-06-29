package com.decompiler.entities.innerclass;

import java.util.Set;

import com.decompiler.bytecode.analysis.types.JavaTypeInstance;
import com.decompiler.entities.AccessFlag;
import com.decompiler.util.annotation.Nullable;

public class InnerClassAttributeInfo {
    private final
    @Nullable
    JavaTypeInstance innerClassInfo;
    private final
    @Nullable
    JavaTypeInstance outerClassInfo;
    private final
    @Nullable
    String innerName;
    private final Set<AccessFlag> accessFlags;

    public InnerClassAttributeInfo(JavaTypeInstance innerClassInfo, JavaTypeInstance outerClassInfo, String innerName, Set<AccessFlag> accessFlags) {
        this.innerClassInfo = innerClassInfo;
        this.outerClassInfo = outerClassInfo;
        this.innerName = innerName;
        this.accessFlags = accessFlags;
    }

    public JavaTypeInstance getInnerClassInfo() {
        return innerClassInfo;
    }

    private JavaTypeInstance getOuterClassInfo() {
        return outerClassInfo;
    }

    private String getInnerName() {
        return innerName;
    }

    public Set<AccessFlag> getAccessFlags() {
        return accessFlags;
    }
}

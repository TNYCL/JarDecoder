package com.decompiler.entities.annotations;

import java.util.Map;

import com.decompiler.bytecode.analysis.types.JavaTypeInstance;
import com.decompiler.entities.attributes.TypeAnnotationEntryKind;
import com.decompiler.entities.attributes.TypeAnnotationEntryValue;
import com.decompiler.entities.attributes.TypeAnnotationTargetInfo;
import com.decompiler.entities.attributes.TypePath;

public class AnnotationTableTypeEntry<T extends TypeAnnotationTargetInfo> extends AnnotationTableEntry {
    private final TypeAnnotationEntryValue value;
    private final T targetInfo;
    private final TypePath typePath;

    public AnnotationTableTypeEntry(TypeAnnotationEntryValue value, T targetInfo, TypePath typePath, JavaTypeInstance type, Map<String, ElementValue> elementValueMap) {
        super(type, elementValueMap);
        this.value = value;
        this.targetInfo = targetInfo;
        this.typePath = typePath;
    }

    public TypePath getTypePath() {
        return typePath;
    }

    public TypeAnnotationEntryValue getValue() { return value; }

    public TypeAnnotationEntryKind getKind() {
        return value.getKind();
    }

    public T getTargetInfo() {
        return targetInfo;
    }
}
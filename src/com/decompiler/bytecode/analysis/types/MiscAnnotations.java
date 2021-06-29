package com.decompiler.bytecode.analysis.types;

import java.util.List;

import com.decompiler.entities.annotations.AnnotationTableEntry;
import com.decompiler.entities.attributes.AttributeMap;
import com.decompiler.entities.attributes.AttributeRuntimeInvisibleAnnotations;
import com.decompiler.entities.attributes.AttributeRuntimeVisibleAnnotations;
import com.decompiler.util.collections.ListFactory;

public class MiscAnnotations {
    public static List<AnnotationTableEntry> BasicAnnotations(AttributeMap attributeMap) {
        AttributeRuntimeVisibleAnnotations a1 = attributeMap.getByName(AttributeRuntimeVisibleAnnotations.ATTRIBUTE_NAME);
        AttributeRuntimeInvisibleAnnotations a2 = attributeMap.getByName(AttributeRuntimeInvisibleAnnotations.ATTRIBUTE_NAME);
        List<AnnotationTableEntry> e1 = a1 == null ? null : a1.getEntryList();
        List<AnnotationTableEntry> e2 = a2 == null ? null : a2.getEntryList();
        return ListFactory.combinedOptimistic(e1,e2);
    }

}

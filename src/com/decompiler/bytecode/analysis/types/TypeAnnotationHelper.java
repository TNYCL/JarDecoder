package com.decompiler.bytecode.analysis.types;

import java.util.List;
import java.util.Set;

import com.decompiler.bytecode.analysis.types.annotated.JavaAnnotatedTypeInstance;
import com.decompiler.entities.annotations.AnnotationTableTypeEntry;
import com.decompiler.entities.attributes.AttributeMap;
import com.decompiler.entities.attributes.AttributeRuntimeInvisibleTypeAnnotations;
import com.decompiler.entities.attributes.AttributeRuntimeVisibleTypeAnnotations;
import com.decompiler.entities.attributes.AttributeTypeAnnotations;
import com.decompiler.entities.attributes.TypeAnnotationEntryValue;
import com.decompiler.entities.attributes.TypePathPart;
import com.decompiler.util.DecompilerComments;
import com.decompiler.util.collections.ListFactory;

public class TypeAnnotationHelper {
    private final List<AnnotationTableTypeEntry> entries;

    private TypeAnnotationHelper(List<AnnotationTableTypeEntry> entries) {
        this.entries = entries;
    }

    public static TypeAnnotationHelper create(AttributeMap map, TypeAnnotationEntryValue ... tkeys) {
        String[] keys = new String[] {
            AttributeRuntimeVisibleTypeAnnotations.ATTRIBUTE_NAME,
            AttributeRuntimeInvisibleTypeAnnotations.ATTRIBUTE_NAME
        };
        List<AnnotationTableTypeEntry> res = ListFactory.newList();
        for (String key : keys) {
            AttributeTypeAnnotations ann = map.getByName(key);
            if (ann == null) continue;
            List<AnnotationTableTypeEntry> tmp = ann.getAnnotationsFor(tkeys);
            if (tmp != null) {
                res.addAll(tmp);
            }
        }
        if (!res.isEmpty()) return new TypeAnnotationHelper(res);
        return null;
    }
    
    public static void apply(JavaAnnotatedTypeInstance annotatedTypeInstance, List<? extends AnnotationTableTypeEntry> typeEntries, DecompilerComments comments) {
        if (typeEntries != null) {
            for (AnnotationTableTypeEntry typeEntry : typeEntries) {
                apply(annotatedTypeInstance, typeEntry, comments);
            }
        }
    }
    
    private static void apply(JavaAnnotatedTypeInstance annotatedTypeInstance, AnnotationTableTypeEntry typeEntry, DecompilerComments comments) {
        JavaAnnotatedTypeIterator iterator = annotatedTypeInstance.pathIterator();
        List<TypePathPart> segments = typeEntry.getTypePath().segments;
        for (TypePathPart part : segments) {
            iterator = part.apply(iterator, comments);
        }
        iterator.apply(typeEntry);
    }

    // TODO : Find usages of this, ensure linear scans are small.
    public List<AnnotationTableTypeEntry> getEntries() {
        return entries;
    }
}

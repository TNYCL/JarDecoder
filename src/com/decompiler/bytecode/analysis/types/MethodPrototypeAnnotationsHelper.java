package com.decompiler.bytecode.analysis.types;

import java.util.List;

import com.decompiler.bytecode.analysis.types.DeclarationAnnotationHelper.DeclarationAnnotationsInfo;
import com.decompiler.bytecode.analysis.types.annotated.JavaAnnotatedTypeInstance;
import com.decompiler.entities.annotations.AnnotationTableEntry;
import com.decompiler.entities.annotations.AnnotationTableTypeEntry;
import com.decompiler.entities.attributes.AttributeMap;
import com.decompiler.entities.attributes.AttributeRuntimeInvisibleParameterAnnotations;
import com.decompiler.entities.attributes.AttributeRuntimeVisibleParameterAnnotations;
import com.decompiler.entities.attributes.TypeAnnotationEntryValue;
import com.decompiler.entities.attributes.TypeAnnotationTargetInfo;
import com.decompiler.util.DecompilerComments;
import com.decompiler.util.collections.Functional;
import com.decompiler.util.collections.ListFactory;
import com.decompiler.util.functors.Predicate;
import com.decompiler.util.output.Dumper;

public class MethodPrototypeAnnotationsHelper {
    private final AttributeMap attributeMap;
    private final TypeAnnotationHelper typeAnnotationHelper;

    public MethodPrototypeAnnotationsHelper(AttributeMap attributes) {
        this.attributeMap = attributes;
        this.typeAnnotationHelper = TypeAnnotationHelper.create(attributes,
                TypeAnnotationEntryValue.type_generic_method_constructor,
                TypeAnnotationEntryValue.type_ret_or_new,
                TypeAnnotationEntryValue.type_receiver,
                TypeAnnotationEntryValue.type_throws,
                TypeAnnotationEntryValue.type_formal
                );
    }

    static void dumpAnnotationTableEntries(List<? extends AnnotationTableEntry> annotationTableEntries, Dumper d) {
        for (AnnotationTableEntry annotation : annotationTableEntries) {
            annotation.dump(d).print(' ');
        }
    }

    public List<AnnotationTableTypeEntry> getMethodReturnAnnotations() {
        return getTypeTargetAnnotations(TypeAnnotationEntryValue.type_ret_or_new);
    }

    // TODO: Linear scans here, could be replaced with index.
    public List<AnnotationTableTypeEntry> getTypeTargetAnnotations(final TypeAnnotationEntryValue target) {
        if (typeAnnotationHelper == null) return null;
        List<AnnotationTableTypeEntry> res = Functional.filter(typeAnnotationHelper.getEntries(), new Predicate<AnnotationTableTypeEntry>() {
            @Override
            public boolean test(AnnotationTableTypeEntry in) {
                return in.getValue() == target;
            }
        });
        if (res.isEmpty()) return null;
        return res;
    }

    public List<AnnotationTableEntry> getMethodAnnotations() {
        return MiscAnnotations.BasicAnnotations(attributeMap);
    }

    private List<AnnotationTableEntry> getParameterAnnotations(int idx) {
        AttributeRuntimeVisibleParameterAnnotations a1 = attributeMap.getByName(AttributeRuntimeVisibleParameterAnnotations.ATTRIBUTE_NAME);
        AttributeRuntimeInvisibleParameterAnnotations a2 = attributeMap.getByName(AttributeRuntimeInvisibleParameterAnnotations.ATTRIBUTE_NAME);
        List<AnnotationTableEntry> e1 = a1 == null ? null : a1.getAnnotationsForParamIdx(idx);
        List<AnnotationTableEntry> e2 = a2 == null ? null : a2.getAnnotationsForParamIdx(idx);
        return ListFactory.combinedOptimistic(e1,e2);
    }

    private List<AnnotationTableTypeEntry> getTypeParameterAnnotations(final int paramIdx) {
        List<AnnotationTableTypeEntry> typeEntries = getTypeTargetAnnotations(TypeAnnotationEntryValue.type_formal);
        if (typeEntries == null) return null;
        typeEntries = Functional.filter(typeEntries, new Predicate<AnnotationTableTypeEntry>() {
            @Override
            public boolean test(AnnotationTableTypeEntry in) {
                return ((TypeAnnotationTargetInfo.TypeAnnotationFormalParameterTarget)in.getTargetInfo()).getIndex() == paramIdx;
            }
        });
        if (typeEntries.isEmpty()) return null;
        return typeEntries;
    }

    void dumpParamType(JavaTypeInstance arg, final int paramIdx, Dumper d) {
        List<AnnotationTableEntry> entries = getParameterAnnotations(paramIdx);
        List<AnnotationTableTypeEntry> typeEntries = getTypeParameterAnnotations(paramIdx);
        DeclarationAnnotationsInfo annotationsInfo = DeclarationAnnotationHelper.getDeclarationInfo(arg, entries, typeEntries);
        /*
         * TODO: This is incorrect, but currently cannot easily influence whether the dumped type is admissible
         * Therefore assume it is always admissible unless required not to
         * (even though then the dumped type might still be admissible)
         */
        boolean usesAdmissibleType = !annotationsInfo.requiresNonAdmissibleType();
        List<AnnotationTableEntry> declAnnotationsToDump = annotationsInfo.getDeclarationAnnotations(usesAdmissibleType);
        List<AnnotationTableTypeEntry> typeAnnotationsToDump = annotationsInfo.getTypeAnnotations(usesAdmissibleType);

        dumpAnnotationTableEntries(declAnnotationsToDump, d);

        if (typeAnnotationsToDump.isEmpty()) {
            d.dump(arg);
        } else {
            JavaAnnotatedTypeInstance jat = arg.getAnnotatedInstance();
            DecompilerComments comments = new DecompilerComments();
            TypeAnnotationHelper.apply(jat, typeAnnotationsToDump, comments);
            d.dump(comments);
            d.dump(jat);
        }
    }
}

package com.decompiler.entities.attributes;

import java.util.List;
import java.util.Map;

import com.decompiler.bytecode.analysis.parse.utils.Pair;
import com.decompiler.entities.annotations.AnnotationTableTypeEntry;
import com.decompiler.entities.constantpool.ConstantPool;
import com.decompiler.state.TypeUsageCollector;
import com.decompiler.util.bytestream.ByteData;
import com.decompiler.util.collections.ListFactory;
import com.decompiler.util.collections.MapFactory;
import com.decompiler.util.functors.UnaryFunction;
import com.decompiler.util.output.Dumper;

public abstract class AttributeTypeAnnotations extends Attribute {

    private static final long OFFSET_OF_ATTRIBUTE_LENGTH = 2;
    private static final long OFFSET_OF_REMAINDER = 6;
    private static final long OFFSET_OF_NUMBER_OF_ANNOTATIONS = 6;
    private static final long OFFSET_OF_ANNOTATION_TABLE = 8;
    private Map<TypeAnnotationEntryValue, List<AnnotationTableTypeEntry>> annotationTableEntryData = MapFactory.newMap();

    private final int length;


    AttributeTypeAnnotations(ByteData raw, ConstantPool cp) {
        this.length = raw.getS4At(OFFSET_OF_ATTRIBUTE_LENGTH);
        int numAnnotations = raw.getU2At(OFFSET_OF_NUMBER_OF_ANNOTATIONS);
        long offset = OFFSET_OF_ANNOTATION_TABLE;

        Map<TypeAnnotationEntryValue, List<AnnotationTableTypeEntry>> entryData = MapFactory.newLazyMap(annotationTableEntryData, new UnaryFunction<TypeAnnotationEntryValue, List<AnnotationTableTypeEntry>>() {
            @Override
            public List<AnnotationTableTypeEntry> invoke(TypeAnnotationEntryValue arg) {
                return ListFactory.newList();
            }
        });

        for (int x = 0; x < numAnnotations; ++x) {
            Pair<Long, AnnotationTableTypeEntry> ape = AnnotationHelpers.getTypeAnnotation(raw, offset, cp);
            offset = ape.getFirst();
            AnnotationTableTypeEntry entry = ape.getSecond();
            entryData.get(entry.getValue()).add(entry);
        }
    }

    @Override
    public Dumper dump(Dumper d) {
        for (List<AnnotationTableTypeEntry> annotationTableEntryList : annotationTableEntryData.values()) {
            for (AnnotationTableTypeEntry annotationTableEntry : annotationTableEntryList) {
                annotationTableEntry.dump(d);
                d.newln();
            }
        }
        return d;
    }


    @Override
    public long getRawByteLength() {
        return OFFSET_OF_REMAINDER + length;
    }

    @Override
    public void collectTypeUsages(TypeUsageCollector collector) {
        for (List<AnnotationTableTypeEntry> annotationTableEntryList : annotationTableEntryData.values()) {
            for (AnnotationTableTypeEntry annotationTableEntry : annotationTableEntryList) {
                annotationTableEntry.collectTypeUsages(collector);
            }
        }
    }

    public List<AnnotationTableTypeEntry> getAnnotationsFor(TypeAnnotationEntryValue ... types) {
        List<AnnotationTableTypeEntry> res = null;
        boolean orig = true;
        for (TypeAnnotationEntryValue type : types) {
            List<AnnotationTableTypeEntry> items = annotationTableEntryData.get(type);
            if (items == null) {
                continue;
            }
            if (orig) {
                res = items;
                orig = false;
            } else {
                res = ListFactory.newList(res);
                res.addAll(items);
            }
        }
        return res;
    }
}

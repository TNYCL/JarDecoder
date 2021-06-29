package com.decompiler.entities.attributes;

import java.util.List;

import com.decompiler.bytecode.analysis.parse.utils.Pair;
import com.decompiler.bytecode.analysis.types.JavaTypeInstance;
import com.decompiler.entities.annotations.*;
import com.decompiler.entities.constantpool.ConstantPool;
import com.decompiler.state.TypeUsageCollector;
import com.decompiler.util.TypeUsageCollectable;
import com.decompiler.util.bytestream.ByteData;
import com.decompiler.util.collections.Functional;
import com.decompiler.util.collections.ListFactory;
import com.decompiler.util.functors.Predicate;
import com.decompiler.util.output.Dumper;

public abstract class AttributeAnnotations extends Attribute implements TypeUsageCollectable {

    private static final long OFFSET_OF_ATTRIBUTE_LENGTH = 2;
    private static final long OFFSET_OF_REMAINDER = 6;
    private static final long OFFSET_OF_NUMBER_OF_ANNOTATIONS = 6;
    private static final long OFFSET_OF_ANNOTATION_TABLE = 8;

    private final List<AnnotationTableEntry> annotationTableEntryList = ListFactory.newList();

    private final int length;

    AttributeAnnotations(ByteData raw, ConstantPool cp) {
        this.length = raw.getS4At(OFFSET_OF_ATTRIBUTE_LENGTH);
        int numAnnotations = raw.getU2At(OFFSET_OF_NUMBER_OF_ANNOTATIONS);
        long offset = OFFSET_OF_ANNOTATION_TABLE;
        for (int x = 0; x < numAnnotations; ++x) {
            Pair<Long, AnnotationTableEntry> ape = AnnotationHelpers.getAnnotation(raw, offset, cp);
            offset = ape.getFirst();
            annotationTableEntryList.add(ape.getSecond());
        }
    }

    public void hide(final JavaTypeInstance type) {
        List<AnnotationTableEntry> hideThese = Functional.filter(annotationTableEntryList, new Predicate<AnnotationTableEntry>() {
            @Override
            public boolean test(AnnotationTableEntry in) {
                return in.getClazz().equals(type);
            }
        });
        for (AnnotationTableEntry hide : hideThese) {
            hide.setHidden();
        }
    }

    @Override
    public Dumper dump(Dumper d) {
        for (AnnotationTableEntry annotationTableEntry : annotationTableEntryList) {
            if (!annotationTableEntry.isHidden()) {
                annotationTableEntry.dump(d);
                d.newln();
            }
        }
        return d;
    }

    public List<AnnotationTableEntry> getEntryList() {
        return annotationTableEntryList;
    }

    @Override
    public long getRawByteLength() {
        return OFFSET_OF_REMAINDER + length;
    }

    @Override
    public void collectTypeUsages(TypeUsageCollector collector) {
        for (AnnotationTableEntry annotationTableEntry : annotationTableEntryList) {
            annotationTableEntry.collectTypeUsages(collector);
        }
    }
}

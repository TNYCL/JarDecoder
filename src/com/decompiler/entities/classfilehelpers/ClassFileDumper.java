package com.decompiler.entities.classfilehelpers;

import com.decompiler.entities.ClassFile;
import com.decompiler.state.TypeUsageCollector;
import com.decompiler.util.TypeUsageCollectable;
import com.decompiler.util.output.Dumper;

public interface ClassFileDumper extends TypeUsageCollectable {
    enum InnerClassDumpType {
        NOT(false),
        INNER_CLASS(true),
        INLINE_CLASS(true);

        final boolean isInnerClass;

        InnerClassDumpType(boolean isInnerClass) {
            this.isInnerClass = isInnerClass;
        }

        public boolean isInnerClass() {
            return isInnerClass;
        }
    }

    Dumper dump(ClassFile classFile, InnerClassDumpType innerClass, Dumper d);

    /*
     * Some dumpers may need to request additional types -
     */
    void collectTypeUsages(TypeUsageCollector collector);

}

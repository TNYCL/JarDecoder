package com.decompiler.entities.classfilehelpers;

import java.util.List;

import com.decompiler.entities.AccessFlag;
import com.decompiler.entities.ClassFile;
import com.decompiler.entities.ClassFileField;
import com.decompiler.state.DCCommonState;
import com.decompiler.state.TypeUsageCollector;
import com.decompiler.util.StringUtils;
import com.decompiler.util.collections.Functional;
import com.decompiler.util.functors.Predicate;
import com.decompiler.util.output.Dumper;

public class ClassFileDumperRecord extends AbstractClassFileDumper {

    private static final AccessFlag[] dumpableAccessFlagsClass = new AccessFlag[]{
            AccessFlag.ACC_PUBLIC, AccessFlag.ACC_PRIVATE, AccessFlag.ACC_PROTECTED, AccessFlag.ACC_STRICT, AccessFlag.ACC_ABSTRACT
    };

    public ClassFileDumperRecord(DCCommonState dcCommonState) {
        super(dcCommonState);
    }

    private void dumpHeader(ClassFile c, InnerClassDumpType innerClassDumpType, Dumper d) {
        d.keyword(getAccessFlagsString(c.getAccessFlags(), dumpableAccessFlagsClass));

        d.keyword("record ").dump(c.getClassType()).print("(");
        List<ClassFileField> fields = Functional.filter(c.getFields(), new Predicate<ClassFileField>() {
                    @Override
                    public boolean test(ClassFileField in) {
                        return !in.getField().testAccessFlag(AccessFlag.ACC_STATIC);
                    }
                });
        boolean first = true;
        for (ClassFileField f : fields) {
            first = StringUtils.comma(first, d);
            f.dumpAsRecord(d, c);
        }
        d.print(") ");
    }

    @Override
    public Dumper dump(ClassFile classFile, InnerClassDumpType innerClass, Dumper d) {
        if (!d.canEmitClass(classFile.getClassType())) return d;

        if (!innerClass.isInnerClass()) {
            dumpTopHeader(classFile, d, true);
            dumpImports(d, classFile);
        }

        dumpComments(classFile, d);
        dumpAnnotations(classFile, d);
        dumpHeader(classFile, innerClass, d);
        d.separator("{").newln();
        d.indent(1);
        boolean first = true;

        List<ClassFileField> fields = classFile.getFields();
        for (ClassFileField field : fields) {
            if (!field.shouldNotDisplay()) {
                field.dump(d, classFile);
                first = false;
            }
        }
        dumpMethods(classFile, d, first, true);
        classFile.dumpNamedInnerClasses(d);
        d.indent(-1);
        d.separator("}").newln();

        return d;
    }

    @Override
    public void collectTypeUsages(TypeUsageCollector collector) {
    }
}

package com.decompiler.entities.classfilehelpers;

import java.util.List;

import com.decompiler.entities.*;
import com.decompiler.state.DCCommonState;
import com.decompiler.state.TypeUsageCollector;
import com.decompiler.util.output.Dumper;

public class ClassFileDumperAnnotation extends AbstractClassFileDumper {

    private static final AccessFlag[] dumpableAccessFlagsInterface = new AccessFlag[]{
            AccessFlag.ACC_PUBLIC, AccessFlag.ACC_PRIVATE, AccessFlag.ACC_PROTECTED, AccessFlag.ACC_STATIC, AccessFlag.ACC_FINAL
    };

    public ClassFileDumperAnnotation(DCCommonState dcCommonState) {
        super(dcCommonState);
    }

    private void dumpHeader(ClassFile c, InnerClassDumpType innerClassDumpType, Dumper d) {

        d.print(getAccessFlagsString(c.getAccessFlags(), dumpableAccessFlagsInterface));

        d.print("@interface ");
        c.dumpClassIdentity(d);
        d.print(" ");
    }


    @Override
    public Dumper dump(ClassFile classFile, InnerClassDumpType innerClass, Dumper d) {

        if (!innerClass.isInnerClass()) {
            dumpTopHeader(classFile, d, true);
            dumpImports(d, classFile);
        }

        boolean first = true;
        dumpComments(classFile, d);
        dumpAnnotations(classFile, d);
        dumpHeader(classFile, innerClass, d);
        d.separator("{").newln();
        d.indent(1);
        // Horrid, but an interface can have fields....
        List<ClassFileField> fields = classFile.getFields();
        for (ClassFileField field : fields) {
            field.dump(d, classFile);
            first = false;
        }
        dumpMethods(classFile, d, first, false);
        classFile.dumpNamedInnerClasses(d);
        d.indent(-1);
        d.print("}").newln();
        return d;
    }

    @Override
    public void collectTypeUsages(TypeUsageCollector collector) {

    }
}

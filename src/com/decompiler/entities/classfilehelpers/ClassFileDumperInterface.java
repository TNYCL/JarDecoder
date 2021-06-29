package com.decompiler.entities.classfilehelpers;

import static com.decompiler.util.DecompilerComment.PACKAGE_INFO_CODE;

import java.util.List;

import com.decompiler.bytecode.analysis.types.ClassSignature;
import com.decompiler.bytecode.analysis.types.JavaRefTypeInstance;
import com.decompiler.bytecode.analysis.types.JavaTypeInstance;
import com.decompiler.entities.*;
import com.decompiler.state.DCCommonState;
import com.decompiler.state.TypeUsageCollector;
import com.decompiler.util.MiscConstants;
import com.decompiler.util.output.Dumper;

public class ClassFileDumperInterface extends AbstractClassFileDumper {

    private static final AccessFlag[] dumpableAccessFlagsInterface = new AccessFlag[]{
            AccessFlag.ACC_PUBLIC, AccessFlag.ACC_PRIVATE, AccessFlag.ACC_PROTECTED, AccessFlag.ACC_STRICT, AccessFlag.ACC_STATIC, AccessFlag.ACC_FINAL
    };

    public ClassFileDumperInterface(DCCommonState dcCommonState) {
        super(dcCommonState);
    }

    private void dumpHeader(ClassFile c, InnerClassDumpType innerClassDumpType, Dumper d) {

        d.print(getAccessFlagsString(c.getAccessFlags(), dumpableAccessFlagsInterface));

        d.print("interface ");
        c.dumpClassIdentity(d);
        d.newln();

        ClassSignature signature = c.getClassSignature();
        List<JavaTypeInstance> interfaces = signature.getInterfaces();
        if (!interfaces.isEmpty()) {
            d.print("extends ");
            int size = interfaces.size();
            for (int x = 0; x < size; ++x) {
                JavaTypeInstance iface = interfaces.get(x);
                d.dump(iface).print((x < (size - 1) ? "," : "")).newln();
            }
        }
        d.removePendingCarriageReturn().print(" ");
    }

    @Override
    public Dumper dump(ClassFile classFile, InnerClassDumpType innerClass, Dumper d) {
        // Do this now, as we don't necessarily know if something is a package info until later
        // than classfiledumper construction.
        // (we could match on the end of the filename, but that means we might get tricked by OS specific separators).
        if (isPackageInfo(classFile, d)) {
            dumpPackageInfo(classFile, d);
            return d;
        }

        if (!innerClass.isInnerClass()) {
            dumpTopHeader(classFile, d, true);
            dumpImports(d, classFile);
        }

        dumpComments(classFile, d);
        dumpAnnotations(classFile, d);
        dumpHeader(classFile, innerClass, d);
        boolean first = true;
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

    private boolean isPackageInfo(ClassFile classFile, Dumper d) {
        JavaRefTypeInstance classType = classFile.getRefClassType();
        if (!MiscConstants.PACKAGE_INFO.equals(classType.getRawShortName())) return false;
        // A package info should have no methods.
        List<Method> methods = classFile.getMethods();
        if (!methods.isEmpty()) {
            classFile.ensureDecompilerComments().addComment(PACKAGE_INFO_CODE);
            return false;
        }
        if (!classFile.getFields().isEmpty()) {
            classFile.ensureDecompilerComments().addComment(PACKAGE_INFO_CODE);
            return false;
        }
        return true;
    }

    private void dumpPackageInfo(ClassFile classFile, Dumper d) {
        dumpTopHeader(classFile, d, false);
        dumpAnnotations(classFile, d);
        d.packageName(classFile.getRefClassType());
        dumpImports(d, classFile);
        dumpComments(classFile, d);
    }

    @Override
    public void collectTypeUsages(TypeUsageCollector collector) {

    }
}

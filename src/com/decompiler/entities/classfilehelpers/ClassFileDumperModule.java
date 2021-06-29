package com.decompiler.entities.classfilehelpers;

import java.util.List;
import java.util.Set;

import com.decompiler.entities.ClassFile;
import com.decompiler.entities.attributes.AttributeModule;
import com.decompiler.entities.constantpool.ConstantPool;
import com.decompiler.entities.constantpool.ConstantPoolEntryClass;
import com.decompiler.entities.constantpool.ConstantPoolEntryModuleInfo;
import com.decompiler.entities.constantpool.ConstantPoolEntryPackageInfo;
import com.decompiler.state.DCCommonState;
import com.decompiler.state.TypeUsageCollector;
import com.decompiler.util.StringUtils;
import com.decompiler.util.collections.CollectionUtils;
import com.decompiler.util.output.Dumper;

public class ClassFileDumperModule extends AbstractClassFileDumper {

    public ClassFileDumperModule(DCCommonState dcCommonState) {
        super(dcCommonState);
    }

    @Override
    public Dumper dump(ClassFile classFile, InnerClassDumpType innerClass, Dumper d) {
        AttributeModule module = classFile.getAttributes().getByName(AttributeModule.ATTRIBUTE_NAME);
        ConstantPool cp = module.getCp();
        Set<AttributeModule.ModuleFlags> flags = module.getFlags();
        d.print(CollectionUtils.joinPostFix(flags, " "));
        d.print("module ").print(module.getModuleName()).print(" {").newln();
        d.indent(1);
        dumpRequires(cp, d, module.getRequires());
        dumpOpensExports(cp, d, module.getExports(), "exports");
        dumpOpensExports(cp, d, module.getOpens(), "opens");
        dumpProvides(cp, d, module.getProvides());
        d.indent(-1);
        d.print("}").newln();
        return d;
    }

    private void dumpRequires(ConstantPool cp, Dumper d, List<AttributeModule.Require> l) {
        if (l.isEmpty()) {
            return;
        }
        boolean effect = false;
        for (AttributeModule.Require r : l) {
            Set<AttributeModule.ModuleContentFlags> flags = r.getFlags();
            if (flags.contains(AttributeModule.ModuleContentFlags.MANDATED)) {
                continue;
            }
            ConstantPoolEntryModuleInfo module = cp.getModuleEntry(r.getIndex());
            d.print(CollectionUtils.joinPostFix(flags, " "));
            d.print("requires ").print(module.getName().getValue()).endCodeln();
            effect = true;
        }
        if (effect) {
            d.newln();
        }
    }

    private void dumpOpensExports(ConstantPool cp, Dumper d, List<AttributeModule.ExportOpen> l, String prefix) {
        if (l.isEmpty()) {
            return;
        }
        boolean effect = false;
        for (AttributeModule.ExportOpen r : l) {
            Set<AttributeModule.ModuleContentFlags> flags = r.getFlags();
            if (flags.contains(AttributeModule.ModuleContentFlags.MANDATED)) {
                continue;
            }
            ConstantPoolEntryPackageInfo pck = cp.getPackageEntry(r.getIndex());
            d.keyword(CollectionUtils.joinPostFix(flags, " "));
            d.keyword(prefix).print(' ').print(pck.getPackageName());
            int[] to = r.getToIndex();
            if (to.length != 0) {
                d.print(" to ");
                boolean first = true;
                for (int t : to) {
                    first = StringUtils.comma(first, d);
                    ConstantPoolEntryModuleInfo toModule = cp.getModuleEntry(t);
                    d.print(toModule.getName().getValue());
                }
            }
            d.endCodeln();
            effect = true;
        }
        if (effect) {
            d.newln();
        }
    }

    private void dumpProvides(ConstantPool cp, Dumper d, List<AttributeModule.Provide> l) {
        if (l.isEmpty()) {
            return;
        }
        for (AttributeModule.Provide r : l) {
            ConstantPoolEntryClass pck = cp.getClassEntry(r.getIndex());
            d.print("provides ").dump(pck.getTypeInstance());
            int[] with = r.getWithIndex();
            if (with.length != 0) {
                d.print(" with ");
                boolean first = true;
                for (int t : with) {
                    first = StringUtils.comma(first, d);
                    ConstantPoolEntryClass toModule = cp.getClassEntry(t);
                    d.dump(toModule.getTypeInstance());
                }
            }
            d.endCodeln();
        }
        d.newln();
    }


    @Override
    public void collectTypeUsages(TypeUsageCollector collector) {
        // we don't collect.  We want the dumper to use the full names.
    }
}

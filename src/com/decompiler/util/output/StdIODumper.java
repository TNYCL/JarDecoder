package com.decompiler.util.output;

import java.io.BufferedOutputStream;
import java.util.Set;

import com.decompiler.bytecode.analysis.types.JavaTypeInstance;
import com.decompiler.entities.Method;
import com.decompiler.state.TypeUsageInformation;
import com.decompiler.util.getopt.Options;

public class StdIODumper extends StreamDumper {
    StdIODumper(TypeUsageInformation typeUsageInformation, Options options, IllegalIdentifierDump illegalIdentifierDump, MovableDumperContext context) {
        super(typeUsageInformation, options, illegalIdentifierDump, context);
    }

    private StdIODumper(TypeUsageInformation typeUsageInformation, Options options, IllegalIdentifierDump illegalIdentifierDump, MovableDumperContext context, Set<JavaTypeInstance> emitted) {
        super(typeUsageInformation, options, illegalIdentifierDump, context, emitted);
    }

    @Override
    protected void write(String s) {
        System.out.print(s);
    }

    @Override
    public void addSummaryError(Method method, String s) {
    }

    @Override
    public void close() {
    }

    @Override
    public Dumper withTypeUsageInformation(TypeUsageInformation innerclassTypeUsageInformation) {
        return new StdIODumper(innerclassTypeUsageInformation, options, illegalIdentifierDump, context, emitted);
    }

    @Override
    public BufferedOutputStream getAdditionalOutputStream(String description) {
        return new BufferedOutputStream(System.out);
    }
}

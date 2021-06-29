package com.decompiler.util.output;

import com.decompiler.bytecode.analysis.types.JavaTypeInstance;
import com.decompiler.util.getopt.OptionsImpl;

public class ProgressDumperStdErr implements ProgressDumper {

    @Override
    public void analysingType(JavaTypeInstance type) {
        System.err.println("Processing " + type.getRawName());
    }

    @Override
    public void analysingPath(String path) {
        System.err.println("Processing " + path + " (use " + OptionsImpl.SILENT.getName() + " to silence)");
    }
}

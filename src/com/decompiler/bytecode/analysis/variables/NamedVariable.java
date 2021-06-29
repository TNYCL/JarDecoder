package com.decompiler.bytecode.analysis.variables;

import com.decompiler.util.output.Dumpable;
import com.decompiler.util.output.Dumper;

public interface NamedVariable extends Dumpable {
    void forceName(String name);

    String getStringName();

    boolean isGoodName();

    @Override
    Dumper dump(Dumper d);

    Dumper dump(Dumper d, boolean defines);
}

package com.decompiler.entities.attributes;

import com.decompiler.state.TypeUsageCollector;
import com.decompiler.util.KnowsRawName;
import com.decompiler.util.KnowsRawSize;
import com.decompiler.util.TypeUsageCollectable;
import com.decompiler.util.output.Dumpable;
import com.decompiler.util.output.Dumper;

public abstract class Attribute implements KnowsRawSize, KnowsRawName, Dumpable, TypeUsageCollectable {

    @Override
    public abstract Dumper dump(Dumper d);

    @Override
    public void collectTypeUsages(TypeUsageCollector collector) {
    }
}

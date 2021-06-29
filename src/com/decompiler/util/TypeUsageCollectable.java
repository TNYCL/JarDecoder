package com.decompiler.util;

import com.decompiler.state.TypeUsageCollector;

public interface TypeUsageCollectable {
    void collectTypeUsages(TypeUsageCollector collector);
}

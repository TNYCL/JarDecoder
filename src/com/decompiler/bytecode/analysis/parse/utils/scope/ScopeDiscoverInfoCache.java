package com.decompiler.bytecode.analysis.parse.utils.scope;

import java.util.Map;

import com.decompiler.bytecode.analysis.structured.StructuredStatement;
import com.decompiler.util.collections.MapFactory;

public class ScopeDiscoverInfoCache {
    private final Map<StructuredStatement, Boolean> tests = MapFactory.newIdentityMap();

    public Boolean get(StructuredStatement structuredStatement) {
        return tests.get(structuredStatement);
    }

    public void put(StructuredStatement structuredStatement, Boolean b) {
        tests.put(structuredStatement, b);
    }

    boolean anyFound() {
        for (Boolean value : tests.values()) {
            if (value) return true;
        }
        return false;
    }
}

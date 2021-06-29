package com.decompiler.bytecode.analysis.parse.utils;

import java.util.Map;

import com.decompiler.util.collections.MapFactory;
import com.decompiler.util.functors.UnaryFunction;

public class SSAIdentifierFactory<KEYTYPE, CMPTYPE> {
    private final Map<KEYTYPE, Integer> nextIdentFor = MapFactory.newLazyMap(
            MapFactory.<KEYTYPE, Integer>newOrderedMap(),
            new UnaryFunction<KEYTYPE, Integer>() {
                @Override
                public Integer invoke(KEYTYPE ignore) {
                    return 0;
                }
            });

    private final UnaryFunction<KEYTYPE, CMPTYPE> typeComparisonFunction;

    public SSAIdentifierFactory(UnaryFunction<KEYTYPE, CMPTYPE> typeComparisonFunction) {
        this.typeComparisonFunction = typeComparisonFunction;
    }

    public SSAIdent getIdent(KEYTYPE lValue) {
        int val = nextIdentFor.get(lValue);
        nextIdentFor.put(lValue, val + 1);
        return new SSAIdent(val, typeComparisonFunction == null ? null : typeComparisonFunction.invoke(lValue));
    }
}

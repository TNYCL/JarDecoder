package com.decompiler.bytecode.analysis.parse.utils;

public interface ComparableUnderEC {
    boolean equivalentUnder(Object o, EquivalenceConstraint constraint);
}

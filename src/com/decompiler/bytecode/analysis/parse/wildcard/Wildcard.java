package com.decompiler.bytecode.analysis.parse.wildcard;

public interface Wildcard<X> {
    X getMatch();

    void resetMatch();
}

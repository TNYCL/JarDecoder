package com.decompiler.bytecode.analysis.opgraph.op4rewriters.matchutil;

public interface Matcher<T> {
    boolean match(MatchIterator<T> matchIterator, MatchResultCollector matchResultCollector);
}

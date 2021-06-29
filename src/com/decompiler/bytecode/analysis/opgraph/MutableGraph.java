package com.decompiler.bytecode.analysis.opgraph;

public interface MutableGraph<T> extends Graph<T> {
    void addSource(T source);
    void addTarget(T target);
}

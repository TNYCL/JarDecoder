package com.decompiler.util.graph;

import com.decompiler.util.functors.BinaryProcedure;

public class GraphVisitorFIFO<T> extends AbstractGraphVisitorFI<T> {
    public GraphVisitorFIFO(T first, BinaryProcedure<T, GraphVisitor<T>> callee) {
        super(first, callee);
    }
}

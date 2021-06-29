package com.decompiler.util.functors;

public interface UnaryFunction<X,Y> {
    Y invoke(X arg);
}

package com.decompiler.util.functors;

public interface BinaryPredicate<X, Y> {
    boolean test(X a, Y b);
}

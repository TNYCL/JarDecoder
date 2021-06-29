package com.decompiler.bytecode.analysis.opgraph.op3rewriters;

import com.decompiler.bytecode.analysis.opgraph.Op03SimpleStatement;
import com.decompiler.util.functors.Predicate;

public class ExactTypeFilter<T> implements Predicate<Op03SimpleStatement> {
    private final Class<T> clazz;

    public ExactTypeFilter(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public boolean test(Op03SimpleStatement in) {
        return clazz == (in.getStatement().getClass());
    }
}

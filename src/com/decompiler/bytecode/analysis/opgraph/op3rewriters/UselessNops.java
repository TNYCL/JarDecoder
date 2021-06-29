package com.decompiler.bytecode.analysis.opgraph.op3rewriters;

import java.util.List;

import com.decompiler.bytecode.analysis.opgraph.Op03SimpleStatement;
import com.decompiler.util.collections.Functional;
import com.decompiler.util.functors.Predicate;

public class UselessNops {
    public static List<Op03SimpleStatement> removeUselessNops(List<Op03SimpleStatement> in) {
        return Functional.filter(in, new Predicate<Op03SimpleStatement>() {
            @Override
            public boolean test(Op03SimpleStatement in) {
                return !(in.getSources().isEmpty() && in.getTargets().isEmpty());
            }
        });
    }
}

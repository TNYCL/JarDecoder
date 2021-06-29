package com.decompiler.bytecode.analysis.opgraph.op3rewriters;

import java.util.Comparator;

import com.decompiler.bytecode.analysis.opgraph.Op03SimpleStatement;
import com.decompiler.util.ConfusedDecompilerException;

public class CompareByIndex implements Comparator<Op03SimpleStatement> {

    private boolean asc;

    public CompareByIndex() {
        this(true);
    }

    public CompareByIndex(boolean asc) {
        this.asc = asc;
    }

    @Override
    public int compare(Op03SimpleStatement a, Op03SimpleStatement b) {
        int res = a.getIndex().compareTo(b.getIndex());
        if (!asc) res = -res;
        if (res == 0) {
            throw new ConfusedDecompilerException("Can't sort instructions [" + a + ", " + b + "]");
        }
        //noinspection ComparatorMethodParameterNotUsed
        return res;
    }
}

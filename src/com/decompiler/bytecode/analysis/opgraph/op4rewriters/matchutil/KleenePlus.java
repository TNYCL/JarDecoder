package com.decompiler.bytecode.analysis.opgraph.op4rewriters.matchutil;

import com.decompiler.bytecode.analysis.structured.StructuredStatement;

public class KleenePlus extends KleeneN {
    public KleenePlus(Matcher<StructuredStatement> inner) {
        super(1, inner);
    }
}

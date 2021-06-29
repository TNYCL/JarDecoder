package com.decompiler.bytecode.analysis.opgraph.op4rewriters.matchutil;

import com.decompiler.bytecode.analysis.structured.StructuredStatement;

public class KleeneStar extends KleeneN {
    public KleeneStar(Matcher<StructuredStatement> inner) {
        super(0, inner);
    }
}

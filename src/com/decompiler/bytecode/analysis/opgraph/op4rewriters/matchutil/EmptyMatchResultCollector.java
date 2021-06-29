package com.decompiler.bytecode.analysis.opgraph.op4rewriters.matchutil;

import com.decompiler.bytecode.analysis.parse.wildcard.WildcardMatch;
import com.decompiler.bytecode.analysis.structured.StructuredStatement;

public class EmptyMatchResultCollector implements MatchResultCollector {
    @Override
    public void clear() {

    }

    @Override
    public void collectStatement(String name, StructuredStatement statement) {

    }

    @Override
    public void collectMatches(String name, WildcardMatch wcm) {

    }
}

package com.decompiler.bytecode.analysis.opgraph.op4rewriters.matchutil;

import com.decompiler.bytecode.analysis.parse.wildcard.WildcardMatch;
import com.decompiler.bytecode.analysis.structured.StructuredStatement;

public interface MatchResultCollector {
    void clear();

    void collectStatement(String name, StructuredStatement statement);

    void collectMatches(String name, WildcardMatch wcm);
}

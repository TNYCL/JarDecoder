package com.decompiler.bytecode.analysis.opgraph.op4rewriters.matchutil;

import com.decompiler.bytecode.analysis.structured.StructuredStatement;

/**
 * Note that this will match the FIRST match, not necessarily the best.
 * I.e. no backtracking.
 */
public class MatchOneOf implements Matcher<StructuredStatement> {
    private final Matcher<StructuredStatement>[] matchers;

    public MatchOneOf(Matcher<StructuredStatement>... matchers) {
        this.matchers = matchers;
    }

    @Override
    public boolean match(MatchIterator<StructuredStatement> matchIterator, MatchResultCollector matchResultCollector) {

        for (Matcher<StructuredStatement> matcher : matchers) {
            MatchIterator<StructuredStatement> mi = matchIterator.copy();
            if (matcher.match(mi, matchResultCollector)) {
                matchIterator.advanceTo(mi);
                return true;
            }
        }
        return false;
    }
}

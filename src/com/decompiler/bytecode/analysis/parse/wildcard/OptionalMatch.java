package com.decompiler.bytecode.analysis.parse.wildcard;

import com.decompiler.util.Optional;

public class OptionalMatch<T> {
    final Optional<T> expected; // may be missing.
    T matched;

    OptionalMatch(Optional<T> lhs) {
        this.expected = lhs;
        reset();
    }

    public boolean match(T other) {
        if (matched != null) return matched.equals(other);
        matched = other;
        return true;
    }

    public void reset() {
        if (expected.isSet()) {
            matched = expected.getValue();
        } else {
            matched = null;
        }
    }

    public T getMatch() {
        return matched;
    }
}

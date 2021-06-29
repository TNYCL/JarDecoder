package com.decompiler.bytecode.analysis.stack;

import java.util.Set;

import com.decompiler.bytecode.analysis.types.StackType;
import com.decompiler.util.DecompilerComment;

public class StackEntryHolder {
    private StackEntry stackEntry;

    StackEntryHolder(StackType stackType) {
        stackEntry = new StackEntry(stackType);
    }

    public void mergeWith(StackEntryHolder other, Set<DecompilerComment> comments) {
        stackEntry.mergeWith(other.stackEntry, comments);
        other.stackEntry = stackEntry;
    }

    @Override
    public String toString() {
        return stackEntry.toString();
    }

    public StackEntry getStackEntry() {
        return stackEntry;
    }
}

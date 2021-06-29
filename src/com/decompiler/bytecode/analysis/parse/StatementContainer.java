package com.decompiler.bytecode.analysis.parse;

import java.util.Set;

import com.decompiler.bytecode.analysis.opgraph.InstrIndex;
import com.decompiler.bytecode.analysis.parse.utils.BlockIdentifier;
import com.decompiler.bytecode.analysis.parse.utils.SSAIdentifiers;

public interface StatementContainer<T> {
    T getStatement();

    T getTargetStatement(int idx);

    String getLabel();

    InstrIndex getIndex();

    void nopOut();

    void replaceStatement(T newTarget);

    void nopOutConditional();

    SSAIdentifiers<LValue> getSSAIdentifiers();

    Set<BlockIdentifier> getBlockIdentifiers();

    BlockIdentifier getBlockStarted();

    Set<BlockIdentifier> getBlocksEnded();

    void copyBlockInformationFrom(StatementContainer<T> other);

    void copyBytecodeInformationFrom(StatementContainer<T> other);
}

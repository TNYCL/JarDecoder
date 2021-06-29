package com.decompiler.bytecode.analysis.parse.utils;

import com.decompiler.bytecode.analysis.parse.Expression;
import com.decompiler.bytecode.analysis.parse.LValue;
import com.decompiler.bytecode.analysis.parse.StatementContainer;
import com.decompiler.bytecode.analysis.parse.lvalue.LocalVariable;
import com.decompiler.bytecode.analysis.parse.lvalue.StackSSALabel;

public interface LValueAssignmentCollector<T> {
    // TODO : Should these be StackSSALabels?  Seems they should be lvalues.
    void collect(StackSSALabel lValue, StatementContainer<T> statementContainer, Expression value);

    void collectMultiUse(StackSSALabel lValue, StatementContainer<T> statementContainer, Expression value);

    void collectMutatedLValue(LValue lValue, StatementContainer<T> statementContainer, Expression value);

    void collectLocalVariableAssignment(LocalVariable localVariable, StatementContainer<T> statementContainer, Expression value);
}

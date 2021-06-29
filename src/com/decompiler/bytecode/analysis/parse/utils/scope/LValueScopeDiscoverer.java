package com.decompiler.bytecode.analysis.parse.utils.scope;

import com.decompiler.bytecode.analysis.opgraph.Op04StructuredStatement;
import com.decompiler.bytecode.analysis.parse.Expression;
import com.decompiler.bytecode.analysis.parse.LValue;
import com.decompiler.bytecode.analysis.parse.StatementContainer;
import com.decompiler.bytecode.analysis.parse.lvalue.LocalVariable;
import com.decompiler.bytecode.analysis.parse.lvalue.StackSSALabel;
import com.decompiler.bytecode.analysis.parse.utils.LValueAssignmentCollector;
import com.decompiler.bytecode.analysis.parse.utils.LValueUsageCollector;
import com.decompiler.bytecode.analysis.parse.utils.ReadWrite;
import com.decompiler.bytecode.analysis.structured.StructuredStatement;

public interface LValueScopeDiscoverer extends LValueUsageCollector, LValueAssignmentCollector<StructuredStatement> {
    void processOp04Statement(Op04StructuredStatement statement);

    void enterBlock(StructuredStatement structuredStatement);

    void leaveBlock(StructuredStatement structuredStatement);

    void mark(StatementContainer<StructuredStatement> mark);

    void collect(StackSSALabel lValue, StatementContainer<StructuredStatement> statementContainer, Expression value);

    void collectMultiUse(StackSSALabel lValue, StatementContainer<StructuredStatement> statementContainer, Expression value);

    void collectMutatedLValue(LValue lValue, StatementContainer<StructuredStatement> statementContainer, Expression value);

    void collectLocalVariableAssignment(LocalVariable localVariable, StatementContainer<StructuredStatement> statementContainer, Expression value);

    void collect(LValue lValue, ReadWrite rw);

    boolean ifCanDefine();

    boolean descendLambdas();
}

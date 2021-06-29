package com.decompiler.bytecode.analysis.parse.statement;

import com.decompiler.bytecode.analysis.loc.BytecodeLoc;
import com.decompiler.bytecode.analysis.parse.Expression;
import com.decompiler.bytecode.analysis.parse.LValue;
import com.decompiler.bytecode.analysis.parse.expression.AbstractAssignmentExpression;
import com.decompiler.bytecode.analysis.parse.expression.ArithOp;

public abstract class AbstractAssignment extends AbstractStatement {

    public AbstractAssignment(BytecodeLoc loc) {
        super(loc);
    }

    public abstract boolean isSelfMutatingOperation();

    public abstract boolean isSelfMutatingOp1(LValue lValue, ArithOp arithOp);

    public abstract Expression getPostMutation();

    public abstract Expression getPreMutation();

    public abstract AbstractAssignmentExpression getInliningExpression();
}

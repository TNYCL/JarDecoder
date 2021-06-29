package com.decompiler.bytecode.analysis.parse.expression;

import com.decompiler.bytecode.analysis.loc.BytecodeLoc;
import com.decompiler.bytecode.analysis.parse.LValue;
import com.decompiler.bytecode.analysis.types.discovery.InferredJavaType;

public abstract class AbstractAssignmentExpression extends AbstractExpression {

    public AbstractAssignmentExpression(BytecodeLoc loc, InferredJavaType inferredJavaType) {
        super(loc, inferredJavaType);
    }

    public abstract boolean isSelfMutatingOp1(LValue lValue, ArithOp arithOp);

    public abstract ArithmeticPostMutationOperation getPostMutation();

    public abstract ArithmeticPreMutationOperation getPreMutation();

    public abstract LValue getUpdatedLValue();
}

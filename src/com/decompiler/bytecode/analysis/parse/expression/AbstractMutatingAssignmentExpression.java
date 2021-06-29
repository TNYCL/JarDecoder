package com.decompiler.bytecode.analysis.parse.expression;

import com.decompiler.bytecode.analysis.loc.BytecodeLoc;
import com.decompiler.bytecode.analysis.types.RawJavaType;
import com.decompiler.bytecode.analysis.types.discovery.InferredJavaType;
import com.decompiler.entities.exceptions.ExceptionCheck;

public abstract class AbstractMutatingAssignmentExpression extends AbstractAssignmentExpression {
    AbstractMutatingAssignmentExpression(BytecodeLoc loc, InferredJavaType inferredJavaType) {
        super(loc, inferredJavaType);
    }

    @Override
    public boolean canThrow(ExceptionCheck caught) {
        return !(getInferredJavaType().getJavaTypeInstance() instanceof RawJavaType);
    }

    @Override
    public boolean isValidStatement() {
        return true;
    }
}

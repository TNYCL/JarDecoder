package com.decompiler.bytecode.analysis.opgraph.op3rewriters;

import com.decompiler.bytecode.analysis.parse.LValue;
import com.decompiler.bytecode.analysis.parse.StatementContainer;
import com.decompiler.bytecode.analysis.parse.rewriters.AbstractExpressionRewriter;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriterFlags;
import com.decompiler.bytecode.analysis.parse.utils.SSAIdentifiers;
import com.decompiler.bytecode.analysis.types.JavaTypeInstance;
import com.decompiler.bytecode.analysis.types.RawJavaType;
import com.decompiler.bytecode.analysis.types.discovery.InferredJavaType;

public class NullTypedLValueRewriter extends AbstractExpressionRewriter {
    @Override
    public LValue rewriteExpression(LValue lValue, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer, ExpressionRewriterFlags flags) {
        InferredJavaType inferredJavaType = lValue.getInferredJavaType();
        JavaTypeInstance javaTypeInstance = inferredJavaType.getJavaTypeInstance();
        if (javaTypeInstance == RawJavaType.NULL || javaTypeInstance == RawJavaType.VOID) {
            inferredJavaType.applyKnownBaseType();
        }
        return lValue;
    }
}

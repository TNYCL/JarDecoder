package com.decompiler.bytecode.analysis.parse.expression;

import java.util.List;

import com.decompiler.bytecode.analysis.loc.BytecodeLoc;
import com.decompiler.bytecode.analysis.parse.Expression;
import com.decompiler.bytecode.analysis.parse.StatementContainer;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriter;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriterFlags;
import com.decompiler.bytecode.analysis.parse.utils.SSAIdentifiers;
import com.decompiler.bytecode.analysis.types.JavaTypeInstance;
import com.decompiler.bytecode.analysis.types.MethodPrototype;
import com.decompiler.bytecode.analysis.types.discovery.InferredJavaType;
import com.decompiler.entities.constantpool.ConstantPoolEntryMethodRef;

public abstract class AbstractFunctionInvokation extends AbstractExpression {
    private final ConstantPoolEntryMethodRef function;
    private final MethodPrototype methodPrototype;

    AbstractFunctionInvokation(BytecodeLoc loc, ConstantPoolEntryMethodRef function, InferredJavaType inferredJavaType) {
        super(loc, inferredJavaType);
        this.function = function;
        this.methodPrototype = function.getMethodPrototype();
    }

    public abstract void applyExpressionRewriterToArgs(ExpressionRewriter expressionRewriter, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer, ExpressionRewriterFlags flags);

    public abstract void setExplicitGenerics(List<JavaTypeInstance> types);

    public abstract List<JavaTypeInstance> getExplicitGenerics();

    public ConstantPoolEntryMethodRef getFunction() {
        return function;
    }

    public MethodPrototype getMethodPrototype() { return methodPrototype; }

    public String getName() {
        return methodPrototype.getName();
    }

    String getFixedName() {
        return methodPrototype.getFixedName();
    }

    @Override
    public boolean isValidStatement() {
        return true;
    }

    public abstract List<Expression> getArgs();
}

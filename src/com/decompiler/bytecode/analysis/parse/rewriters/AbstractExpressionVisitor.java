package com.decompiler.bytecode.analysis.parse.rewriters;

import com.decompiler.bytecode.analysis.parse.Expression;
import com.decompiler.bytecode.analysis.parse.expression.ArithmeticOperation;
import com.decompiler.bytecode.analysis.parse.expression.Literal;
import com.decompiler.bytecode.analysis.parse.expression.TernaryExpression;

public class AbstractExpressionVisitor<T> implements ExpressionVisitor<T> {
    @Override
    public T visit(Expression e) {
        return null;
    }

    @Override
    public T visit(Literal l) {
        return null;
    }

    @Override
    public T visit(TernaryExpression e) {
        return null;
    }

    @Override
    public T visit(ArithmeticOperation e) {
        return null;
    }
}

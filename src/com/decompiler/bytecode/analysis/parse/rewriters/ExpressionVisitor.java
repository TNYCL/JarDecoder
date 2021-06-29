package com.decompiler.bytecode.analysis.parse.rewriters;

import com.decompiler.bytecode.analysis.parse.Expression;
import com.decompiler.bytecode.analysis.parse.expression.ArithmeticOperation;
import com.decompiler.bytecode.analysis.parse.expression.Literal;
import com.decompiler.bytecode.analysis.parse.expression.TernaryExpression;

public interface ExpressionVisitor<T> {
    T visit(Expression e);

    T visit(Literal l);

    T visit(TernaryExpression e);

    T visit(ArithmeticOperation e);
}

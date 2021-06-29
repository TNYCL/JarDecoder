package com.decompiler.entities.exceptions;

import java.util.Set;

import com.decompiler.bytecode.analysis.parse.Expression;
import com.decompiler.bytecode.analysis.parse.expression.AbstractMemberFunctionInvokation;
import com.decompiler.bytecode.analysis.types.JavaTypeInstance;

public class ExceptionCheckSimple implements ExceptionCheck {
    public static final ExceptionCheck INSTANCE = new ExceptionCheckSimple();

    private ExceptionCheckSimple() {
    }

    @Override
    public boolean checkAgainst(Set<? extends JavaTypeInstance> thrown) {
        return true;
    }

    @Override
    public boolean checkAgainst(AbstractMemberFunctionInvokation functionInvokation) {
        return true;
    }

    @Override
    public boolean checkAgainstException(Expression expression) {
        return true;
    }

    @Override
    public boolean mightCatchUnchecked() {
        return true;
    }
}

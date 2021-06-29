package com.decompiler.entities.exceptions;

import java.util.Set;

import com.decompiler.bytecode.analysis.parse.Expression;
import com.decompiler.bytecode.analysis.parse.expression.AbstractMemberFunctionInvokation;
import com.decompiler.bytecode.analysis.types.JavaTypeInstance;

public interface ExceptionCheck {
    boolean checkAgainst(Set<? extends JavaTypeInstance> thrown);

    // Might this throw in a way which means it can't be moved into the exception block?
    boolean checkAgainst(AbstractMemberFunctionInvokation functionInvokation);

    boolean checkAgainstException(Expression expression);

    boolean mightCatchUnchecked();
}

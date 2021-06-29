package com.decompiler.bytecode.analysis.opgraph.op4rewriters;

import com.decompiler.bytecode.analysis.parse.expression.SuperFunctionInvokation;

public class EnumAllSuperRewriter extends RedundantSuperRewriter {

    @Override
    protected boolean canBeNopped(SuperFunctionInvokation superInvokation) {
        return true;
    }
}

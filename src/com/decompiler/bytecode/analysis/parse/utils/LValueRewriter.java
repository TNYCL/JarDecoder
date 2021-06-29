package com.decompiler.bytecode.analysis.parse.utils;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.decompiler.bytecode.analysis.parse.Expression;
import com.decompiler.bytecode.analysis.parse.LValue;
import com.decompiler.bytecode.analysis.parse.StatementContainer;

public interface LValueRewriter<T> {
    Expression getLValueReplacement(LValue lValue, SSAIdentifiers<LValue> ssaIdentifiers, StatementContainer<T> statementContainer);

    boolean explicitlyReplaceThisLValue(LValue lValue);

    void checkPostConditions(LValue lValue, Expression rValue);

    LValueRewriter getWithFixed(Set<SSAIdent> fixed);

    boolean needLR();

    LValueRewriter<T> keepConstant(Collection<LValue> usedLValues);

    class Util {
        public static void rewriteArgArray(LValueRewriter lValueRewriter, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer, List<Expression> args) {
            boolean lr = lValueRewriter.needLR();
            int argsSize = args.size();
            for (int x = 0; x < argsSize; ++x) {
                int y = lr ? x : argsSize - 1 - x;
                args.set(y, args.get(y).replaceSingleUsageLValues(lValueRewriter, ssaIdentifiers, statementContainer));
            }
        }
    }
}

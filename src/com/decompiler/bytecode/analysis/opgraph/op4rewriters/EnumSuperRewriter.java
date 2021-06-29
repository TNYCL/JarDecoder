package com.decompiler.bytecode.analysis.opgraph.op4rewriters;

import java.util.List;
import java.util.Set;

import com.decompiler.bytecode.analysis.parse.Expression;
import com.decompiler.bytecode.analysis.parse.LValue;
import com.decompiler.bytecode.analysis.parse.expression.CastExpression;
import com.decompiler.bytecode.analysis.parse.expression.LValueExpression;
import com.decompiler.bytecode.analysis.parse.expression.SuperFunctionInvokation;
import com.decompiler.bytecode.analysis.parse.wildcard.WildcardMatch;
import com.decompiler.util.collections.ListFactory;
import com.decompiler.util.collections.SetFactory;

public class EnumSuperRewriter extends RedundantSuperRewriter {
    @Override
    protected List<Expression> getSuperArgs(WildcardMatch wcm) {
        List<Expression> res = ListFactory.newList();
        res.add(wcm.getExpressionWildCard("enum_a"));
        res.add(wcm.getExpressionWildCard("enum_b"));
        return res;
    }

    private static LValue getLValue(WildcardMatch wcm, String name) {
        Expression e = wcm.getExpressionWildCard(name).getMatch();
        while (e instanceof CastExpression) {
            e = ((CastExpression) e).getChild();
        }
        if (!(e instanceof LValueExpression)) {
            throw new IllegalStateException();
        }
        return ((LValueExpression) e).getLValue();
    }

    protected Set<LValue> getDeclarationsToNop(WildcardMatch wcm) {
        Set<LValue> res = SetFactory.newSet();
        res.add(getLValue(wcm, "enum_a"));
        res.add(getLValue(wcm, "enum_b"));
        return res;
    }

    @Override
    protected boolean canBeNopped(SuperFunctionInvokation superInvokation) {
        return true;
    }
}

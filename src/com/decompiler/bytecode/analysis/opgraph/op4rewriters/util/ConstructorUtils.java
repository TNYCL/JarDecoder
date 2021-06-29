package com.decompiler.bytecode.analysis.opgraph.op4rewriters.util;

import java.util.List;

import com.decompiler.bytecode.analysis.loc.BytecodeLoc;
import com.decompiler.bytecode.analysis.opgraph.Op04StructuredStatement;
import com.decompiler.bytecode.analysis.parse.expression.LValueExpression;
import com.decompiler.bytecode.analysis.parse.expression.MemberFunctionInvokation;
import com.decompiler.bytecode.analysis.parse.wildcard.WildcardMatch;
import com.decompiler.bytecode.analysis.structured.StructuredStatement;
import com.decompiler.bytecode.analysis.structured.statement.StructuredComment;
import com.decompiler.bytecode.analysis.structured.statement.StructuredExpressionStatement;
import com.decompiler.bytecode.analysis.types.MethodPrototype;
import com.decompiler.entities.Method;

public class ConstructorUtils {
    public static MethodPrototype getDelegatingPrototype(Method constructor) {
        List<Op04StructuredStatement> statements = MiscStatementTools.getBlockStatements(constructor.getAnalysis());
        if (statements == null) return null;
        for (Op04StructuredStatement statement : statements) {
            StructuredStatement structuredStatement = statement.getStatement();
            if (structuredStatement instanceof StructuredComment) continue;
            if (!(structuredStatement instanceof StructuredExpressionStatement)) return null;
            StructuredExpressionStatement structuredExpressionStatement = (StructuredExpressionStatement) structuredStatement;

            WildcardMatch wcm1 = new WildcardMatch();
            StructuredStatement test = new StructuredExpressionStatement(BytecodeLoc.NONE, wcm1.getMemberFunction("m", null, true /* this method */, new LValueExpression(wcm1.getLValueWildCard("o")), null), false);
            if (test.equals(structuredExpressionStatement)) {
                MemberFunctionInvokation m = wcm1.getMemberFunction("m").getMatch();
                MethodPrototype prototype = m.getMethodPrototype();
                return prototype;
            }
            return null;
        }
        return null;
    }

    public static boolean isDelegating(Method constructor) {
        return getDelegatingPrototype(constructor) != null;
    }
}


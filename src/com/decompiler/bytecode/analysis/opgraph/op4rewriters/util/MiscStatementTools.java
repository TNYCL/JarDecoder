package com.decompiler.bytecode.analysis.opgraph.op4rewriters.util;

import java.util.List;

import com.decompiler.bytecode.analysis.opgraph.Op04StructuredStatement;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriter;
import com.decompiler.bytecode.analysis.structured.StructuredStatement;
import com.decompiler.bytecode.analysis.structured.statement.Block;
import com.decompiler.bytecode.analysis.structured.statement.StructuredComment;
import com.decompiler.util.annotation.Nullable;
import com.decompiler.util.collections.ListFactory;

public class MiscStatementTools {
    public static List<Op04StructuredStatement> getBlockStatements(Op04StructuredStatement code) {
        StructuredStatement topCode = code.getStatement();
        if (!(topCode instanceof Block)) return null;

        Block block = (Block) topCode;
        List<Op04StructuredStatement> statements = block.getBlockStatements();
        return statements;
    }

    public static boolean isDeadCode(Op04StructuredStatement code) {
        List<Op04StructuredStatement> statements = getBlockStatements(code);
        if (statements == null) return false;
        for (Op04StructuredStatement statement : statements) {
            if (!(statement.getStatement() instanceof StructuredComment)) return false;
        }
        return true;
    }

    public static @Nullable
    List<StructuredStatement> linearise(Op04StructuredStatement root) {
        List<StructuredStatement> structuredStatements = ListFactory.newList();
        try {
            // This is being done multiple times, it's very inefficient!
            root.linearizeStatementsInto(structuredStatements);
        } catch (UnsupportedOperationException e) {
            // Todo : Should output something at the end about this failure.
            return null;
        }
        return structuredStatements;
    }

    public static void applyExpressionRewriter(Op04StructuredStatement root, ExpressionRewriter expressionRewriter) {
        List<StructuredStatement> statements = linearise(root);
        if (statements == null) return;
        for (StructuredStatement statement : statements) {
            statement.rewriteExpressions(expressionRewriter);
        }
    }
}

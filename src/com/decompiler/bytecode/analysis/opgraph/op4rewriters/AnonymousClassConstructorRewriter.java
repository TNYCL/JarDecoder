package com.decompiler.bytecode.analysis.opgraph.op4rewriters;

import java.util.List;

import com.decompiler.bytecode.analysis.opgraph.Op04StructuredStatement;
import com.decompiler.bytecode.analysis.parse.Expression;
import com.decompiler.bytecode.analysis.parse.StatementContainer;
import com.decompiler.bytecode.analysis.parse.expression.ConstructorInvokationAnonymousInner;
import com.decompiler.bytecode.analysis.parse.expression.SuperFunctionInvokation;
import com.decompiler.bytecode.analysis.parse.rewriters.AbstractExpressionRewriter;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriterFlags;
import com.decompiler.bytecode.analysis.parse.utils.SSAIdentifiers;
import com.decompiler.bytecode.analysis.structured.StructuredStatement;
import com.decompiler.bytecode.analysis.structured.statement.Block;
import com.decompiler.bytecode.analysis.structured.statement.StructuredComment;
import com.decompiler.bytecode.analysis.structured.statement.StructuredExpressionStatement;
import com.decompiler.entities.ClassFile;
import com.decompiler.entities.Method;

public class AnonymousClassConstructorRewriter extends AbstractExpressionRewriter {
    @Override
    public Expression rewriteExpression(Expression expression, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer, ExpressionRewriterFlags flags) {
        expression = super.rewriteExpression(expression, ssaIdentifiers, statementContainer, flags);
        if (expression instanceof ConstructorInvokationAnonymousInner) {
            ClassFile classFile = ((ConstructorInvokationAnonymousInner) expression).getClassFile();
            if (classFile != null) {
                for (Method constructor : classFile.getConstructors()) {
                    Op04StructuredStatement analysis = constructor.getAnalysis();
                    /*
                     * nop out initial super call, if present.
                     */
                    if (!(analysis.getStatement() instanceof Block)) continue;
                    Block block = (Block) analysis.getStatement();
                    List<Op04StructuredStatement> statements = block.getBlockStatements();
                    for (Op04StructuredStatement stmCont : statements) {
                        StructuredStatement stm = stmCont.getStatement();
                        if (stm instanceof StructuredComment) continue;
                        if (stm instanceof StructuredExpressionStatement) {
                            Expression e = ((StructuredExpressionStatement) stm).getExpression();
                            if (e instanceof SuperFunctionInvokation) {
                                stmCont.nopOut();
                                break;
                            }
                        }
                        break;
                    }
                }
            }
        }
        return expression;
    }
}

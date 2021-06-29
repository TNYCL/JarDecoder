package com.decompiler.bytecode.analysis.parse.rewriters;

import com.decompiler.bytecode.analysis.parse.Expression;
import com.decompiler.bytecode.analysis.parse.LValue;
import com.decompiler.bytecode.analysis.parse.StatementContainer;
import com.decompiler.bytecode.analysis.parse.expression.ConditionalExpression;
import com.decompiler.bytecode.analysis.parse.lvalue.StackSSALabel;
import com.decompiler.bytecode.analysis.parse.utils.SSAIdentifiers;

public interface ExpressionRewriter {
    Expression rewriteExpression(Expression expression, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer, ExpressionRewriterFlags flags);

    ConditionalExpression rewriteExpression(ConditionalExpression expression, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer, ExpressionRewriterFlags flags);

//    AbstractAssignmentExpression rewriteExpression(AbstractAssignmentExpression expression, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer, ExpressionRewriterFlags flags);

    LValue rewriteExpression(LValue lValue, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer, ExpressionRewriterFlags flags);

    StackSSALabel rewriteExpression(StackSSALabel lValue, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer, ExpressionRewriterFlags flags);

    /*
     * Does statement have specific handling?
     */
    void handleStatement(StatementContainer statementContainer);
}

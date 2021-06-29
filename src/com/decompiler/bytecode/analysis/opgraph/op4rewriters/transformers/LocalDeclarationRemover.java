package com.decompiler.bytecode.analysis.opgraph.op4rewriters.transformers;

import com.decompiler.bytecode.analysis.opgraph.Op04StructuredStatement;
import com.decompiler.bytecode.analysis.parse.Expression;
import com.decompiler.bytecode.analysis.parse.LValue;
import com.decompiler.bytecode.analysis.parse.StatementContainer;
import com.decompiler.bytecode.analysis.parse.expression.ConditionalExpression;
import com.decompiler.bytecode.analysis.parse.lvalue.SentinelLocalClassLValue;
import com.decompiler.bytecode.analysis.parse.lvalue.StackSSALabel;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriter;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriterFlags;
import com.decompiler.bytecode.analysis.parse.utils.SSAIdentifiers;
import com.decompiler.bytecode.analysis.structured.StructuredScope;
import com.decompiler.bytecode.analysis.structured.StructuredStatement;
import com.decompiler.bytecode.analysis.structured.expression.StructuredStatementExpression;
import com.decompiler.bytecode.analysis.structured.statement.StructuredComment;
import com.decompiler.bytecode.analysis.structured.statement.StructuredDefinition;

public class LocalDeclarationRemover implements StructuredStatementTransformer, ExpressionRewriter {
    @Override
    public StructuredStatement transform(StructuredStatement in, StructuredScope scope) {
        if (in instanceof StructuredDefinition) {
            if (((StructuredDefinition) in).getLvalue() instanceof SentinelLocalClassLValue) {
                return StructuredComment.EMPTY_COMMENT;
            }
        }
        in.transformStructuredChildren(this, scope);
        in.rewriteExpressions(this);
        return in;
    }

    @Override
    public Expression rewriteExpression(Expression expression, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer, ExpressionRewriterFlags flags) {
        Expression tmp = expression.applyExpressionRewriter(this, ssaIdentifiers, statementContainer, flags);
        if (tmp instanceof StructuredStatementExpression) {
            Op04StructuredStatement container = ((StructuredStatementExpression) tmp).getContent().getContainer();
            // Can't always assume a container - some lambda expressions won't have one.
            if (container != null) {
                container.transform(this, new StructuredScope());
            }
        }
        return tmp;
    }

    @Override
    public ConditionalExpression rewriteExpression(ConditionalExpression expression, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer, ExpressionRewriterFlags flags) {
        return (ConditionalExpression) expression.applyExpressionRewriter(this, ssaIdentifiers, statementContainer, flags);
    }

    @Override
    public LValue rewriteExpression(LValue lValue, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer, ExpressionRewriterFlags flags) {
        return lValue;
    }

    @Override
    public StackSSALabel rewriteExpression(StackSSALabel lValue, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer, ExpressionRewriterFlags flags) {
        return lValue;
    }

    @Override
    public void handleStatement(StatementContainer statementContainer) {

    }
}

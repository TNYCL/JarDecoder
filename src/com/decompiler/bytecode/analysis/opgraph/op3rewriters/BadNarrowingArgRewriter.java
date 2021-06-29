package com.decompiler.bytecode.analysis.opgraph.op3rewriters;

import com.decompiler.bytecode.analysis.loc.BytecodeLoc;
import com.decompiler.bytecode.analysis.parse.Expression;
import com.decompiler.bytecode.analysis.parse.StatementContainer;
import com.decompiler.bytecode.analysis.parse.expression.AbstractFunctionInvokation;
import com.decompiler.bytecode.analysis.parse.expression.CastExpression;
import com.decompiler.bytecode.analysis.parse.expression.Literal;
import com.decompiler.bytecode.analysis.parse.literal.TypedLiteral;
import com.decompiler.bytecode.analysis.parse.rewriters.AbstractExpressionRewriter;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriterFlags;
import com.decompiler.bytecode.analysis.parse.utils.SSAIdentifiers;
import com.decompiler.bytecode.analysis.types.discovery.InferredJavaType;

public class BadNarrowingArgRewriter extends AbstractExpressionRewriter {
    private class InternalBadNarrowingRewriter extends AbstractExpressionRewriter {
        @Override
        public Expression rewriteExpression(Expression expression, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer, ExpressionRewriterFlags flags) {
            Expression rwExpression = expression;
            if (expression instanceof CastExpression) {
                CastExpression castExpression = (CastExpression)expression;
                if (!castExpression.isForced()) {
                    rwExpression = rewriteLiteral(expression, castExpression.getChild(), castExpression.getInferredJavaType());
                }
            } else {
                rwExpression = rewriteLiteral(expression, expression, expression.getInferredJavaType());
            }
            return rwExpression;
        }

        private Expression rewriteLiteral(Expression original, Expression possibleLiteral, InferredJavaType tgtType) {
            if (possibleLiteral instanceof Literal) {
                Literal literal = (Literal)possibleLiteral;
                TypedLiteral tl = literal.getValue();
                if (tl.getType() == TypedLiteral.LiteralType.Integer) {
                    switch (tgtType.getRawType()) {
                        case BYTE:
                        case SHORT:
                            return new CastExpression(BytecodeLoc.NONE, tgtType, possibleLiteral, true);
                    }
                }
            }
            return original;
        }
    }

    private final InternalBadNarrowingRewriter internalBadNarrowingRewriter = new InternalBadNarrowingRewriter();

    @Override
    public Expression rewriteExpression(Expression expression, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer, ExpressionRewriterFlags flags) {
        // If we're rewriting a method call
        if (expression instanceof AbstractFunctionInvokation) {
            AbstractFunctionInvokation functionInvokation = (AbstractFunctionInvokation)expression;
            functionInvokation.applyExpressionRewriterToArgs(internalBadNarrowingRewriter, ssaIdentifiers, statementContainer, flags);
        }
        return super.rewriteExpression(expression, ssaIdentifiers, statementContainer, flags);
    }
}

package com.decompiler.bytecode.analysis.opgraph.op4rewriters;

import com.decompiler.bytecode.analysis.loc.BytecodeLoc;
import com.decompiler.bytecode.analysis.parse.Expression;
import com.decompiler.bytecode.analysis.parse.StatementContainer;
import com.decompiler.bytecode.analysis.parse.expression.CastExpression;
import com.decompiler.bytecode.analysis.parse.expression.ConditionalExpression;
import com.decompiler.bytecode.analysis.parse.expression.Literal;
import com.decompiler.bytecode.analysis.parse.expression.TernaryExpression;
import com.decompiler.bytecode.analysis.parse.rewriters.AbstractExpressionRewriter;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriterFlags;
import com.decompiler.bytecode.analysis.parse.utils.SSAIdentifiers;
import com.decompiler.bytecode.analysis.types.JavaTypeInstance;
import com.decompiler.bytecode.analysis.types.RawJavaType;
import com.decompiler.bytecode.analysis.types.TypeConstants;
import com.decompiler.bytecode.analysis.types.discovery.InferredJavaType;

public class BadCastChainRewriter extends AbstractExpressionRewriter {
    @Override
    public Expression rewriteExpression(Expression expression, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer, ExpressionRewriterFlags flags) {
        expression = super.rewriteExpression(expression, ssaIdentifiers, statementContainer, flags);
        if (expression instanceof CastExpression) {
            Expression child = ((CastExpression) expression).getChild();
            JavaTypeInstance type = expression.getInferredJavaType().getJavaTypeInstance().getDeGenerifiedType();
            JavaTypeInstance childType = child.getInferredJavaType().getJavaTypeInstance().getDeGenerifiedType();
            if (type.isComplexType() && childType.isComplexType()) {
                if (!childType.correctCanCastTo(type, null)) {
                    expression = new CastExpression(BytecodeLoc.NONE,
                            expression.getInferredJavaType(),
                        new CastExpression(BytecodeLoc.NONE, new InferredJavaType(TypeConstants.OBJECT, InferredJavaType.Source.UNKNOWN),
                                child, true)
                    );
                }
            } else if (childType == RawJavaType.BOOLEAN && child instanceof ConditionalExpression) {
                child = new TernaryExpression(BytecodeLoc.NONE, (ConditionalExpression)child, Literal.INT_ONE, Literal.INT_ZERO);
                expression = new CastExpression(BytecodeLoc.NONE,
                        expression.getInferredJavaType(), child
                        );
            }
        }
        return expression;
    }
}

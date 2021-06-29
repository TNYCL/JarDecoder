package com.decompiler.bytecode.analysis.parse.expression;

import java.util.List;

import com.decompiler.bytecode.analysis.loc.BytecodeLoc;
import com.decompiler.bytecode.analysis.parse.Expression;
import com.decompiler.bytecode.analysis.parse.StatementContainer;
import com.decompiler.bytecode.analysis.parse.expression.misc.Precedence;
import com.decompiler.bytecode.analysis.parse.rewriters.CloneHelper;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriter;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriterFlags;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriterHelper;
import com.decompiler.bytecode.analysis.parse.utils.EquivalenceConstraint;
import com.decompiler.bytecode.analysis.parse.utils.LValueRewriter;
import com.decompiler.bytecode.analysis.parse.utils.LValueUsageCollector;
import com.decompiler.bytecode.analysis.parse.utils.SSAIdentifiers;
import com.decompiler.bytecode.analysis.types.JavaTypeInstance;
import com.decompiler.bytecode.analysis.types.discovery.InferredJavaType;
import com.decompiler.util.StringUtils;
import com.decompiler.util.output.Dumper;

/**
 * A call that doesn't necessarily exist, for a type we don't necessarily have.
 */
public abstract class AbstractFunctionInvokationExplicit extends AbstractExpression {
    private final JavaTypeInstance clazz;
    private final String method;
    private final List<Expression> args;

    AbstractFunctionInvokationExplicit(BytecodeLoc loc, InferredJavaType res, JavaTypeInstance clazz, String method, List<Expression> args) {
        super(loc, res);
        this.clazz = clazz;
        this.method = method;
        this.args = args;
    }

    protected JavaTypeInstance getClazz() {
        return clazz;
    }

    protected String getMethod() {
        return method;
    }

    protected List<Expression> getArgs() {
        return args;
    }

    @Override
    public Precedence getPrecedence() {
        return Precedence.WEAKEST;
    }

    @Override
    public Expression replaceSingleUsageLValues(LValueRewriter lValueRewriter, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer) {
        LValueRewriter.Util.rewriteArgArray(lValueRewriter, ssaIdentifiers, statementContainer, args);
        return this;
    }

    @Override
    public Expression applyExpressionRewriter(ExpressionRewriter expressionRewriter, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer, ExpressionRewriterFlags flags) {
        ExpressionRewriterHelper.applyForwards(args, expressionRewriter, ssaIdentifiers, statementContainer, flags);
        return this;
    }

    @Override
    public Expression applyReverseExpressionRewriter(ExpressionRewriter expressionRewriter, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer, ExpressionRewriterFlags flags) {
        ExpressionRewriterHelper.applyBackwards(args, expressionRewriter, ssaIdentifiers, statementContainer, flags);
        return this;
    }

    @Override
    public void collectUsedLValues(LValueUsageCollector lValueUsageCollector) {
        for (Expression expression : args) {
            expression.collectUsedLValues(lValueUsageCollector);
        }
    }
}

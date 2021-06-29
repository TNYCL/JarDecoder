package com.decompiler.bytecode.analysis.parse.expression;

import java.util.Collections;
import java.util.List;

import com.decompiler.bytecode.analysis.loc.BytecodeLoc;
import com.decompiler.bytecode.analysis.opgraph.op4rewriters.PrimitiveBoxingRewriter;
import com.decompiler.bytecode.analysis.parse.Expression;
import com.decompiler.bytecode.analysis.parse.StatementContainer;
import com.decompiler.bytecode.analysis.parse.expression.rewriteinterface.BoxingProcessor;
import com.decompiler.bytecode.analysis.parse.rewriters.CloneHelper;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriter;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriterFlags;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriterHelper;
import com.decompiler.bytecode.analysis.parse.utils.EquivalenceConstraint;
import com.decompiler.bytecode.analysis.parse.utils.LValueRewriter;
import com.decompiler.bytecode.analysis.parse.utils.LValueUsageCollector;
import com.decompiler.bytecode.analysis.parse.utils.SSAIdentifiers;
import com.decompiler.bytecode.analysis.types.*;
import com.decompiler.bytecode.analysis.types.discovery.InferredJavaType;
import com.decompiler.entities.classfilehelpers.OverloadMethodSet;
import com.decompiler.entities.constantpool.ConstantPoolEntryMethodRef;
import com.decompiler.state.TypeUsageCollector;

public abstract class AbstractConstructorInvokation extends AbstractExpression implements BoxingProcessor {
    private final ConstantPoolEntryMethodRef function;
    private final MethodPrototype methodPrototype;
    private final List<Expression> args;

    AbstractConstructorInvokation(BytecodeLoc loc, InferredJavaType inferredJavaType, ConstantPoolEntryMethodRef function, List<Expression> args) {
        super(loc, inferredJavaType);
        this.args = args;
        this.function = function;
        this.methodPrototype = function.getMethodPrototype();
    }

    AbstractConstructorInvokation(BytecodeLoc loc, AbstractConstructorInvokation other, CloneHelper cloneHelper) {
        super(loc, other.getInferredJavaType());
        this.args = cloneHelper.replaceOrClone(other.args);
        this.function = other.function;
        this.methodPrototype = other.methodPrototype;
    }

    @Override
    public void collectTypeUsages(TypeUsageCollector collector) {
        methodPrototype.collectTypeUsages(collector);
        for (Expression arg : args) {
            arg.collectTypeUsages(collector);
        }
        super.collectTypeUsages(collector);
    }

    public List<Expression> getArgs() {
        return args;
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

    public JavaTypeInstance getTypeInstance() {
        return getInferredJavaType().getJavaTypeInstance();
    }

    @Override
    public void collectUsedLValues(LValueUsageCollector lValueUsageCollector) {
        for (Expression expression : args) {
            expression.collectUsedLValues(lValueUsageCollector);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o == null) return false;

        if (!(o instanceof AbstractConstructorInvokation)) return false;
        AbstractConstructorInvokation other = (AbstractConstructorInvokation) o;

        if (!getTypeInstance().equals(other.getTypeInstance())) return false;
        if (!args.equals(other.args)) return false;
        return true;
    }

    @Override
    public boolean equivalentUnder(Object o, EquivalenceConstraint constraint) {
        if (o == this) return true;
        if (o == null) return false;

        if (!(o instanceof AbstractConstructorInvokation)) return false;
        AbstractConstructorInvokation other = (AbstractConstructorInvokation) o;

        if (!constraint.equivalent(getTypeInstance(), other.getTypeInstance())) return false;
        if (!constraint.equivalent(args, other.args)) return false;
        return true;
    }

    /*
     * Duplicate code with abstractFunctionInvokation
     */
    final OverloadMethodSet getOverloadMethodSet() {
        OverloadMethodSet overloadMethodSet = methodPrototype.getOverloadMethodSet();
        if (overloadMethodSet == null) return null;
        JavaTypeInstance objectType = getInferredJavaType().getJavaTypeInstance();
        if (objectType instanceof JavaGenericRefTypeInstance) {
            JavaGenericRefTypeInstance genericType = (JavaGenericRefTypeInstance) objectType;
            return overloadMethodSet.specialiseTo(genericType);
        }
        return overloadMethodSet;
    }

    @Override
    public boolean isValidStatement() {
        return true;
    }

    protected final MethodPrototype getMethodPrototype() {
        return methodPrototype;
    }

    @Override
    public boolean rewriteBoxing(PrimitiveBoxingRewriter boxingRewriter) {

        List<Expression> args = getArgs();

        if (args.isEmpty()) return false;
        /*
         * Ignore completely for lambda, etc.
         */

        OverloadMethodSet overloadMethodSet = getOverloadMethodSet();
        if (overloadMethodSet == null) {
            /* We can't change any of the types here.
             * Best we can do is remove invalid casts.
             */
            boxingRewriter.removeRedundantCastOnly(args);
            return false;
        }

        GenericTypeBinder gtb = methodPrototype.getTypeBinderFor(args);
        boolean callsCorrectEntireMethod = overloadMethodSet.callsCorrectEntireMethod(args, gtb);
        for (int x = 0; x < args.size(); ++x) {
            /*
             * We can only remove explicit boxing if the target type is correct -
             * i.e. calling an object function with an explicit box can't have the box removed.
             *
             * This is fixed by a later pass which makes sure that the argument
             * can be passed to the target.
             */
            Expression arg = args.get(x);
            /*
             * we only need to shove a cast to the exact type on it if our current argument
             * doesn't call the 'correct' method.
             */
            if (!callsCorrectEntireMethod && !overloadMethodSet.callsCorrectMethod(arg, x, null)) {
                /*
                 * If arg isn't the right type, shove an extra cast on the front now.
                 * Then we will forcibly remove it if we don't need it.
                 */
                JavaTypeInstance argType = overloadMethodSet.getArgType(x, arg.getInferredJavaType().getJavaTypeInstance());
                boolean ignore = false;
                if (argType instanceof JavaGenericBaseInstance) {
                    // TODO : Should check flag for ignore bad generics?
                    ignore = ((JavaGenericBaseInstance) argType).hasForeignUnbound(function.getCp(), 0, false, Collections.<String, FormalTypeParameter>emptyMap());
                }
                /*
                 * Lambda types will always look wrong.
                 */
                if (!ignore) {
                    ignore = arg instanceof LambdaExpression
                            || arg instanceof LambdaExpressionFallback;
                }
                if (!ignore) {
                    arg = new CastExpression(BytecodeLoc.NONE, new InferredJavaType(argType, InferredJavaType.Source.EXPRESSION, true), arg);
                }
            }

            arg = boxingRewriter.rewriteExpression(arg, null, null, null);
            arg = boxingRewriter.sugarParameterBoxing(arg, x, overloadMethodSet, null, methodPrototype);
            args.set(x, arg);
        }
        return true;
    }

    @Override
    public void applyNonArgExpressionRewriter(ExpressionRewriter expressionRewriter, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer, ExpressionRewriterFlags flags) {
    }
}

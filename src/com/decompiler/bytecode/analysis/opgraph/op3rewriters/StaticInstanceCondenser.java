package com.decompiler.bytecode.analysis.opgraph.op3rewriters;

import java.util.List;
import java.util.Map;

import com.decompiler.bytecode.analysis.opgraph.Op03SimpleStatement;
import com.decompiler.bytecode.analysis.parse.Expression;
import com.decompiler.bytecode.analysis.parse.LValue;
import com.decompiler.bytecode.analysis.parse.Statement;
import com.decompiler.bytecode.analysis.parse.StatementContainer;
import com.decompiler.bytecode.analysis.parse.expression.LValueExpression;
import com.decompiler.bytecode.analysis.parse.expression.Literal;
import com.decompiler.bytecode.analysis.parse.expression.StaticFunctionInvokation;
import com.decompiler.bytecode.analysis.parse.rewriters.AbstractExpressionRewriter;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriterFlags;
import com.decompiler.bytecode.analysis.parse.statement.ExpressionStatement;
import com.decompiler.bytecode.analysis.parse.statement.GotoStatement;
import com.decompiler.bytecode.analysis.parse.statement.IfStatement;
import com.decompiler.bytecode.analysis.parse.statement.Nop;
import com.decompiler.bytecode.analysis.parse.utils.SSAIdentifiers;
import com.decompiler.bytecode.analysis.types.JavaTypeInstance;
import com.decompiler.util.collections.MapFactory;

public class StaticInstanceCondenser {
    public static final StaticInstanceCondenser INSTANCE = new StaticInstanceCondenser();

    /* Expression statement, which involves a different class (maybe via a field), followed by a static
     * method call.
     * The static method call may be void, or it may be side effecting.
     *
     * i.e.
     *
     * Foo.CONSTANT
     * ConstantType.doThing()
     *
     * Foo.CONSTANT
     * x = ConstantType.doThing()
     */
    public void rewrite(List<Op03SimpleStatement> statements) {
        for (Op03SimpleStatement stm : statements) {
            if (stm.getStatement() instanceof ExpressionStatement) {
                consider(stm);
            }
        }
    }

    private void consider(Op03SimpleStatement stm) {
        ExpressionStatement es = (ExpressionStatement)stm.getStatement();
        Expression e = es.getExpression();
        // Don't think this is relevant for other than lvalue expressions?
        if (!(e instanceof LValueExpression)) return;
        if (stm.getTargets().size() != 1) return;
        JavaTypeInstance typ = e.getInferredJavaType().getJavaTypeInstance();

        Op03SimpleStatement next = Misc.followNopGoto(stm.getTargets().get(0), true, false);

        // Hope to find a static method invokation in next, which invokes a static method on typ.
        Rewriter rewriter = new Rewriter(e, typ);
        next.rewrite(rewriter);
        if (rewriter.success) {
            stm.nopOut();
        }
    }

    private static class Rewriter extends AbstractExpressionRewriter {
        JavaTypeInstance typ;
        Expression object;
        boolean done = false;
        boolean success = false;

        Rewriter(Expression object, JavaTypeInstance typ) {
            this.object = object;
            this.typ = typ;
        }

        @Override
        public Expression rewriteExpression(Expression expression, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer, ExpressionRewriterFlags flags) {
            if (done) return expression;
            if (expression instanceof StaticFunctionInvokation) {
                StaticFunctionInvokation sfe = (StaticFunctionInvokation)expression;
                JavaTypeInstance staticType = sfe.getClazz();
                if (staticType.equals(typ)) {
                    sfe.forceObject(object);
                    success = true;
                }
                done = true;
                return expression;
            }
            return super.rewriteExpression(expression, ssaIdentifiers, statementContainer, flags);
        }
    }

}

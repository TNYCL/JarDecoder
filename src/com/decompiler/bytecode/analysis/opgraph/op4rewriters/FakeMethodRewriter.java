package com.decompiler.bytecode.analysis.opgraph.op4rewriters;

import com.decompiler.bytecode.analysis.opgraph.Op04StructuredStatement;
import com.decompiler.bytecode.analysis.opgraph.op4rewriters.transformers.ExpressionRewriterTransformer;
import com.decompiler.bytecode.analysis.parse.Expression;
import com.decompiler.bytecode.analysis.parse.StatementContainer;
import com.decompiler.bytecode.analysis.parse.expression.MethodHandlePlaceholder;
import com.decompiler.bytecode.analysis.parse.rewriters.AbstractExpressionRewriter;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriterFlags;
import com.decompiler.bytecode.analysis.parse.utils.SSAIdentifiers;
import com.decompiler.entities.ClassFile;
import com.decompiler.entities.FakeMethod;
import com.decompiler.entities.Method;
import com.decompiler.state.TypeUsageCollectingDumper;

public class FakeMethodRewriter {
    public static void rewrite(ClassFile classFile, TypeUsageCollectingDumper typeUsage) {
        ExpressionRewriterTransformer trans = new ExpressionRewriterTransformer(new Rewriter(classFile, typeUsage));
        for (Method method : classFile.getMethods()) {
            if (method.hasCodeAttribute()) {
                Op04StructuredStatement code = method.getAnalysis();
                trans.transform(code);
            }
        }
    }

    private static class Rewriter extends AbstractExpressionRewriter {
        private final ClassFile classFile;
        private TypeUsageCollectingDumper typeUsage;

        Rewriter(ClassFile classFile, TypeUsageCollectingDumper typeUsage) {
            this.classFile = classFile;
            this.typeUsage = typeUsage;
        }

        @Override
        public Expression rewriteExpression(Expression expression, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer, ExpressionRewriterFlags flags) {
            if (expression instanceof MethodHandlePlaceholder) {
                FakeMethod method = ((MethodHandlePlaceholder) expression).addFakeMethod(classFile);
                typeUsage.dump(method);
            }
            return super.rewriteExpression(expression, ssaIdentifiers, statementContainer, flags);
        }
    }
}

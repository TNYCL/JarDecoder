package com.decompiler.bytecode.analysis.opgraph.op4rewriters;

import java.util.List;
import java.util.Map;

import com.decompiler.bytecode.analysis.opgraph.Op04StructuredStatement;
import com.decompiler.bytecode.analysis.opgraph.op4rewriters.util.MiscStatementTools;
import com.decompiler.bytecode.analysis.parse.Expression;
import com.decompiler.bytecode.analysis.parse.StatementContainer;
import com.decompiler.bytecode.analysis.parse.expression.*;
import com.decompiler.bytecode.analysis.parse.rewriters.AbstractExpressionRewriter;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriterFlags;
import com.decompiler.bytecode.analysis.parse.utils.SSAIdentifiers;
import com.decompiler.bytecode.analysis.structured.StructuredStatement;
import com.decompiler.bytecode.analysis.types.*;

// Well, this is pretty specific!
// LOCAL inlined constants don't have a nice hint about the type they belong to, so we only use strings,
// and assume they indeed come from here.
public class LocalInlinedStringConstantRewriter extends AbstractExpressionRewriter implements Op04Rewriter {

    private final Map<String, Expression> rewrites;

    public LocalInlinedStringConstantRewriter(Map<String, Expression> rewrites) {
        this.rewrites = rewrites;
    }

    // TODO : This is a very common pattern - linearize is treated as a util - we should just walk.
    @Override
    public void rewrite(Op04StructuredStatement root) {
        List<StructuredStatement> structuredStatements = MiscStatementTools.linearise(root);
        if (structuredStatements == null) return;

        for (StructuredStatement statement : structuredStatements) {
            statement.rewriteExpressions(this);
        }
    }

    /*
     * Expression rewriter boilerplate - note that we can't expect ssaIdentifiers to be non-null.
     */
    @Override
    public Expression rewriteExpression(Expression expression, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer, ExpressionRewriterFlags flags) {
        // would be nicer perhaps to replace this with a dictonary<Expression, Expression> rewriter.
        if (expression instanceof Literal && expression.getInferredJavaType().getJavaTypeInstance() == TypeConstants.STRING) {
            Literal exp = (Literal)expression;
            Object val = exp.getValue().getValue();
            if (val instanceof String) {
                String str = (String)val;
                Expression replacement = rewrites.get(str);
                if (replacement != null) return replacement;
            }
        }
        expression = expression.applyExpressionRewriter(this, ssaIdentifiers, statementContainer, flags);
        return expression;
    }
}

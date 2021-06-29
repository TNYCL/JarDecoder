package com.decompiler.bytecode.analysis.opgraph.op4rewriters;

import java.util.List;

import com.decompiler.bytecode.analysis.opgraph.Op04StructuredStatement;
import com.decompiler.bytecode.analysis.opgraph.op4rewriters.util.MiscStatementTools;
import com.decompiler.bytecode.analysis.parse.Expression;
import com.decompiler.bytecode.analysis.parse.LValue;
import com.decompiler.bytecode.analysis.parse.expression.CastExpression;
import com.decompiler.bytecode.analysis.structured.StructuredStatement;
import com.decompiler.bytecode.analysis.structured.statement.StructuredAssignment;
import com.decompiler.bytecode.analysis.types.RawJavaType;

// It's not normally valid to use an implicit narrowing conversion, however in the case where this is an assignment,
// it is.
public class NarrowingAssignmentRewriter implements Op04Rewriter {

    public NarrowingAssignmentRewriter() {
    }

    @Override
    public void rewrite(Op04StructuredStatement root) {
        List<StructuredStatement> statements = MiscStatementTools.linearise(root);
        if (statements == null) return;
        for (StructuredStatement s : statements) {
            if (!(s instanceof StructuredAssignment)) continue;
            StructuredAssignment ass = (StructuredAssignment)s;
            LValue lValue = ass.getLvalue();
            RawJavaType raw =  RawJavaType.getUnboxedTypeFor(lValue.getInferredJavaType().getJavaTypeInstance());
            if (raw == null) continue;
            Expression rhs = ass.getRvalue();
            if (!(rhs instanceof CastExpression)) continue;
            CastExpression exp = (CastExpression)rhs;
            if (!(exp.isForced() && exp.getInferredJavaType().getRawType() == raw)) continue;
            s.rewriteExpressions(new ExpressionReplacingRewriter(exp, exp.getChild()));
        }
    }
}

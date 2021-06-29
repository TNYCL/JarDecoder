package com.decompiler.bytecode.analysis.opgraph.op4rewriters.transformers;

import java.util.Set;

import com.decompiler.bytecode.analysis.loc.BytecodeLoc;
import com.decompiler.bytecode.analysis.opgraph.Op04StructuredStatement;
import com.decompiler.bytecode.analysis.parse.Expression;
import com.decompiler.bytecode.analysis.parse.rewriters.AbstractExpressionRewriter;
import com.decompiler.bytecode.analysis.structured.StructuredScope;
import com.decompiler.bytecode.analysis.structured.StructuredStatement;
import com.decompiler.bytecode.analysis.structured.statement.StructuredAssignment;
import com.decompiler.bytecode.analysis.structured.statement.StructuredExpressionStatement;
import com.decompiler.bytecode.analysis.variables.VariableFactory;

public class InvalidExpressionStatementCleaner extends AbstractExpressionRewriter implements StructuredStatementTransformer {

    private VariableFactory variableFactory;

    public InvalidExpressionStatementCleaner(VariableFactory variableNamer) {
        this.variableFactory = variableNamer;
    }

    public void transform(Op04StructuredStatement root) {
        StructuredScope structuredScope = new StructuredScope();
        root.transform(this, structuredScope);
    }

    @Override
    public StructuredStatement transform(StructuredStatement in, StructuredScope scope) {
        in.transformStructuredChildren(this, scope);
        if (in instanceof StructuredExpressionStatement) {
            Expression exp = ((StructuredExpressionStatement) in).getExpression();
            if (!exp.isValidStatement()) {
                /* Have to assign to an ignored temporary, or discard.
                 * We prefer not to discard, as that involves detecting side effects
                 * and hides bytecode.
                 */
                return new StructuredAssignment(BytecodeLoc.TODO, variableFactory.ignoredVariable(exp.getInferredJavaType()), exp, true);
            }
        }
        return in;
    }
}

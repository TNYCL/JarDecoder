package com.decompiler.bytecode.analysis.opgraph.op4rewriters.checker;

import com.decompiler.bytecode.analysis.structured.StructuredScope;
import com.decompiler.bytecode.analysis.structured.StructuredStatement;
import com.decompiler.bytecode.analysis.structured.statement.StructuredDefinition;
import com.decompiler.bytecode.analysis.types.RawJavaType;
import com.decompiler.bytecode.analysis.types.discovery.InferredJavaType;
import com.decompiler.util.DecompilerComment;
import com.decompiler.util.DecompilerComments;

public class VoidVariableChecker implements Op04Checker {
    private boolean found = false;

    @Override
    public StructuredStatement transform(StructuredStatement in, StructuredScope scope) {
        if (found) return in;
        if (in instanceof StructuredDefinition) {
            InferredJavaType inferredJavaType = ((StructuredDefinition) in).getLvalue().getInferredJavaType();
            if (inferredJavaType != null && inferredJavaType.getJavaTypeInstance().getRawTypeOfSimpleType() == RawJavaType.VOID) {
                found = true;
                return in;
            }
        }

        in.transformStructuredChildren(this, scope);
        return in;
    }

    @Override
    public void commentInto(DecompilerComments comments) {
        if (found) {
            comments.addComment(DecompilerComment.VOID_DECLARATION);
        }
    }
}

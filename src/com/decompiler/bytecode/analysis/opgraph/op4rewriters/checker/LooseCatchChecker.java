package com.decompiler.bytecode.analysis.opgraph.op4rewriters.checker;

import com.decompiler.bytecode.analysis.structured.StructuredScope;
import com.decompiler.bytecode.analysis.structured.StructuredStatement;
import com.decompiler.bytecode.analysis.structured.statement.StructuredCatch;
import com.decompiler.bytecode.analysis.structured.statement.StructuredTry;
import com.decompiler.util.DecompilerComment;
import com.decompiler.util.DecompilerComments;

public class LooseCatchChecker implements Op04Checker {
    private boolean looseCatch = false;

    @Override
    public StructuredStatement transform(StructuredStatement in, StructuredScope scope) {
        if (looseCatch) return in;
        if (in instanceof StructuredCatch) {
            // Then we require the scope above this to be a try, otherwise it's an issue.
            StructuredStatement outer = scope.get(1);
            if (!(outer instanceof StructuredTry)) {
                looseCatch = true;
                return in;
            }
        }
        in.transformStructuredChildren(this, scope);
        return in;
    }

    @Override
    public void commentInto(DecompilerComments comments) {
        if (looseCatch) {
            comments.addComment(DecompilerComment.LOOSE_CATCH_BLOCK);
        }
    }
}

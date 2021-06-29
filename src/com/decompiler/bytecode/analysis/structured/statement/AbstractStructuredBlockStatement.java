package com.decompiler.bytecode.analysis.structured.statement;

import com.decompiler.bytecode.analysis.loc.BytecodeLoc;
import com.decompiler.bytecode.analysis.opgraph.Op04StructuredStatement;
import com.decompiler.bytecode.analysis.opgraph.op4rewriters.transformers.StructuredStatementTransformer;
import com.decompiler.bytecode.analysis.structured.StructuredScope;
import com.decompiler.state.TypeUsageCollector;

public abstract class AbstractStructuredBlockStatement extends AbstractStructuredStatement {
    private Op04StructuredStatement body;

    AbstractStructuredBlockStatement(BytecodeLoc loc, Op04StructuredStatement body) {
        super(loc);
        this.body = body;
    }

    public Op04StructuredStatement getBody() {
        return body;
    }

    @Override
    public boolean isRecursivelyStructured() {
        return body.isFullyStructured();
    }

    @Override
    public void transformStructuredChildren(StructuredStatementTransformer transformer, StructuredScope scope) {
        getBody().transform(transformer, scope);
    }

    @Override
    public void collectTypeUsages(TypeUsageCollector collector) {
        if (!collector.isStatementRecursive()) return;
        body.collectTypeUsages(collector);
    }
}

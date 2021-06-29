package com.decompiler.bytecode.analysis.structured.statement;

import java.util.List;

import com.decompiler.bytecode.analysis.loc.BytecodeLoc;
import com.decompiler.bytecode.analysis.opgraph.op4rewriters.matchutil.MatchIterator;
import com.decompiler.bytecode.analysis.opgraph.op4rewriters.matchutil.MatchResultCollector;
import com.decompiler.bytecode.analysis.opgraph.op4rewriters.transformers.StructuredStatementTransformer;
import com.decompiler.bytecode.analysis.parse.LValue;
import com.decompiler.bytecode.analysis.parse.lvalue.LocalVariable;
import com.decompiler.bytecode.analysis.parse.lvalue.SentinelLocalClassLValue;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriter;
import com.decompiler.bytecode.analysis.parse.utils.scope.LValueScopeDiscoverer;
import com.decompiler.bytecode.analysis.structured.StructuredScope;
import com.decompiler.bytecode.analysis.structured.StructuredStatement;
import com.decompiler.bytecode.analysis.types.JavaRefTypeInstance;
import com.decompiler.bytecode.analysis.types.JavaTypeInstance;
import com.decompiler.entities.ClassFile;
import com.decompiler.state.TypeUsageCollector;
import com.decompiler.util.collections.ListFactory;
import com.decompiler.util.output.Dumper;

public class StructuredDefinition extends AbstractStructuredStatement {

    private LValue scopedEntity;

    public StructuredDefinition(LValue scopedEntity) {
        super(BytecodeLoc.NONE);
        this.scopedEntity = scopedEntity;
    }

    @Override
    public void collectTypeUsages(TypeUsageCollector collector) {
        scopedEntity.collectTypeUsages(collector);
    }

    @Override
    public Dumper dump(Dumper dumper) {
        Class<?> clazz = scopedEntity.getClass();
        if (clazz == LocalVariable.class) {
            return LValue.Creation.dump(dumper, scopedEntity).endCodeln();
        } else if (clazz == SentinelLocalClassLValue.class) {
            JavaTypeInstance type = ((SentinelLocalClassLValue) scopedEntity).getLocalClassType().getDeGenerifiedType();
            if (type instanceof JavaRefTypeInstance) {
                ClassFile classFile = ((JavaRefTypeInstance) type).getClassFile();
                if (classFile != null) {
                    return classFile.dumpAsInlineClass(dumper);
                }
            }
        }
        return dumper;
    }

    @Override
    public BytecodeLoc getCombinedLoc() {
        return getLoc();
    }

    @Override
    public void transformStructuredChildren(StructuredStatementTransformer transformer, StructuredScope scope) {
    }

    @Override
    public void linearizeInto(List<StructuredStatement> out) {
        out.add(this);
    }

    @Override
    public void traceLocalVariableScope(LValueScopeDiscoverer scopeDiscoverer) {
    }

    public LValue getLvalue() {
        return scopedEntity;
    }

    @Override
    public List<LValue> findCreatedHere() {
        return ListFactory.newImmutableList(scopedEntity);
    }

    @Override
    public boolean match(MatchIterator<StructuredStatement> matchIterator, MatchResultCollector matchResultCollector) {
        StructuredStatement o = matchIterator.getCurrent();
        if (!this.equals(o)) return false;
        matchIterator.advance();
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o == null) return false;
        if (!(o instanceof StructuredDefinition)) return false;
        StructuredDefinition other = (StructuredDefinition) o;
        if (!scopedEntity.equals(other.scopedEntity)) return false;
        return true;
    }

    @Override
    public void rewriteExpressions(ExpressionRewriter expressionRewriter) {
    }

}


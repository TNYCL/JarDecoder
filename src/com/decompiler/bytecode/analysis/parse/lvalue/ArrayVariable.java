package com.decompiler.bytecode.analysis.parse.lvalue;

import com.decompiler.bytecode.analysis.parse.Expression;
import com.decompiler.bytecode.analysis.parse.LValue;
import com.decompiler.bytecode.analysis.parse.StatementContainer;
import com.decompiler.bytecode.analysis.parse.expression.ArrayIndex;
import com.decompiler.bytecode.analysis.parse.expression.misc.Precedence;
import com.decompiler.bytecode.analysis.parse.rewriters.CloneHelper;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriter;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriterFlags;
import com.decompiler.bytecode.analysis.parse.utils.*;
import com.decompiler.state.TypeUsageCollector;
import com.decompiler.util.ConfusedDecompilerException;
import com.decompiler.util.output.Dumper;

public class ArrayVariable extends AbstractLValue {

    private ArrayIndex arrayIndex;

    public ArrayVariable(ArrayIndex arrayIndex) {
        super(arrayIndex.getInferredJavaType());
        this.arrayIndex = arrayIndex;
    }

    @Override
    public void markFinal() {

    }

    @Override
    public boolean isFinal() {
        return false;
    }

    @Override
    public void markVar() {

    }

    @Override
    public boolean isVar() {
        return false;
    }

    @Override
    public boolean validIterator() {
        return false;
    }

    @Override
    public LValue deepClone(CloneHelper cloneHelper) {
        return new ArrayVariable((ArrayIndex) cloneHelper.replaceOrClone(arrayIndex));
    }

    @Override
    public void collectTypeUsages(TypeUsageCollector collector) {
        arrayIndex.collectTypeUsages(collector);
    }

    @Override
    public void collectLValueUsage(LValueUsageCollector lValueUsageCollector) {
        arrayIndex.collectUsedLValues(lValueUsageCollector);
    }

    @Override
    public boolean doesBlackListLValueReplacement(LValue replace, Expression with) {
        return arrayIndex.doesBlackListLValueReplacement(replace, with);
    }

    @Override
    public int getNumberOfCreators() {
        throw new ConfusedDecompilerException("NYI");
    }

    @Override
    public Precedence getPrecedence() {
        return Precedence.PAREN_SUB_MEMBER;
    }

    @Override
    public Dumper dumpInner(Dumper d) {
        return arrayIndex.dump(d);
    }

    public ArrayIndex getArrayIndex() {
        return arrayIndex;
    }

    @Override
    public void collectLValueAssignments(Expression assignedTo, StatementContainer statementContainer, LValueAssignmentCollector lValueAssigmentCollector) {
    }

    @Override
    public LValue replaceSingleUsageLValues(LValueRewriter lValueRewriter, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer) {
        arrayIndex = (ArrayIndex) arrayIndex.replaceSingleUsageLValues(lValueRewriter, ssaIdentifiers, statementContainer);
        return this;
    }

    @Override
    public LValue applyExpressionRewriter(ExpressionRewriter expressionRewriter, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer, ExpressionRewriterFlags flags) {
        // Note ,we say as rvalue, as we're not changing the ARRAY. (bit dodgy this).
        arrayIndex = (ArrayIndex) arrayIndex.applyExpressionRewriter(expressionRewriter, ssaIdentifiers, statementContainer, ExpressionRewriterFlags.RVALUE);
        return this;
    }

    @Override
    public SSAIdentifiers<LValue> collectVariableMutation(SSAIdentifierFactory<LValue, ?> ssaIdentifierFactory) {
        return new SSAIdentifiers<LValue>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArrayVariable)) return false;
        ArrayVariable other = (ArrayVariable) o;
        return arrayIndex.equals(other.arrayIndex);
    }
}

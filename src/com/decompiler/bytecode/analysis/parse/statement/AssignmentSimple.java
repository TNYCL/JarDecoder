package com.decompiler.bytecode.analysis.parse.statement;

import java.util.Collection;

import com.decompiler.bytecode.analysis.loc.BytecodeLoc;
import com.decompiler.bytecode.analysis.parse.Expression;
import com.decompiler.bytecode.analysis.parse.LValue;
import com.decompiler.bytecode.analysis.parse.Statement;
import com.decompiler.bytecode.analysis.parse.expression.AbstractAssignmentExpression;
import com.decompiler.bytecode.analysis.parse.expression.ArithOp;
import com.decompiler.bytecode.analysis.parse.expression.ArithmeticOperation;
import com.decompiler.bytecode.analysis.parse.expression.AssignmentExpression;
import com.decompiler.bytecode.analysis.parse.expression.CastExpression;
import com.decompiler.bytecode.analysis.parse.expression.LValueExpression;
import com.decompiler.bytecode.analysis.parse.expression.MemberFunctionInvokation;
import com.decompiler.bytecode.analysis.parse.rewriters.CloneHelper;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriter;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriterFlags;
import com.decompiler.bytecode.analysis.parse.utils.CreationCollector;
import com.decompiler.bytecode.analysis.parse.utils.EquivalenceConstraint;
import com.decompiler.bytecode.analysis.parse.utils.LValueAssignmentCollector;
import com.decompiler.bytecode.analysis.parse.utils.LValueRewriter;
import com.decompiler.bytecode.analysis.parse.utils.LValueUsageCollector;
import com.decompiler.bytecode.analysis.parse.utils.LValueUsageCollectorSimple;
import com.decompiler.bytecode.analysis.parse.utils.ReadWrite;
import com.decompiler.bytecode.analysis.parse.utils.SSAIdentifierFactory;
import com.decompiler.bytecode.analysis.parse.utils.SSAIdentifiers;
import com.decompiler.bytecode.analysis.structured.StructuredStatement;
import com.decompiler.bytecode.analysis.structured.statement.StructuredAssignment;
import com.decompiler.entities.exceptions.ExceptionCheck;
import com.decompiler.util.output.Dumper;

public class AssignmentSimple extends AbstractAssignment {
    private LValue lvalue;
    private Expression rvalue;

    public AssignmentSimple(BytecodeLoc loc, LValue lvalue, Expression rvalue) {
        super(loc);
        this.lvalue = lvalue;
        this.rvalue = lvalue.getInferredJavaType().chain(rvalue.getInferredJavaType()).performCastAction(rvalue, lvalue.getInferredJavaType());
    }

    @Override
    public Dumper dump(Dumper d) {
        return d.dump(lvalue).operator(" = ").dump(rvalue).endCodeln();
    }

    @Override
    public BytecodeLoc getCombinedLoc() {
        return BytecodeLoc.combine(this, rvalue);
    }

    @Override
    public Statement deepClone(CloneHelper cloneHelper) {
        return new AssignmentSimple(getLoc(), cloneHelper.replaceOrClone(lvalue), cloneHelper.replaceOrClone(rvalue));
    }

    @Override
    public void collectLValueAssignments(LValueAssignmentCollector<Statement> lValueAssigmentCollector) {
        lvalue.collectLValueAssignments(rvalue, this.getContainer(), lValueAssigmentCollector);
    }

    @Override
    public boolean doesBlackListLValueReplacement(LValue lValue, Expression expression) {
        return lvalue.doesBlackListLValueReplacement(lValue, expression);
    }

    @Override
    public void collectLValueUsage(LValueUsageCollector lValueUsageCollector) {
        lValueUsageCollector.collect(lvalue, ReadWrite.WRITE);
        rvalue.collectUsedLValues(lValueUsageCollector);
    }

    @Override
    public void collectObjectCreation(CreationCollector creationCollector) {
        creationCollector.collectCreation(lvalue, rvalue, this.getContainer());
    }

    @Override
    public SSAIdentifiers<LValue> collectLocallyMutatedVariables(SSAIdentifierFactory<LValue, ?> ssaIdentifierFactory) {
        return lvalue.collectVariableMutation(ssaIdentifierFactory);
    }

    @Override
    public LValue getCreatedLValue() {
        return lvalue;
    }

    @Override
    public Expression getRValue() {
        return rvalue;
    }

    public void setRValue(Expression rvalue) {
        this.rvalue = rvalue;
    }

    /* We /should/ be using assignmentPreChange here, but if that has been disabled, these
         * assignments should be able to stand in.
         *
         * This (should) also catch self member calls?
         */
    @Override
    public boolean isSelfMutatingOperation() {
        Expression localR = rvalue;
        while (localR instanceof CastExpression) localR = ((CastExpression) localR).getChild();
        if (localR instanceof ArithmeticOperation) {
            ArithmeticOperation arithmeticOperation = (ArithmeticOperation) localR;
            return arithmeticOperation.isLiteralFunctionOf(lvalue);
        } else if (localR instanceof MemberFunctionInvokation) {
            MemberFunctionInvokation memberFunctionInvokation = (MemberFunctionInvokation)localR;
            Expression object = memberFunctionInvokation.getObject();
            if (object instanceof LValueExpression) {
                LValue memberLValue = ((LValueExpression) object).getLValue();
                return memberLValue.equals(lvalue);
            }
        }
        return false;
    }

    @Override
    public boolean isSelfMutatingOp1(LValue lValue, ArithOp arithOp) {
        return false;
/*        if (!lValue.equals(this.lvalue)) return false;
        WildcardMatch wildcardMatch = new WildcardMatch();

        return wildcardMatch.match(
                new ArithmeticOperation(
                        new LValueExpression(lValue),
                        new Literal(TypedLiteral.getInt(1)),
                        arithOp), rvalue);
                        */
    }

    @Override
    public Expression getPostMutation() {
        throw new IllegalStateException();
    }

    @Override
    public Expression getPreMutation() {
        throw new IllegalStateException();
    }

    @Override
    public AbstractAssignmentExpression getInliningExpression() {
        return new AssignmentExpression(getLoc(), getCreatedLValue(), getRValue());
    }

    @Override
    public void replaceSingleUsageLValues(LValueRewriter lValueRewriter, SSAIdentifiers ssaIdentifiers) {
        lvalue = lvalue.replaceSingleUsageLValues(lValueRewriter, ssaIdentifiers, getContainer());
        LValueUsageCollectorSimple tmp = new LValueUsageCollectorSimple();
        lvalue.collectLValueUsage(tmp);
        Collection<LValue> usedLValues = tmp.getUsedLValues();
        if (!usedLValues.isEmpty()) {
            lValueRewriter = lValueRewriter.keepConstant(usedLValues);
        }
        rvalue = rvalue.replaceSingleUsageLValues(lValueRewriter, ssaIdentifiers, getContainer());
        // We need to make sure that we haven't violated any preconditions with a rewrite.
        lValueRewriter.checkPostConditions(lvalue, rvalue);
    }

    @Override
    public void rewriteExpressions(ExpressionRewriter expressionRewriter, SSAIdentifiers ssaIdentifiers) {
        lvalue = expressionRewriter.rewriteExpression(lvalue, ssaIdentifiers, getContainer(), ExpressionRewriterFlags.LVALUE);
        rvalue = expressionRewriter.rewriteExpression(rvalue, ssaIdentifiers, getContainer(), ExpressionRewriterFlags.RVALUE);
    }

    @Override
    public StructuredStatement getStructuredStatement() {
        return new StructuredAssignment(getLoc(), lvalue, rvalue);
    }

    @Override
    public boolean canThrow(ExceptionCheck caught) {
        return lvalue.canThrow(caught) || rvalue.canThrow(caught);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof AssignmentSimple)) return false;

        AssignmentSimple other = (AssignmentSimple) o;
        return lvalue.equals(other.lvalue) && rvalue.equals(other.rvalue);
    }

    @Override
    public final boolean equivalentUnder(Object o, EquivalenceConstraint constraint) {
        if (o == null) return false;
        if (o == this) return true;
        if (getClass() != o.getClass()) return false;
        AssignmentSimple other = (AssignmentSimple) o;
        if (!constraint.equivalent(lvalue, other.lvalue)) return false;
        if (!constraint.equivalent(rvalue, other.rvalue)) return false;
        return true;
    }
}

package com.decompiler.bytecode.analysis.parse.statement;

import java.util.List;

import com.decompiler.bytecode.analysis.loc.BytecodeLoc;
import com.decompiler.bytecode.analysis.parse.Expression;
import com.decompiler.bytecode.analysis.parse.Statement;
import com.decompiler.bytecode.analysis.parse.expression.AbstractAssignmentExpression;
import com.decompiler.bytecode.analysis.parse.expression.ConditionalExpression;
import com.decompiler.bytecode.analysis.parse.rewriters.CloneHelper;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriter;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriterFlags;
import com.decompiler.bytecode.analysis.parse.utils.*;
import com.decompiler.bytecode.analysis.structured.StructuredStatement;
import com.decompiler.bytecode.analysis.structured.statement.UnstructuredWhile;
import com.decompiler.util.ConfusedDecompilerException;
import com.decompiler.util.output.Dumper;

public class WhileStatement extends AbstractStatement {
    private ConditionalExpression condition;
    private BlockIdentifier blockIdentifier;

    public WhileStatement(BytecodeLoc loc, ConditionalExpression conditionalExpression, BlockIdentifier blockIdentifier) {
        super(loc);
        this.condition = conditionalExpression;
        this.blockIdentifier = blockIdentifier;
    }

    @Override
    public BytecodeLoc getCombinedLoc() {
        return BytecodeLoc.combine(this, condition);
    }

    @Override
    public Statement deepClone(CloneHelper cloneHelper) {
        return new WhileStatement(getLoc(), (ConditionalExpression)cloneHelper.replaceOrClone(condition), blockIdentifier);
    }

    private int getBackJumpIndex() {
        return condition == null ? 0 : 1;
    }

    @Override
    public Dumper dump(Dumper dumper) {
        dumper.print("while (");
        if (condition == null) {
            dumper.print("true");
        } else {
            dumper.dump(condition);
        }
        dumper.print(") ");
        dumper.print(" // ends " + getTargetStatement(getBackJumpIndex()).getContainer().getLabel() + ";").newln();
        return dumper;
    }

    public void replaceWithForLoop(AssignmentSimple initial, List<AbstractAssignmentExpression> assignment) {
        if (condition == null) {
            throw new UnsupportedOperationException();
        }
        ForStatement forStatement = new ForStatement(getLoc(), condition, blockIdentifier, initial, assignment);
        getContainer().replaceStatement(forStatement);
    }

    @Override
    public void replaceSingleUsageLValues(LValueRewriter lValueRewriter, SSAIdentifiers ssaIdentifiers) {
        if (condition == null) return;
        Expression replacementCondition = condition.replaceSingleUsageLValues(lValueRewriter, ssaIdentifiers, getContainer());
        if (replacementCondition != condition) throw new ConfusedDecompilerException("Can't yet support replacing conditions");
    }

    @Override
    public void rewriteExpressions(ExpressionRewriter expressionRewriter, SSAIdentifiers ssaIdentifiers) {
        if (condition == null) return;
        condition = expressionRewriter.rewriteExpression(condition, ssaIdentifiers, getContainer(), ExpressionRewriterFlags.RVALUE);
    }

    @Override
    public void collectLValueUsage(LValueUsageCollector lValueUsageCollector) {
        if (condition != null) condition.collectUsedLValues(lValueUsageCollector);
    }

    @Override
    public StructuredStatement getStructuredStatement() {
        return new UnstructuredWhile(getLoc(), condition, blockIdentifier, getTargetStatement(getBackJumpIndex()).getContainer().getBlocksEnded());
    }

    public BlockIdentifier getBlockIdentifier() {
        return blockIdentifier;
    }

    public ConditionalExpression getCondition() {
        return condition;
    }

    @Override
    public final boolean equivalentUnder(Object o, EquivalenceConstraint constraint) {
        if (o == null) return false;
        if (o == this) return true;
        if (getClass() != o.getClass()) return false;
        WhileStatement other = (WhileStatement) o;
        if (!constraint.equivalent(condition, other.condition)) return false;
        return true;
    }

}

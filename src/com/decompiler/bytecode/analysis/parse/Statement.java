package com.decompiler.bytecode.analysis.parse;

import java.util.List;
import java.util.Set;

import com.decompiler.bytecode.analysis.loc.BytecodeLoc;
import com.decompiler.bytecode.analysis.loc.HasByteCodeLoc;
import com.decompiler.bytecode.analysis.parse.rewriters.DeepCloneable;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriter;
import com.decompiler.bytecode.analysis.parse.utils.*;
import com.decompiler.bytecode.analysis.structured.StructuredStatement;
import com.decompiler.entities.exceptions.ExceptionCheck;
import com.decompiler.util.output.Dumpable;

/*
 * statement =
 * 
 *   assignment 
 *   if (condition) statement  [ else statement
 *   { list<statement> }
 *   label
 *   goto label
 */
public interface Statement extends Dumpable, ComparableUnderEC, DeepCloneable<Statement>, HasByteCodeLoc {
    void setContainer(StatementContainer<Statement> container);

    void collectLValueAssignments(LValueAssignmentCollector<Statement> lValueAssigmentCollector);

    void collectLValueUsage(LValueUsageCollector lValueUsageCollector);

    boolean doesBlackListLValueReplacement(LValue lValue, Expression expression);

    void replaceSingleUsageLValues(LValueRewriter lValueRewriter, SSAIdentifiers ssaIdentifiers);

    void rewriteExpressions(ExpressionRewriter expressionRewriter, SSAIdentifiers ssaIdentifiers);

    void collectObjectCreation(CreationCollector creationCollector);

    SSAIdentifiers<LValue> collectLocallyMutatedVariables(SSAIdentifierFactory<LValue, ?> ssaIdentifierFactory);

    boolean isCompound();

    // Valid to call on everything, only useful on an assignment.
    LValue getCreatedLValue();

    // Only sensible to call on an assignment
    Expression getRValue();

    StatementContainer<Statement> getContainer();

    List<Statement> getCompoundParts();

    StructuredStatement getStructuredStatement();

    boolean equivalentUnder(Object o, EquivalenceConstraint constraint);

    boolean fallsToNext();

    boolean canThrow(ExceptionCheck caught);

    Set<LValue> wantsLifetimeHint();

    void setLifetimeHint(LValue lv, boolean usedInChildren);
}

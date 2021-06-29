package com.decompiler.bytecode.analysis.parse;

import java.util.Map;

import com.decompiler.bytecode.analysis.loc.HasByteCodeLoc;
import com.decompiler.bytecode.analysis.parse.expression.Literal;
import com.decompiler.bytecode.analysis.parse.rewriters.DeepCloneable;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriter;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriterFlags;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionVisitor;
import com.decompiler.bytecode.analysis.parse.utils.*;
import com.decompiler.bytecode.analysis.types.discovery.InferredJavaType;
import com.decompiler.entities.exceptions.ExceptionCheck;
import com.decompiler.util.TypeUsageCollectable;
import com.decompiler.util.output.DumpableWithPrecedence;
import com.decompiler.util.output.Dumper;

public interface Expression extends DumpableWithPrecedence, DeepCloneable<Expression>, ComparableUnderEC, TypeUsageCollectable, HasByteCodeLoc {
    // Can /PROBABLY/ replace LValueRewriter with expression rewriter.
    Expression replaceSingleUsageLValues(LValueRewriter lValueRewriter, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer);

    Expression applyExpressionRewriter(ExpressionRewriter expressionRewriter, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer, ExpressionRewriterFlags flags);

    Expression applyReverseExpressionRewriter(ExpressionRewriter expressionRewriter, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer, ExpressionRewriterFlags flags);

    boolean isSimple();

    void collectUsedLValues(LValueUsageCollector lValueUsageCollector);

    boolean canPushDownInto();

    Expression pushDown(Expression toPush, Expression parent);

    InferredJavaType getInferredJavaType();

    boolean equivalentUnder(Object o, EquivalenceConstraint constraint);

    boolean canThrow(ExceptionCheck caught);

    // If this expression has any side effects, other than updating stackVar/locals it MUST return null, regardless.
    Literal getComputedLiteral(Map<LValue, Literal> display);

    boolean isValidStatement();

    <T> T visit(ExpressionVisitor<T> visitor);

    @Override
    Dumper dump(Dumper d);

}

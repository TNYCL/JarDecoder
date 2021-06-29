package com.decompiler.bytecode.analysis.opgraph.op4rewriters.transformers;

import java.util.List;

import com.decompiler.bytecode.analysis.loc.BytecodeLoc;
import com.decompiler.bytecode.analysis.opgraph.Op04StructuredStatement;
import com.decompiler.bytecode.analysis.parse.Expression;
import com.decompiler.bytecode.analysis.parse.expression.BoolOp;
import com.decompiler.bytecode.analysis.parse.expression.BooleanExpression;
import com.decompiler.bytecode.analysis.parse.expression.BooleanOperation;
import com.decompiler.bytecode.analysis.parse.expression.ConditionalExpression;
import com.decompiler.bytecode.analysis.parse.expression.LValueExpression;
import com.decompiler.bytecode.analysis.parse.expression.NotOperation;
import com.decompiler.bytecode.analysis.parse.lvalue.StaticVariable;
import com.decompiler.bytecode.analysis.parse.utils.BlockIdentifier;
import com.decompiler.bytecode.analysis.parse.wildcard.WildcardMatch;
import com.decompiler.bytecode.analysis.structured.StructuredScope;
import com.decompiler.bytecode.analysis.structured.StructuredStatement;
import com.decompiler.bytecode.analysis.structured.statement.AbstractStructuredBlockStatement;
import com.decompiler.bytecode.analysis.structured.statement.Block;
import com.decompiler.bytecode.analysis.structured.statement.StructuredDo;
import com.decompiler.bytecode.analysis.structured.statement.StructuredIf;
import com.decompiler.bytecode.analysis.structured.statement.StructuredThrow;
import com.decompiler.bytecode.analysis.structured.statement.StructuredWhile;
import com.decompiler.bytecode.analysis.types.TypeConstants;
import com.decompiler.util.collections.ListFactory;

/*
 * if (X && !assertsDisabled .... ) {
 * } NO ELSE BRANCH
 * ->
 *
 * if (X) {
 *   if (!assertsDisabled ...) {
 *   }
 * }
 *
 * Note, due to demorgan, we may see
 *
 * if (!(!X || assertsDisabled....
 *
 * If that's the case, run the predicate through remorganification ;)
 */
public class PreconditionAssertRewriter implements StructuredStatementTransformer
{
    private Expression test;

    public PreconditionAssertRewriter(StaticVariable assertionStatic) {
        this.test = new NotOperation(BytecodeLoc.NONE, new BooleanExpression(new LValueExpression(assertionStatic)));
    }


    public void transform(Op04StructuredStatement root) {
        StructuredScope structuredScope = new StructuredScope();
        root.transform(this, structuredScope);
    }

    @Override
    public StructuredStatement transform(StructuredStatement in, StructuredScope scope) {
        in.transformStructuredChildren(this, scope);

        if (in instanceof StructuredIf) {
            in = transformAssertIf((StructuredIf)in);
        }
        return in;
    }

    private StructuredStatement transformAssertIf(StructuredIf in) {
        if (in.hasElseBlock()) return in;
        ConditionalExpression expression = in.getConditionalExpression();
        if (expression instanceof NotOperation) {
            expression = expression.getDemorganApplied(false);
        }
        List<ConditionalExpression> cnf = getFlattenedCNF(expression);
        if (cnf.size() < 2) return in;
        for (int x=0;x<cnf.size();++x) {
            if (test.equals(cnf.get(x))) {
                if (x==0) return in;
                ConditionalExpression c1 = BooleanOperation.makeRightDeep(cnf.subList(0,x), BoolOp.AND);
                ConditionalExpression c2 = BooleanOperation.makeRightDeep(cnf.subList(x, cnf.size()), BoolOp.AND);
                return new StructuredIf(BytecodeLoc.TODO, c1,
                        new Op04StructuredStatement(new StructuredIf(
                                BytecodeLoc.TODO,
                                c2,
                                in.getIfTaken())));
            }
        }
        return in;
    }

    private List<ConditionalExpression> getFlattenedCNF(ConditionalExpression ce) {
        List<ConditionalExpression> accum = ListFactory.newList();
        getFlattenedCNF(ce, accum);
        return accum;
    }

    private void getFlattenedCNF(ConditionalExpression ce, List<ConditionalExpression> accum) {
        if (ce instanceof BooleanOperation) {
            BooleanOperation bo = (BooleanOperation)ce;
            if (bo.getOp() == BoolOp.AND) {
                getFlattenedCNF(bo.getLhs(), accum);
                getFlattenedCNF(bo.getRhs(), accum);
                return;
            }
        }
        accum.add(ce);
    }
}

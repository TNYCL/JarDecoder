package com.decompiler.bytecode.analysis.structured.statement;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import com.decompiler.bytecode.analysis.loc.BytecodeLoc;
import com.decompiler.bytecode.analysis.opgraph.Op04StructuredStatement;
import com.decompiler.bytecode.analysis.parse.expression.ConditionalExpression;
import com.decompiler.bytecode.analysis.parse.utils.BlockIdentifier;
import com.decompiler.bytecode.analysis.structured.StructuredStatement;
import com.decompiler.state.TypeUsageCollector;
import com.decompiler.util.Optional;
import com.decompiler.util.collections.ListFactory;
import com.decompiler.util.output.Dumper;

public class UnstructuredDo extends AbstractUnStructuredStatement {
    private BlockIdentifier blockIdentifier;

    public UnstructuredDo(BlockIdentifier blockIdentifier) {
        super(BytecodeLoc.NONE);
        this.blockIdentifier = blockIdentifier;
    }

    @Override
    public BytecodeLoc getCombinedLoc() {
        return getLoc();
    }

    @Override
    public Dumper dump(Dumper dumper) {
        return dumper.print("** do ").newln();
    }

    @Override
    public void collectTypeUsages(TypeUsageCollector collector) {
    }

    @Override
    public StructuredStatement claimBlock(Op04StructuredStatement innerBlock, BlockIdentifier blockIdentifier, Vector<BlockIdentifier> blocksCurrentlyIn) {
        if (blockIdentifier != this.blockIdentifier) {
            throw new RuntimeException("Do statement claiming wrong block");
        }
        UnstructuredWhile lastEndWhile = innerBlock.removeLastEndWhile();
        if (lastEndWhile != null) {
            ConditionalExpression condition = lastEndWhile.getCondition();
            return StructuredDo.create(condition, innerBlock, blockIdentifier);
        }

        /*
         * If there were any ways of legitimately hitting the exit, we need a break.  If not, we don't.
         * do always points to while so it's not orphaned, so we're checking for > 1 parent.
         *
         * need to transform
         * do {
         * } ???
         *    ->
         * do {
         *  ...
         *  break;
         * } while (true);
         */
        /*
         * But - if the inner statement is simply a single statement, and not a break FROM this block,
         * (or a continue of it), we can just drop the loop completely.
         */

        StructuredStatement inner = innerBlock.getStatement();
        if (!(inner instanceof Block)) {
            LinkedList<Op04StructuredStatement> blockContent = ListFactory.newLinkedList();
            blockContent.add(new Op04StructuredStatement(inner));
            inner = new Block(blockContent, true);
            innerBlock.replaceStatement(inner);
        }
        Block block = (Block) inner;
        Optional<Op04StructuredStatement> maybeStatement = block.getMaybeJustOneStatement();
        if (maybeStatement.isSet()) {
            Op04StructuredStatement singleStatement = maybeStatement.getValue();
            StructuredStatement stm = singleStatement.getStatement();
            boolean canRemove = true;
            if (stm instanceof StructuredBreak) {
                StructuredBreak brk = (StructuredBreak) stm;
                if (brk.getBreakBlock().equals(blockIdentifier)) canRemove = false;
            } else if (stm instanceof StructuredContinue) {
                StructuredContinue cnt = (StructuredContinue) stm;
                if (cnt.getContinueTgt().equals(blockIdentifier)) canRemove = false;
            } else if (stm.canFall()) {
                canRemove = false;
            }
            if (canRemove) {
                return stm;
            }
        }
        Op04StructuredStatement last = block.getLast();
        if (last != null) {
            if (last.getStatement().canFall()) {
                block.addStatement(new Op04StructuredStatement(new StructuredBreak(getLoc(), blockIdentifier, true)));
            }
        }
        return StructuredDo.create(null, innerBlock, blockIdentifier);
    }
}

package com.decompiler.bytecode.analysis.structured.statement;

import java.util.List;

import com.decompiler.bytecode.analysis.loc.BytecodeLoc;
import com.decompiler.bytecode.analysis.opgraph.Op04StructuredStatement;
import com.decompiler.bytecode.analysis.opgraph.op4rewriters.matchutil.MatchIterator;
import com.decompiler.bytecode.analysis.opgraph.op4rewriters.matchutil.MatchResultCollector;
import com.decompiler.bytecode.analysis.parse.expression.ConditionalExpression;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriter;
import com.decompiler.bytecode.analysis.parse.utils.BlockIdentifier;
import com.decompiler.bytecode.analysis.parse.utils.scope.LValueScopeDiscoverer;
import com.decompiler.bytecode.analysis.structured.StructuredStatement;
import com.decompiler.state.TypeUsageCollector;
import com.decompiler.util.output.Dumper;

public class StructuredWhile extends AbstractStructuredConditionalLoopStatement {
    public StructuredWhile(ConditionalExpression condition, Op04StructuredStatement body, BlockIdentifier block) {
        super(BytecodeLoc.NONE, condition, block, body);
    }

    @Override
    public BytecodeLoc getCombinedLoc() {
        return BytecodeLoc.combine(this, getCondition());
    }

    @Override
    public Dumper dump(Dumper dumper) {
        if (block.hasForeignReferences()) dumper.label(block.getName(), true);
        dumper.print("while (");
        if (condition == null) {
            dumper.print("true");
        } else {
            dumper.dump(condition);
        }
        dumper.print(") ");
        getBody().dump(dumper);
        return dumper;
    }

    @Override
    public boolean match(MatchIterator<StructuredStatement> matchIterator, MatchResultCollector matchResultCollector) {
        StructuredStatement o = matchIterator.getCurrent();
        if (!(o instanceof StructuredWhile)) return false;
        StructuredWhile other = (StructuredWhile) o;
        if (condition == null) {
            if (other.condition != null) return false;
        } else {
            if (!condition.equals(other.condition)) return false;
        }
        if (!block.equals(other.block)) return false;
        // Don't check locality.
        matchIterator.advance();
        return true;
    }
}

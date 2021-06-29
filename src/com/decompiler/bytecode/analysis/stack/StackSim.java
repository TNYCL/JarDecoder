package com.decompiler.bytecode.analysis.stack;

import java.util.List;

import com.decompiler.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs;
import com.decompiler.bytecode.analysis.types.StackType;
import com.decompiler.bytecode.analysis.types.StackTypes;
import com.decompiler.util.ConfusedDecompilerException;
import com.decompiler.util.collections.ListFactory;

public class StackSim {
    private final StackSim parent;
    private final StackEntryHolder stackEntryHolder;
    private final long depth;

    public StackSim() {
        this.depth = 0;
        this.parent = null;
        this.stackEntryHolder = null;
    }

    private StackSim(StackSim parent, StackType stackType) {
        this.parent = parent;
        this.depth = parent.depth + 1;
        this.stackEntryHolder = new StackEntryHolder(stackType);
    }

    public StackEntry getEntry(int depth) {
        StackSim thisSim = this;
        while (depth > 0) {
            thisSim = thisSim.getParent();
            depth--;
        }
        if (thisSim.stackEntryHolder == null) {
            throw new ConfusedDecompilerException("Underrun type stack");
        }
        return thisSim.stackEntryHolder.getStackEntry();
    }

    public List<StackEntryHolder> getHolders(int offset, long num) {
        StackSim thisSim = this;
        List<StackEntryHolder> res = ListFactory.newList();
        while (num > 0) {
            if (offset > 0) {
                offset--;
            } else {
                res.add(thisSim.stackEntryHolder);
                num--;
            }
            thisSim = thisSim.getParent();
        }
        return res;
    }

    public long getDepth() {
        return depth;
    }

    public StackSim getChange(StackDelta delta, List<StackEntryHolder> consumed, List<StackEntryHolder> produced, Op02WithProcessedDataAndRefs instruction) {
        if (delta.isNoOp()) {
            return this;
        }
        try {
            StackSim thisSim = this;
            StackTypes consumedStack = delta.getConsumed();
            for (StackType stackType : consumedStack) {
                consumed.add(thisSim.stackEntryHolder);
                thisSim = thisSim.getParent();
            }
            StackTypes producedStack = delta.getProduced();
            for (int x = producedStack.size() - 1; x >= 0; --x) {
                thisSim = new StackSim(thisSim, producedStack.get(x));
            }
            StackSim thatSim = thisSim;
            for (StackType stackType : producedStack) {
                produced.add(thatSim.stackEntryHolder);
                thatSim = thatSim.getParent();
            }
            return thisSim;
        } catch (ConfusedDecompilerException e) {
            throw new ConfusedDecompilerException("While processing " + instruction + " : " + e.getMessage());
        }
    }

    private StackSim getParent() {
        if (parent == null) {
            throw new ConfusedDecompilerException("Stack underflow");
        }
        return parent;
    }

    @Override
    public String toString() {
        StackSim next = this;
        StringBuilder sb = new StringBuilder();
        while (next != null) {
            if (next.stackEntryHolder == null) break;
            StackEntry stackEntry = next.stackEntryHolder.getStackEntry();
            sb.append(stackEntry).append('[').append(stackEntry.getType()).append("] ");
            next = next.parent;
        }
        return sb.toString();
    }
}

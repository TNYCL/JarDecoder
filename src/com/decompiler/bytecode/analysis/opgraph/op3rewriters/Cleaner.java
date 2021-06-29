package com.decompiler.bytecode.analysis.opgraph.op3rewriters;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.decompiler.bytecode.analysis.opgraph.InstrIndex;
import com.decompiler.bytecode.analysis.opgraph.Op03SimpleStatement;
import com.decompiler.bytecode.analysis.parse.Statement;
import com.decompiler.bytecode.analysis.parse.statement.JumpingStatement;
import com.decompiler.bytecode.analysis.parse.statement.WhileStatement;
import com.decompiler.util.collections.ListFactory;
import com.decompiler.util.collections.SetFactory;
import com.decompiler.util.functors.BinaryProcedure;
import com.decompiler.util.graph.GraphVisitor;
import com.decompiler.util.graph.GraphVisitorDFS;

public class Cleaner {
    public static List<Op03SimpleStatement> removeUnreachableCode(final List<Op03SimpleStatement> statements, final boolean checkBackJumps) {
        final Set<Op03SimpleStatement> reachable = SetFactory.newSet();
        reachable.add(statements.get(0));
        GraphVisitor<Op03SimpleStatement> gv = new GraphVisitorDFS<Op03SimpleStatement>(statements.get(0), new BinaryProcedure<Op03SimpleStatement, GraphVisitor<Op03SimpleStatement>>() {
            @Override
            public void call(Op03SimpleStatement arg1, GraphVisitor<Op03SimpleStatement> arg2) {
                reachable.add(arg1);
//                if (!statements.contains(arg1)) {
//                    throw new IllegalStateException("Statement missing");
//                }
                arg2.enqueue(arg1.getTargets());
                for (Op03SimpleStatement source : arg1.getSources()) {
//                    if (!statements.contains(source)) {
//                        throw new IllegalStateException("Source not in graph!");
//                    }
                    if (!source.getTargets().contains(arg1)) {
                        throw new IllegalStateException("Inconsistent graph " + source + " does not have a target of " + arg1);
                    }
                }
                for (Op03SimpleStatement test : arg1.getTargets()) {
                    // Also, check for backjump targets on non jumps.
                    Statement argContained = arg1.getStatement();
                    if (checkBackJumps) {
                        if (!(argContained instanceof JumpingStatement || argContained instanceof WhileStatement)) {
                            if (test.getIndex().isBackJumpFrom(arg1)) {
                                throw new IllegalStateException("Backjump on non jumping statement " + arg1);
                            }
                        }
                    }
                    if (!test.getSources().contains(arg1)) {
                        throw new IllegalStateException("Inconsistent graph " + test + " does not have a source " + arg1);
                    }
                }
            }
        });
        gv.process();

        List<Op03SimpleStatement> result = ListFactory.newList();
        for (Op03SimpleStatement statement : statements) {
            if (reachable.contains(statement)) {
                result.add(statement);
            }
        }
        // Too expensive....
        for (Op03SimpleStatement res1 : result) {
            List<Op03SimpleStatement> sources = ListFactory.newList(res1.getSources());
            for (Op03SimpleStatement source : sources) {
                if (!reachable.contains(source)) {
                    res1.removeSource(source);
                }
            }
        }
        return result;
    }

    /*
* Filter out nops (where appropriate) and renumber.  For display purposes.
*/
    public static List<Op03SimpleStatement> sortAndRenumber(List<Op03SimpleStatement> statements) {
        boolean nonNopSeen = false;
        List<Op03SimpleStatement> result = ListFactory.newList();
        for (Op03SimpleStatement statement : statements) {
            boolean thisIsNop = statement.isAgreedNop();
            if (!nonNopSeen) {
                result.add(statement);
                if (!thisIsNop) nonNopSeen = true;
            } else {
                if (!thisIsNop) {
                    result.add(statement);
                }
            }
        }
        // Sort result by existing index.
        sortAndRenumberInPlace(result);
        return result;
    }

    static void sortAndRenumberFromInPlace(List<Op03SimpleStatement> statements, InstrIndex start) {
        Collections.sort(statements, new CompareByIndex());
        for (Op03SimpleStatement statement : statements) {
            statement.setIndex(start);
            start = start.justAfter();
        }
    }

    static void sortAndRenumberInPlace(List<Op03SimpleStatement> statements) {
        // Sort result by existing index.
        Collections.sort(statements, new CompareByIndex());
        reindexInPlace(statements);
    }

    public static void reindexInPlace(List<Op03SimpleStatement> statements) {
        int newIndex = 0;
        Op03SimpleStatement prev = null;
        for (Op03SimpleStatement statement : statements) {
            statement.setLinearlyPrevious(prev);
            statement.setLinearlyNext(null);
            if (prev != null) prev.setLinearlyNext(statement);
            statement.setIndex(new InstrIndex(newIndex++));
            prev = statement;
        }
    }

    public static void reLinkInPlace(List<Op03SimpleStatement> statements) {
        Op03SimpleStatement prev = null;
        for (Op03SimpleStatement statement : statements) {
            statement.setLinearlyPrevious(prev);
            statement.setLinearlyNext(null);
            if (prev != null) prev.setLinearlyNext(statement);
            prev = statement;
        }
    }
}

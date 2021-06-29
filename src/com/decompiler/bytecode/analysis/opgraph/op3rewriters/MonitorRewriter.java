package com.decompiler.bytecode.analysis.opgraph.op3rewriters;

import java.util.List;
import java.util.Set;

import com.decompiler.bytecode.analysis.opgraph.Op03SimpleStatement;
import com.decompiler.bytecode.analysis.parse.statement.CommentStatement;
import com.decompiler.bytecode.analysis.parse.statement.MonitorStatement;
import com.decompiler.bytecode.analysis.parse.utils.BlockIdentifier;
import com.decompiler.util.collections.Functional;
import com.decompiler.util.collections.ListFactory;
import com.decompiler.util.collections.SetFactory;

public class MonitorRewriter {
    public static void commentMonitors(List<Op03SimpleStatement> statements) {
        List<Op03SimpleStatement> monitors = Functional.filter(statements, new TypeFilter<MonitorStatement>(MonitorStatement.class));
        if (monitors.isEmpty()) return;
        for (Op03SimpleStatement monitor : monitors) {
            monitor.replaceStatement(new CommentStatement(monitor.getStatement()));
        }
        /*
         * Any jumps to one of these statements which jump into the MIDDLE of a block is a problem.  If we can jump to
         * after this statement and NOT be in the middle of a block, prefer that.
         * [This is very much a heuristic required by dex2jar]
         */
        for (Op03SimpleStatement monitor : monitors) {
            /*
             * Is monitor (as was) the last statement in a block.
             */
            Op03SimpleStatement target = monitor.getTargets().get(0);
            Set<BlockIdentifier> monitorLast = SetFactory.newSet(monitor.getBlockIdentifiers());
            monitorLast.removeAll(target.getBlockIdentifiers());
            if (monitorLast.isEmpty()) continue;
            for (Op03SimpleStatement source : ListFactory.newList(monitor.getSources())) {
                Set<BlockIdentifier> sourceBlocks = source.getBlockIdentifiers();
                if (!sourceBlocks.containsAll(monitorLast)) {
                    /*
                     * Let's redirect source to point to AFTER monitor statement.
                     */
                    source.replaceTarget(monitor, target);
                    monitor.removeSource(source);
                    target.addSource(source);
                }
            }
        }
    }

}

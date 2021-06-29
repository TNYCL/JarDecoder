package com.decompiler.bytecode.analysis.opgraph.op3rewriters;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.decompiler.bytecode.analysis.opgraph.Op03SimpleStatement;
import com.decompiler.bytecode.analysis.parse.Statement;
import com.decompiler.bytecode.analysis.parse.statement.TryStatement;
import com.decompiler.bytecode.analysis.parse.utils.BlockIdentifier;
import com.decompiler.util.collections.Functional;
import com.decompiler.util.collections.ListFactory;

public class RedundantTries {
    public static List<Op03SimpleStatement> removeRedundantTries(List<Op03SimpleStatement> statements) {
        List<Op03SimpleStatement> tryStarts = Functional.filter(statements, new TypeFilter<TryStatement>(TryStatement.class));
        /*
         * If the try doesn't point at a member of the try, it's been made redundant.
         * Verify that no other references to its' block exist, and remove it.
         * (Verification should be unneccesary)
         */
        boolean effect = false;
        Collections.reverse(tryStarts);
        LinkedList<Op03SimpleStatement> starts = ListFactory.newLinkedList();
        starts.addAll(tryStarts);
        while (!starts.isEmpty()) {
            Op03SimpleStatement trys = starts.removeFirst();
            Statement stm = trys.getStatement();
            if (!(stm instanceof TryStatement)) continue;
            TryStatement tryStatement = (TryStatement) stm;
            BlockIdentifier tryBlock = tryStatement.getBlockIdentifier();
            if (trys.getTargets().isEmpty() || !trys.getTargets().get(0).getBlockIdentifiers().contains(tryBlock)) {
                // Remove this try.
                Op03SimpleStatement codeTarget = trys.getTargets().get(0);

                for (Op03SimpleStatement target : trys.getTargets()) {
                    target.removeSource(trys);
                }
                trys.getTargets().clear();
                for (Op03SimpleStatement source : trys.getSources()) {
                    source.replaceTarget(trys, codeTarget);
                    codeTarget.addSource(source);
                }
                trys.getSources().clear();
                effect = true;
            }
        }

        if (effect) {
            statements = Cleaner.removeUnreachableCode(statements, false);
            statements = Cleaner.sortAndRenumber(statements);
        }

        return statements;
    }


}

package com.decompiler.bytecode.analysis.opgraph.op3rewriters;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.decompiler.bytecode.analysis.opgraph.Op03SimpleStatement;
import com.decompiler.bytecode.analysis.parse.Expression;
import com.decompiler.bytecode.analysis.parse.Statement;
import com.decompiler.bytecode.analysis.parse.expression.StackValue;
import com.decompiler.bytecode.analysis.parse.lvalue.StackSSALabel;
import com.decompiler.bytecode.analysis.parse.statement.AssignmentSimple;
import com.decompiler.bytecode.analysis.parse.statement.ExpressionStatement;
import com.decompiler.bytecode.analysis.parse.statement.Nop;
import com.decompiler.util.collections.MapFactory;
import com.decompiler.util.collections.SetFactory;

class IsolatedStackValue {
    static void nopIsolatedStackValues(List<Op03SimpleStatement> statements) {
        // A stack value is (EXCEPT IN THE CASE OF DUP) only consumed once.
        // We can nop both the assignment and the consumption if the consumption is
        // an expression statement.

        Set<StackSSALabel> blackList = SetFactory.newSet();
        Map<StackSSALabel, Op03SimpleStatement> consumptions = MapFactory.newMap();
        Map<StackSSALabel, Op03SimpleStatement> assignments = MapFactory.newMap();

        for (Op03SimpleStatement statement : statements) {
            Statement stm = statement.getStatement();
            if (stm instanceof ExpressionStatement) {
                Expression expression = ((ExpressionStatement) stm).getExpression();
                if (expression instanceof StackValue) {
                    StackValue sv = (StackValue)expression;
                    StackSSALabel stackValue = sv.getStackValue();
                    if (consumptions.put(stackValue, statement) != null|| stackValue.getStackEntry().getUsageCount() > 1) {
                        blackList.add(stackValue);
                    }
                }
            } else if (stm instanceof AssignmentSimple) {
                if (stm.getCreatedLValue() instanceof StackSSALabel) {
                    StackSSALabel stackValue = (StackSSALabel)stm.getCreatedLValue();
                    if (assignments.put(stackValue, statement) != null) {
                        blackList.add(stackValue);
                    }
                }
            }
        }

        for (Map.Entry<StackSSALabel, Op03SimpleStatement> entry : consumptions.entrySet()) {
            StackSSALabel label = entry.getKey();
            Op03SimpleStatement assign = assignments.get(label);
            if (blackList.contains(label) || assign == null) {
                continue;
            }
            entry.getValue().replaceStatement(new Nop());
            assign.replaceStatement(new ExpressionStatement(assign.getStatement().getRValue()));
        }

    }
}

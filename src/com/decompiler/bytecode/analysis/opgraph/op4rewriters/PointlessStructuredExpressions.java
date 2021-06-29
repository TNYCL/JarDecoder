package com.decompiler.bytecode.analysis.opgraph.op4rewriters;

import com.decompiler.bytecode.analysis.opgraph.op3rewriters.PointlessExpressions;
import com.decompiler.bytecode.analysis.parse.Expression;
import com.decompiler.bytecode.analysis.parse.LValue;
import com.decompiler.bytecode.analysis.structured.StructuredStatement;
import com.decompiler.bytecode.analysis.structured.statement.StructuredAssignment;

public class PointlessStructuredExpressions {
    // Same as the op03 version, but we need to unpick the temporary we introduced.
    public static void removePointlessExpression(StructuredStatement stm) {
        if (stm instanceof StructuredAssignment) {
            StructuredAssignment ass = (StructuredAssignment)stm;
            LValue lv = ass.getLvalue();
            if (lv.isFakeIgnored()) {
                Expression e = ass.getRvalue();
                // This didn't used to be.   But after some rewriting it might now be.
                if (PointlessExpressions.isSafeToIgnore(e)) {
                    stm.getContainer().nopOut();
                }
            }
        }
    }
}

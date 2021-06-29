package com.decompiler.bytecode.analysis.types.discovery;

import com.decompiler.bytecode.analysis.loc.BytecodeLoc;
import com.decompiler.bytecode.analysis.parse.Expression;
import com.decompiler.bytecode.analysis.parse.expression.CastExpression;
import com.decompiler.bytecode.analysis.types.RawJavaType;

public enum CastAction {
    None {
        public Expression performCastAction(Expression orig, InferredJavaType tgtType) {
            return orig;
        }
    },
    InsertExplicit {
        public Expression performCastAction(Expression orig, InferredJavaType tgtType) {
            if (tgtType.getJavaTypeInstance() == RawJavaType.BOOLEAN) return orig;
            return new CastExpression(BytecodeLoc.NONE, tgtType, orig);
        }
    };

    public abstract Expression performCastAction(Expression orig, InferredJavaType tgtType);
}

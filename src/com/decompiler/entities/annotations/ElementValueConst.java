package com.decompiler.entities.annotations;

import com.decompiler.bytecode.analysis.parse.literal.TypedLiteral;
import com.decompiler.bytecode.analysis.types.JavaTypeInstance;
import com.decompiler.bytecode.analysis.types.RawJavaType;
import com.decompiler.state.TypeUsageCollector;
import com.decompiler.util.output.Dumper;

public class ElementValueConst implements ElementValue {
    private final TypedLiteral value;

    public ElementValueConst(TypedLiteral value) {
        this.value = value;
    }

    @Override
    public Dumper dump(Dumper d) {
        return d.dump(value);
    }

    @Override
    public void collectTypeUsages(TypeUsageCollector collector) {
    }

    @Override
    public ElementValue withTypeHint(JavaTypeInstance hint) {
        if (hint == RawJavaType.BOOLEAN) return new ElementValueConst(TypedLiteral.shrinkTo(value, RawJavaType.BOOLEAN));
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        else if (obj instanceof ElementValueArray) {
            ElementValueConst other = (ElementValueConst) obj;
            return value.equals(other.value);
        }
        else {
            return false;
        }
    }
}

package com.decompiler.entities.annotations;

import java.util.List;

import com.decompiler.bytecode.analysis.types.JavaTypeInstance;
import com.decompiler.state.TypeUsageCollector;
import com.decompiler.util.StringUtils;
import com.decompiler.util.output.Dumper;

public class ElementValueArray implements ElementValue {
    private final List<ElementValue> content;

    public ElementValueArray(List<ElementValue> content) {
        this.content = content;
    }

    @Override
    public Dumper dump(Dumper d) {
        d.print('{');
        boolean first = true;
        for (ElementValue value : content) {
            first = StringUtils.comma(first, d);
            value.dump(d);
        }
        d.print('}');
        return d;
    }

    @Override
    public void collectTypeUsages(TypeUsageCollector collector) {
        for (ElementValue e : content) {
            e.collectTypeUsages(collector);
        }
    }

    @Override
    public ElementValue withTypeHint(JavaTypeInstance hint) {
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        else if (obj instanceof ElementValueArray) {
            ElementValueArray other = (ElementValueArray) obj;
            return content.equals(other.content);
        }
        else {
            return false;
        }
    }
}

package com.decompiler.entities.annotations;

import java.util.Map;

import com.decompiler.bytecode.analysis.types.JavaTypeInstance;
import com.decompiler.state.TypeUsageCollector;
import com.decompiler.util.StringUtils;
import com.decompiler.util.TypeUsageCollectable;
import com.decompiler.util.output.Dumper;

public class AnnotationTableEntry implements TypeUsageCollectable {
    private final JavaTypeInstance clazz;
    // Sorted map to make ordering predictable.
    private final Map<String, ElementValue> elementValueMap;
    private boolean hidden;

    public AnnotationTableEntry(JavaTypeInstance clazz, Map<String, ElementValue> elementValueMap) {
        this.clazz = clazz;
        this.elementValueMap = elementValueMap;
    }

    public void setHidden() {
        hidden = true;
    }

    public boolean isHidden() {
        return hidden;
    }

    public JavaTypeInstance getClazz() {
        return clazz;
    }

    public Dumper dump(Dumper d) {
        d.print('@').dump(clazz);
        if (elementValueMap != null && !elementValueMap.isEmpty()) {
            d.print('(');
            boolean first = true;
            for (Map.Entry<String, ElementValue> elementValueEntry : elementValueMap.entrySet()) {
                first = StringUtils.comma(first, d);
                d.print(elementValueEntry.getKey()).print('=');
                elementValueEntry.getValue().dump(d);
            }
            d.print(')');
        }
        return d;
    }

    @Override
    public void collectTypeUsages(TypeUsageCollector collector) {
        collector.collect(clazz);
        if (elementValueMap != null) {
            for (ElementValue elementValue : elementValueMap.values()) {
                elementValue.collectTypeUsages(collector);
            }
        }
    }

    public boolean isAnnotationEqual(AnnotationTableEntry other) {
        return clazz.equals(other.getClazz()) && elementValueMap.equals(other.elementValueMap);
    }
}

package com.decompiler.bytecode.analysis.loc;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.decompiler.entities.Method;
import com.decompiler.util.collections.SetFactory;

public class BytecodeLocSet extends BytecodeLoc {
    private final Map<Method, Set<Integer>> locs;

    BytecodeLocSet(Map<Method, Set<Integer>> locs) {
        this.locs = locs;
    }

    @Override
    void addTo(BytecodeLocCollector collector) {
        for (Map.Entry<Method, Set<Integer>> entry : locs.entrySet()) {
            collector.add(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Method, Set<Integer>> entry : locs.entrySet()) {
            sb.append(entry.getKey().getName()).append("[");
            for (Integer i : entry.getValue()) {
                sb.append(i).append(",");
            }
            sb.append("]");
        }
        return sb.toString();
    }

    @Override
    public Collection<Method> getMethods() {
        return locs.keySet();
    }

    @Override
    public Collection<Integer> getOffsetsForMethod(Method method) {
        return locs.get(method);
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}

package com.decompiler.bytecode.analysis.loc;

import java.util.Map;
import java.util.Set;

import com.decompiler.entities.Method;
import com.decompiler.util.collections.CollectionUtils;
import com.decompiler.util.collections.MapFactory;
import com.decompiler.util.collections.SetFactory;
import com.decompiler.util.collections.SetUtil;

/*
 * This implementation is not intended to be performant - revisit when functionally complete.
 */
public class BytecodeLocCollector {

    private final Map<Method, Set<Integer>> data = MapFactory.newIdentityMap();

    private Set<Integer> getForMethod(Method method) {
        Set<Integer> locs = data.get(method);
        if (locs == null) {
            locs = SetFactory.newSet();
            data.put(method, locs);
        }
        return locs;
    }

    public void add(Method method, int offset) {
        getForMethod(method).add(offset);
    }

    public void add(Method method, Set<Integer> offsets) {
        getForMethod(method).addAll(offsets);
    }

    public BytecodeLoc getLoc() {
        if (data.isEmpty()) return BytecodeLoc.NONE;
        if (data.values().size() == 1) {
            Set<Integer> s = CollectionUtils.getSingle(data.values());
            if (s.size() == 1) {
                return new BytecodeLocSimple(
                        SetUtil.getSingle(s),
                        SetUtil.getSingle(data.keySet()));
            }
        }
        return new BytecodeLocSet(data);
    }
}

package com.decompiler.state;

import java.util.Map;

import com.decompiler.bytecode.analysis.types.MethodPrototype;
import com.decompiler.entities.ClassFile;
import com.decompiler.entities.classfilehelpers.OverloadMethodSet;
import com.decompiler.util.collections.MapFactory;
import com.decompiler.util.functors.UnaryFunction;

// The cost of retaining all overload information may become large.  Keeping it centrally allows us to flush it if in low
// memory mode.
public class OverloadMethodSetCache {
    private final Map<ClassFile, Map<MethodPrototype, OverloadMethodSet>> content = MapFactory.newLazyMap(new UnaryFunction<ClassFile, Map<MethodPrototype, OverloadMethodSet>>() {
        @Override
        public Map<MethodPrototype, OverloadMethodSet> invoke(ClassFile arg) {
            return MapFactory.newIdentityMap();
        }
    });

    public OverloadMethodSet get(ClassFile classFile, MethodPrototype methodPrototype) {
        return content.get(classFile).get(methodPrototype);
    }

    public void set(ClassFile classFile, MethodPrototype methodPrototype, OverloadMethodSet overloadMethodSet) {
        content.get(classFile).put(methodPrototype, overloadMethodSet);
    }
}

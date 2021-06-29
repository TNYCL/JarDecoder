package com.decompiler.bytecode.analysis.types;

import java.util.List;

import com.decompiler.entities.constantpool.ConstantPool;
import com.decompiler.state.TypeUsageCollector;
import com.decompiler.util.TypeUsageCollectable;
import com.decompiler.util.collections.ListFactory;

public class ClassSignature implements TypeUsageCollectable {
    private final List<FormalTypeParameter> formalTypeParameters;
    private final JavaTypeInstance superClass;
    private final List<JavaTypeInstance> interfaces;

    public ClassSignature(List<FormalTypeParameter> formalTypeParameters, JavaTypeInstance superClass, List<JavaTypeInstance> interfaces) {
        this.formalTypeParameters = formalTypeParameters;
        this.superClass = superClass;
        this.interfaces = interfaces;
    }

    public List<FormalTypeParameter> getFormalTypeParameters() {
        return formalTypeParameters;
    }

    public JavaTypeInstance getSuperClass() {
        return superClass;
    }

    public List<JavaTypeInstance> getInterfaces() {
        return interfaces;
    }

    @Override
    public void collectTypeUsages(TypeUsageCollector collector) {
        collector.collect(superClass);
        collector.collectFrom(formalTypeParameters);
        collector.collect(interfaces);
    }

    // TODO : This is pointless.
    public JavaTypeInstance getThisGeneralTypeClass(JavaTypeInstance nonGenericInstance, ConstantPool cp) {
        if (nonGenericInstance instanceof JavaGenericBaseInstance) return nonGenericInstance;
        if (formalTypeParameters == null || formalTypeParameters.isEmpty()) return nonGenericInstance;
        List<JavaTypeInstance> typeParameterNames = ListFactory.newList();
        for (FormalTypeParameter formalTypeParameter : formalTypeParameters) {
            typeParameterNames.add(new JavaGenericPlaceholderTypeInstance(formalTypeParameter.getName(), cp));
        }
        JavaTypeInstance res = new JavaGenericRefTypeInstance(nonGenericInstance, typeParameterNames);
        return res;
    }
}

package com.decompiler.bytecode.analysis.types;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.decompiler.bytecode.analysis.types.annotated.JavaAnnotatedTypeInstance;
import com.decompiler.state.ObfuscationTypeMap;
import com.decompiler.state.TypeUsageCollector;
import com.decompiler.state.TypeUsageInformation;
import com.decompiler.util.collections.Functional;
import com.decompiler.util.collections.ListFactory;
import com.decompiler.util.output.Dumper;
import com.decompiler.util.output.IllegalIdentifierDump;
import com.decompiler.util.output.TypeContext;

public class JavaIntersectionTypeInstance implements JavaTypeInstance {
    private final List<JavaTypeInstance> parts;
    private final int id;
    private final static AtomicInteger sid = new AtomicInteger();

    public JavaIntersectionTypeInstance(List<JavaTypeInstance> parts) {
        this.parts = parts;
        id = sid.getAndIncrement();
    }

    JavaIntersectionTypeInstance withPart(JavaTypeInstance part) {
        List<JavaTypeInstance> newParts = ListFactory.newList(parts);
        newParts.add(part);
        return new JavaIntersectionTypeInstance(newParts);
    }

    @Override
    public JavaAnnotatedTypeInstance getAnnotatedInstance() {
        return null;
    }

    @Override
    public StackType getStackType() {
        return StackType.REF;
    }

    @Override
    public boolean isComplexType() {
        return false;
    }

    @Override
    public boolean isUsableType() {
        return false;
    }

    @Override
    public RawJavaType getRawTypeOfSimpleType() {
        return RawJavaType.REF;
    }

    @Override
    public JavaTypeInstance removeAnArrayIndirection() {
        return this;
    }

    @Override
    public JavaTypeInstance getArrayStrippedType() {
        return this;
    }

    @Override
    public JavaTypeInstance getDeGenerifiedType() {
        return this;
    }

    @Override
    public int getNumArrayDimensions() {
        return 0;
    }

    @Override
    public String getRawName() {
        return "<intersection#"  +id + ">";
    }

    @Override
    public String getRawName(IllegalIdentifierDump iid) {
        return getRawName();
    }

    @Override
    public InnerClassInfo getInnerClassHereInfo() {
        return InnerClassInfo.NOT;
    }

    @Override
    public BindingSuperContainer getBindingSupers() {
        return null;
    }

    @Override
    public boolean implicitlyCastsTo(JavaTypeInstance other, GenericTypeBinder gtb) {
        for (JavaTypeInstance t : parts) {
            if (t.implicitlyCastsTo(other, gtb)) return true;
        }
        return false;
    }

    @Override
    public boolean impreciseCanCastTo(JavaTypeInstance other, GenericTypeBinder gtb) {
        for (JavaTypeInstance t : parts) {
            if (t.impreciseCanCastTo(other, gtb)) return true;
        }
        return false;
    }

    @Override
    public boolean correctCanCastTo(JavaTypeInstance other, GenericTypeBinder gtb) {
        for (JavaTypeInstance t : parts) {
            if (t.correctCanCastTo(other, gtb)) return true;
        }
        return false;
    }

    @Override
    public String suggestVarName() {
        return "intersect";
    }

    @Override
    public void dumpInto(Dumper d, TypeUsageInformation typeUsageInformation, TypeContext typeContext) {
        boolean first = true;
        for (JavaTypeInstance t : parts) {
            if (!first) {
                d.print(" & ");
            }
            first = false;
            d.dump(t);
        }
    }

    @Override
    public void collectInto(TypeUsageCollector typeUsageCollector) {
        for (JavaTypeInstance t : parts) {
            t.collectInto(typeUsageCollector);
        }
    }

    @Override
    public boolean isObject() {
        return true;
    }

    @Override
    public boolean isRaw() {
        return false;
    }

    @Override
    public JavaGenericRefTypeInstance asGenericRefInstance(JavaTypeInstance other) {
        JavaTypeInstance degenerifiedOther = other.getDeGenerifiedType();

        for (JavaTypeInstance part : parts) {
            if (part.getDeGenerifiedType().equals(degenerifiedOther)) {
                return part.asGenericRefInstance(other);
            }
        }
        return null;
    }

    @Override
    public JavaTypeInstance deObfuscate(ObfuscationTypeMap obfuscationTypeMap) {
        return new JavaIntersectionTypeInstance(Functional.map(parts, obfuscationTypeMap.getter()));
    }

    @Override
    public JavaTypeInstance directImplOf(JavaTypeInstance other) {
        for (JavaTypeInstance part : parts) {
            JavaTypeInstance res = part.directImplOf(other);
            if (res != null) return res;
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (JavaTypeInstance t : parts) {
            if (!first) {
                sb.append(" & ");
            }
            first = false;
            sb.append(t);
        }
        return sb.toString();
    }
}

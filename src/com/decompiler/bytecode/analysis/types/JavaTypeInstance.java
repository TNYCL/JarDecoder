package com.decompiler.bytecode.analysis.types;

import com.decompiler.bytecode.analysis.types.annotated.JavaAnnotatedTypeInstance;
import com.decompiler.state.ObfuscationTypeMap;
import com.decompiler.state.TypeUsageCollector;
import com.decompiler.state.TypeUsageInformation;
import com.decompiler.util.output.Dumper;
import com.decompiler.util.output.IllegalIdentifierDump;
import com.decompiler.util.output.TypeContext;

public interface JavaTypeInstance {

    // This returns an annotation wrapper over the type - it is much heavier, so we don't
    // do it unless necessary.
    JavaAnnotatedTypeInstance getAnnotatedInstance();

    StackType getStackType();

    boolean isComplexType();

    boolean isUsableType();

    /*
     * TODO : Doesn't feel like this is right, it ignores array dimensionality.
     */
    RawJavaType getRawTypeOfSimpleType();

    /*
     * Again, can't we already be sure we have an array type here?
     * TODO : Doesn't feel right.
     */
    JavaTypeInstance removeAnArrayIndirection();

    JavaTypeInstance getArrayStrippedType();

    /*
     * This will return a type stripped of ALL generic information
     *
     * i.e. Set<Set<?>> -> Set
     */
    JavaTypeInstance getDeGenerifiedType();

    /*
     * This will return a 'minimally' degenerified type - i.e. just enough to
     * remove bad generic info
     *
     * i.e. Set<Set<?>> -> Set<Set>
     */
//    public JavaTypeInstance getMinimallyDeGenerifiedType();

    int getNumArrayDimensions();

    String getRawName();

    String getRawName(IllegalIdentifierDump iid);

    //    public boolean isInnerClassOf(JavaTypeInstance possibleParent);
    // Get info about this class as an inner class, not inner classes of this.....
    InnerClassInfo getInnerClassHereInfo();

    BindingSuperContainer getBindingSupers();

    boolean implicitlyCastsTo(JavaTypeInstance other, GenericTypeBinder gtb);

    /*
     * Boxing relies on this bad implementation.... :P
     */
    boolean impreciseCanCastTo(JavaTypeInstance other, GenericTypeBinder gtb);

    boolean correctCanCastTo(JavaTypeInstance other, GenericTypeBinder gtb);

    String suggestVarName();

    void dumpInto(Dumper d, TypeUsageInformation typeUsageInformation, TypeContext typeContext);

    void collectInto(TypeUsageCollector typeUsageCollector);

    boolean isObject();

    /*
     * Return either the most appropriate generic ref instance or null.
     */
    JavaGenericRefTypeInstance asGenericRefInstance(JavaTypeInstance other);

    /*
     * Does this *directly* implement other?
     * if so, return actual implementation.
     *
     * Particularly useful in pulling a generic implementation of I out of an intersection type.
     *
     * Strip generics before calling.
     */
    JavaTypeInstance directImplOf(JavaTypeInstance other);

    JavaTypeInstance deObfuscate(ObfuscationTypeMap obfuscationTypeMap);
    /*
     * Is this a raw type?
     */
    boolean isRaw();
}

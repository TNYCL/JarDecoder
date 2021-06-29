package com.decompiler.state;

import java.util.Set;

import com.decompiler.bytecode.analysis.types.JavaRefTypeInstance;
import com.decompiler.bytecode.analysis.types.JavaTypeInstance;
import com.decompiler.util.output.IllegalIdentifierDump;
import com.decompiler.util.output.TypeContext;

public interface TypeUsageInformation {
    JavaRefTypeInstance getAnalysisType();

    Set<JavaRefTypeInstance> getShortenedClassTypes();

    Set<JavaRefTypeInstance> getUsedClassTypes();

    Set<JavaRefTypeInstance> getUsedInnerClassTypes();

    boolean hasLocalInstance(JavaRefTypeInstance type);

    String getName(JavaTypeInstance type, TypeContext typeContext);

    boolean isNameClash(JavaTypeInstance type, String name, TypeContext typeContext);

    String generateInnerClassShortName(JavaRefTypeInstance clazz);

    String generateOverriddenName(JavaRefTypeInstance clazz);

    IllegalIdentifierDump getIid();

    boolean isStaticImport(JavaTypeInstance clazz, String fixedName);

    Set<DetectedStaticImport> getDetectedStaticImports();
}

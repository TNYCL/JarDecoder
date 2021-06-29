package com.decompiler.entities.exceptions;

import java.util.Collections;
import java.util.Set;

import com.decompiler.bytecode.analysis.types.JavaRefTypeInstance;
import com.decompiler.bytecode.analysis.types.JavaTypeInstance;
import com.decompiler.util.collections.SetFactory;

/*
 * This defines the set of exceptions which can be thrown by jvm instructions natively.
 */
public class BasicExceptions {
    public static final Set<? extends JavaTypeInstance> instances = Collections.unmodifiableSet(SetFactory.newSet(
            JavaRefTypeInstance.createTypeConstant("java.lang.AbstractMethodError"),
            JavaRefTypeInstance.createTypeConstant("java.lang.ArithmeticException"),
            JavaRefTypeInstance.createTypeConstant("java.lang.ArrayIndexOutOfBoundsException"),
            JavaRefTypeInstance.createTypeConstant("java.lang.ArrayStoreException"),
            JavaRefTypeInstance.createTypeConstant("java.lang.ClassCastException"),
            JavaRefTypeInstance.createTypeConstant("java.lang.IllegalAccessError"),
            JavaRefTypeInstance.createTypeConstant("java.lang.IllegalMonitorStateException"),
            JavaRefTypeInstance.createTypeConstant("java.lang.IncompatibleClassChangeError"),
            JavaRefTypeInstance.createTypeConstant("java.lang.InstantiationError"),
            JavaRefTypeInstance.createTypeConstant("java.lang.NegativeArraySizeException"),
            JavaRefTypeInstance.createTypeConstant("java.lang.NullPointerException"),
            JavaRefTypeInstance.createTypeConstant("java.lang.UnsatisfiedLinkError")
    ));
}

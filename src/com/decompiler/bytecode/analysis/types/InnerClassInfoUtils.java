package com.decompiler.bytecode.analysis.types;

public class InnerClassInfoUtils {
    public static JavaRefTypeInstance getTransitiveOuterClass(JavaRefTypeInstance type) {
        while (type.getInnerClassHereInfo().isInnerClass()) {
            type = type.getInnerClassHereInfo().getOuterClass();
        }
        return type;
    }
}

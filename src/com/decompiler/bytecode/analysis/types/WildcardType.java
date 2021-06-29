package com.decompiler.bytecode.analysis.types;

public enum WildcardType {
    NONE(""),
    SUPER("super"),
    EXTENDS("extends");

    private final String name;

    WildcardType(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return name;
    }
}

package com.decompiler.util;

public class CannotLoadClassException extends RuntimeException {
    public CannotLoadClassException(String s, Throwable throwable) {
        super(s, throwable);
    }

    @Override
    public String toString() {
        return super.toString() + ((super.getCause() == null) ? "" : (" - " + super.getCause().toString()));
    }
}

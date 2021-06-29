package com.decompiler.util;

public class ConfusedDecompilerException extends RuntimeException {
    public ConfusedDecompilerException(String s)
    {
        super(s);
    }
    public ConfusedDecompilerException(Exception e)
    {
        super(e);
    }
}

package com.decompiler.bytecode;

import com.decompiler.bytecode.analysis.opgraph.Op04StructuredStatement;
import com.decompiler.util.DecompilerComments;

public interface AnalysisResult {
    boolean isFailed();
    boolean isThrown();
    Op04StructuredStatement getCode();
    DecompilerComments getComments();
    AnonymousClassUsage getAnonymousClassUsage();
}

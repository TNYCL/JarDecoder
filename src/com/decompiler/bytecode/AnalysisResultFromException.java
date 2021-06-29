package com.decompiler.bytecode;

import com.decompiler.bytecode.analysis.opgraph.Op04StructuredStatement;
import com.decompiler.bytecode.analysis.structured.statement.StructuredFakeDecompFailure;
import com.decompiler.util.DecompilerComment;
import com.decompiler.util.DecompilerComments;

public class AnalysisResultFromException implements AnalysisResult {
    private final Op04StructuredStatement code;
    private final DecompilerComments comments;

    public AnalysisResultFromException(Exception e) {
        this.code = new Op04StructuredStatement(new StructuredFakeDecompFailure(e));
        this.comments = new DecompilerComments();
        comments.addComment(new DecompilerComment("Exception decompiling", e));
    }

    @Override
    public boolean isFailed() {
        return true;
    }

    @Override
    public boolean isThrown() {
        return true;
    }

    @Override
    public Op04StructuredStatement getCode() {
        return code;
    }

    @Override
    public DecompilerComments getComments() {
        return comments;
    }

    @Override
    public AnonymousClassUsage getAnonymousClassUsage() {
        return new AnonymousClassUsage();
    }
}

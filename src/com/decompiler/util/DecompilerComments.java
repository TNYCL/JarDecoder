package com.decompiler.util;

import java.util.Collection;
import java.util.Set;

import com.decompiler.util.collections.SetFactory;
import com.decompiler.util.output.Dumpable;
import com.decompiler.util.output.Dumper;

public class DecompilerComments implements Dumpable {
    private Set<DecompilerComment> comments = SetFactory.newOrderedSet();

    public DecompilerComments() {
    }

    public void addComment(String comment) {
        DecompilerComment decompilerComment = new DecompilerComment(comment);
        comments.add(decompilerComment);
    }

    public void addComment(DecompilerComment comment) {
        comments.add(comment);
    }

    public void addComments(Collection<DecompilerComment> comments) {
        this.comments.addAll(comments);
    }

    @Override
    public Dumper dump(Dumper d) {
        if (comments.isEmpty()) return d;
        d.beginBlockComment(false);
        for (DecompilerComment comment : comments) {
            d.dump(comment);
        }
        d.endBlockComment();
        return d;
    }

    public boolean contains(DecompilerComment comment) {
        return comments.contains(comment);
    }

    public Collection<DecompilerComment> getCommentCollection() {
        return comments;
    }

}

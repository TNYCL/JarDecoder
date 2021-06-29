package com.decompiler.bytecode;

import java.util.List;

import com.decompiler.bytecode.analysis.parse.expression.ConstructorInvokationAnonymousInner;
import com.decompiler.bytecode.analysis.parse.expression.ConstructorInvokationSimple;
import com.decompiler.bytecode.analysis.parse.utils.Pair;
import com.decompiler.entities.ClassFile;
import com.decompiler.util.collections.ListFactory;

/*
 * Usage of anonymous classes currently requires decorating those classes once we've determined the code that's
 * using them - i.e. mutating state. We therefore have to extract this information so we don't perform it multiple
 * times (inside a recovery).
 */
public class AnonymousClassUsage {
    private final List<Pair<ClassFile, ConstructorInvokationAnonymousInner>> noted = ListFactory.newList();
    private final List<Pair<ClassFile, ConstructorInvokationSimple>> localNoted = ListFactory.newList();

    public void note(ClassFile classFile, ConstructorInvokationAnonymousInner constructorInvokationAnonymousInner) {
        noted.add(Pair.make(classFile, constructorInvokationAnonymousInner));
    }

    public void noteMethodClass(ClassFile classFile, ConstructorInvokationSimple constructorInvokation) {
        localNoted.add(Pair.make(classFile, constructorInvokation));
    }

    public boolean isEmpty() {
        return noted.isEmpty() && localNoted.isEmpty();
    }

    void useNotes() {
        for (Pair<ClassFile, ConstructorInvokationAnonymousInner> note : noted) {
            note.getFirst().noteAnonymousUse(note.getSecond());
        }
        for (Pair<ClassFile, ConstructorInvokationSimple> note : localNoted) {
            note.getFirst().noteMethodUse(note.getSecond());
        }
    }
}

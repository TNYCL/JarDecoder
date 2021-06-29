package com.decompiler.bytecode.analysis.opgraph.op3rewriters;

import java.util.List;

import com.decompiler.bytecode.AnonymousClassUsage;
import com.decompiler.bytecode.analysis.opgraph.Op03SimpleStatement;
import com.decompiler.bytecode.analysis.parse.utils.CreationCollector;
import com.decompiler.entities.Method;
import com.decompiler.state.DCCommonState;

class CondenseConstruction {
    /*
     * Find all the constructors and initialisers.  If something is initialised and
     * constructed in one place each, we can guarantee that the construction happened
     * after the initialisation, so replace
     *
     * a1 = new foo
     * a1.<init>(x, y, z)
     *
     * with
     *
     * a1 = new foo(x,y,z)
     */
    static void condenseConstruction(DCCommonState state, Method method, List<Op03SimpleStatement> statements, AnonymousClassUsage anonymousClassUsage) {
        CreationCollector creationCollector = new CreationCollector(anonymousClassUsage);
        for (Op03SimpleStatement statement : statements) {
            statement.findCreation(creationCollector);
        }
        creationCollector.condenseConstructions(method, state);
    }

}

package com.decompiler.bytecode.analysis.opgraph.op3rewriters;

import java.util.List;

import com.decompiler.bytecode.analysis.loc.BytecodeLoc;
import com.decompiler.bytecode.analysis.opgraph.Op03SimpleStatement;
import com.decompiler.bytecode.analysis.parse.statement.GotoStatement;
import com.decompiler.bytecode.analysis.parse.statement.ReturnNothingStatement;
import com.decompiler.entities.Method;
import com.decompiler.util.MiscConstants;
import com.decompiler.util.getopt.Options;
import com.decompiler.util.getopt.OptionsImpl;

/*
 * We shouldn't have return statements in static initializers.
 */
public class StaticInitReturnRewriter {
    public static List<Op03SimpleStatement> rewrite(Options options, Method method, List<Op03SimpleStatement> statementList) {
        if (!method.getName().equals(MiscConstants.STATIC_INIT_METHOD)) return statementList;
        if (!options.getOption(OptionsImpl.STATIC_INIT_RETURN)) return statementList;
        /*
         * if the final statement is a return, then replace all other returns with a jump to that.
         */
        Op03SimpleStatement last = statementList.get(statementList.size()-1);
        if (last.getStatement().getClass() != ReturnNothingStatement.class) return statementList;
        for (int x =0, len=statementList.size()-1;x<len;++x) {
            Op03SimpleStatement stm = statementList.get(x);
            if (stm.getStatement().getClass() == ReturnNothingStatement.class) {
                stm.replaceStatement(new GotoStatement(BytecodeLoc.TODO));
                stm.addTarget(last);
                last.addSource(stm);
            }
        }
        return statementList;
    }
}

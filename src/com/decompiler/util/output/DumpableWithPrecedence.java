package com.decompiler.util.output;

import com.decompiler.bytecode.analysis.parse.expression.misc.Precedence;
import com.decompiler.util.Troolean;

public interface DumpableWithPrecedence extends Dumpable {

    Precedence getPrecedence();

    Dumper dumpWithOuterPrecedence(Dumper d, Precedence outerPrecedence, Troolean isLhs);

}

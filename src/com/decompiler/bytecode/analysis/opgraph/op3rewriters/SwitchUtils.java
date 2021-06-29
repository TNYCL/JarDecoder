package com.decompiler.bytecode.analysis.opgraph.op3rewriters;

import java.util.List;

import com.decompiler.bytecode.analysis.opgraph.Op03SimpleStatement;
import com.decompiler.bytecode.analysis.parse.statement.CaseStatement;
import com.decompiler.bytecode.analysis.parse.utils.BlockIdentifier;
import com.decompiler.bytecode.analysis.parse.utils.BlockType;
import com.decompiler.util.collections.Functional;
import com.decompiler.util.collections.SetUtil;
import com.decompiler.util.functors.Predicate;

class SwitchUtils {
    static void checkFixNewCase(Op03SimpleStatement possCaseItem, Op03SimpleStatement possCase) {
        if (possCase.getStatement().getClass() != CaseStatement.class) return;
        List<BlockIdentifier> idents = SetUtil.differenceAtakeBtoList(possCaseItem.getBlockIdentifiers(), possCase.getBlockIdentifiers());
        idents = Functional.filter(idents, new Predicate<BlockIdentifier>() {
            @Override
            public boolean test(BlockIdentifier in) {
                return in.getBlockType() == BlockType.CASE;
            }
        });
        if (idents.isEmpty()) {
            BlockIdentifier blockIdentifier = ((CaseStatement)possCase.getStatement()).getCaseBlock();
            possCaseItem.getBlockIdentifiers().add(blockIdentifier);
        }
    }
}

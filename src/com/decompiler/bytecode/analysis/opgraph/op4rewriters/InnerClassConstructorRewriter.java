package com.decompiler.bytecode.analysis.opgraph.op4rewriters;

import java.util.List;

import com.decompiler.bytecode.analysis.loc.BytecodeLoc;
import com.decompiler.bytecode.analysis.opgraph.Op04StructuredStatement;
import com.decompiler.bytecode.analysis.opgraph.op4rewriters.matchutil.*;
import com.decompiler.bytecode.analysis.opgraph.op4rewriters.util.MiscStatementTools;
import com.decompiler.bytecode.analysis.parse.LValue;
import com.decompiler.bytecode.analysis.parse.expression.LValueExpression;
import com.decompiler.bytecode.analysis.parse.lvalue.FieldVariable;
import com.decompiler.bytecode.analysis.parse.lvalue.LocalVariable;
import com.decompiler.bytecode.analysis.parse.wildcard.WildcardMatch;
import com.decompiler.bytecode.analysis.structured.StructuredStatement;
import com.decompiler.bytecode.analysis.structured.statement.StructuredAssignment;
import com.decompiler.entities.AccessFlag;
import com.decompiler.entities.ClassFile;
import com.decompiler.entities.ClassFileField;
import com.decompiler.entities.Field;

public class InnerClassConstructorRewriter implements Op04Rewriter {
    private final ClassFile classFile;
    private final LocalVariable outerArg;
    private FieldVariable matchedField;
    private StructuredStatement assignmentStatement;

    public InnerClassConstructorRewriter(ClassFile classFile, LocalVariable outerArg) {
        this.outerArg = outerArg;
        this.classFile = classFile;
    }

    @Override
    public void rewrite(Op04StructuredStatement root) {
        List<StructuredStatement> structuredStatements = MiscStatementTools.linearise(root);

        WildcardMatch wcm1 = new WildcardMatch();

        Matcher<StructuredStatement> m = new CollectMatch("ass1", new StructuredAssignment(BytecodeLoc.NONE, wcm1.getLValueWildCard("outercopy"), new LValueExpression(outerArg)));


        /* The first usage of this variable needs to be an assignment to a final synthetic member */
        MatchIterator<StructuredStatement> mi = new MatchIterator<StructuredStatement>(structuredStatements);
        ConstructResultCollector collector = new ConstructResultCollector();
        while (mi.hasNext()) {
            mi.advance();
            if (m.match(mi, collector)) {
                LValue lValue = wcm1.getLValueWildCard("outercopy").getMatch();
                if (lValue instanceof FieldVariable) {
                    try {
                        FieldVariable fieldVariable = (FieldVariable)lValue;
                        ClassFileField classField = classFile.getFieldByName(fieldVariable.getRawFieldName(), fieldVariable.getInferredJavaType().getJavaTypeInstance());
                        Field field = classField.getField();
                        // Don't continue if it's not final + synthetic?
                        // it's a bad match, so we don't want to try for a later one.
                        if ((field.testAccessFlag(AccessFlag.ACC_SYNTHETIC) && field.testAccessFlag(AccessFlag.ACC_FINAL))) {
                            assignmentStatement = collector.assignmentStatement;
                            matchedField = (FieldVariable) lValue;
                        }
                    } catch (NoSuchFieldException ignore) {
                    }
                }
                return;
            }
        }
    }

    public FieldVariable getMatchedField() {
        return matchedField;
    }

    public StructuredStatement getAssignmentStatement() {
        return assignmentStatement;
    }

    private static class ConstructResultCollector extends AbstractMatchResultIterator {

        private StructuredStatement assignmentStatement;

        private ConstructResultCollector() {
        }

        @Override
        public void clear() {
            assignmentStatement = null;
        }

        @Override
        public void collectStatement(String name, StructuredStatement statement) {
            assignmentStatement = statement;
            /* We also have to rename lValue as 'this.ClassName', or simply '' in the case where there is
             * no ambiguity.
             *
             * We do this at a later step, in case there are multiple constructors.
             */
        }
    }
}

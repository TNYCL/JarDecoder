package com.decompiler.bytecode.analysis.opgraph.op4rewriters;

import java.util.List;
import java.util.Set;

import com.decompiler.bytecode.analysis.loc.BytecodeLoc;
import com.decompiler.bytecode.analysis.opgraph.op4rewriters.matchutil.MatchIterator;
import com.decompiler.bytecode.analysis.opgraph.op4rewriters.matchutil.MatchSequence;
import com.decompiler.bytecode.analysis.opgraph.op4rewriters.matchutil.Matcher;
import com.decompiler.bytecode.analysis.opgraph.op4rewriters.transformers.ExpressionRewriterTransformer;
import com.decompiler.bytecode.analysis.opgraph.op4rewriters.transformers.StructuredStatementTransformer;
import com.decompiler.bytecode.analysis.opgraph.op4rewriters.util.MiscStatementTools;
import com.decompiler.bytecode.analysis.parse.Expression;
import com.decompiler.bytecode.analysis.parse.expression.*;
import com.decompiler.bytecode.analysis.parse.literal.TypedLiteral;
import com.decompiler.bytecode.analysis.parse.lvalue.LocalVariable;
import com.decompiler.bytecode.analysis.parse.lvalue.StaticVariable;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriter;
import com.decompiler.bytecode.analysis.parse.rewriters.ExpressionRewriterFlags;
import com.decompiler.bytecode.analysis.parse.utils.Pair;
import com.decompiler.bytecode.analysis.parse.utils.QuotingUtils;
import com.decompiler.bytecode.analysis.parse.wildcard.WildcardMatch;
import com.decompiler.bytecode.analysis.structured.StructuredScope;
import com.decompiler.bytecode.analysis.structured.StructuredStatement;
import com.decompiler.bytecode.analysis.structured.statement.StructuredCatch;
import com.decompiler.bytecode.analysis.structured.statement.StructuredReturn;
import com.decompiler.bytecode.analysis.structured.statement.StructuredThrow;
import com.decompiler.bytecode.analysis.structured.statement.StructuredTry;
import com.decompiler.bytecode.analysis.structured.statement.placeholder.BeginBlock;
import com.decompiler.bytecode.analysis.structured.statement.placeholder.EndBlock;
import com.decompiler.bytecode.analysis.types.JavaTypeInstance;
import com.decompiler.bytecode.analysis.types.TypeConstants;
import com.decompiler.bytecode.analysis.types.discovery.InferredJavaType;
import com.decompiler.entities.AccessFlagMethod;
import com.decompiler.entities.ClassFile;
import com.decompiler.entities.ClassFileField;
import com.decompiler.entities.Method;
import com.decompiler.state.DCCommonState;
import com.decompiler.util.collections.ListFactory;
import com.decompiler.util.collections.SetFactory;
import com.decompiler.util.functors.NonaryFunction;

/**
 * Java 1.4 will produce
 * <p/>
 * <p/>
 * <p/>
 * public class Java14Test {
 * public Java14Test instance = Java14Test.bar(Java14Test.class$cfrtest$Java14Test == null ? (Java14Test.class$cfrtest$Java14Test = Java14Test.class$("cfrtest.Java14Test")) : Java14Test.class$cfrtest$Java14Test);
 * static Class class$cfrtest$Java14Test;
 * <p/>
 * public static Java14Test bar(Class class_) {
 * return new Java14Test();
 * }
 * <p/>
 * static Class class$(String string) {
 * try {
 * return Class.forName(string);
 * }
 * catch (ClassNotFoundException var1_1) {
 * throw new NoClassDefFoundError().initCause((Throwable)var1_1);
 * }
 * }
 * }
 * <p/>
 * for
 * <p/>
 * public class Java14Test {
 * <p/>
 * public static Java14Test bar(Class c) {
 * <p/>
 * return new Java14Test();
 * <p/>
 * }
 * <p/>
 * public Java14Test instance = bar(Java14Test.class);
 * <p/>
 * }
 */
public class J14ClassObjectRewriter {
    private final ClassFile classFile;
    private final DCCommonState state;

    public J14ClassObjectRewriter(ClassFile classFile, DCCommonState state) {
        this.classFile = classFile;
        this.state = state;
    }

    /*
     * We're very simplistic here - we are not going to attempt to rewrite this if the code is
     * obfuscated - it's not worth it.
     *
     * Recognise the code of class$, and find references to it in static initialisers.
     */
    public void rewrite() {
        Method method = classFile.getSingleMethodByNameOrNull("class$");
        JavaTypeInstance classType = classFile.getClassType();
        if (!methodIsClassLookup(method)) return;

        /*
         * Ok, we've determined that method is the java 1.4 class checker (gross).
         * Hide this method, and rewrite any initialisers that use it.
         */
        method.hideSynthetic();


        final WildcardMatch wcm = new WildcardMatch();
        final WildcardMatch.StaticVariableWildcard staticVariable = wcm.getStaticVariable("classVar", classType, new InferredJavaType(TypeConstants.CLASS, InferredJavaType.Source.TEST));
        LValueExpression staticExpression = new LValueExpression(staticVariable);
        Expression test = new TernaryExpression(BytecodeLoc.NONE,
                new ComparisonOperation(BytecodeLoc.NONE, staticExpression, Literal.NULL, CompOp.EQ),
                new AssignmentExpression(BytecodeLoc.NONE, staticVariable,
                        wcm.getStaticFunction("test",
                                classType,
                                TypeConstants.CLASS,
                                null,
                                ListFactory.<Expression>newImmutableList(wcm.getExpressionWildCard("classString")))
                        ),
                staticExpression);

        final Set<Pair<String, JavaTypeInstance>> hideThese = SetFactory.newSet();
        ExpressionRewriter expressionRewriter = new ExpressionWildcardReplacingRewriter(wcm, test, new NonaryFunction<Expression>() {
            @Override
            public Expression invoke() {
                Expression string = wcm.getExpressionWildCard("classString").getMatch();
                if (!(string instanceof Literal)) return null;
                TypedLiteral literal = ((Literal) string).getValue();
                if (literal.getType() != TypedLiteral.LiteralType.String) return null;
                Expression res = new Literal(TypedLiteral.getClass(state.getClassCache().getRefClassFor(QuotingUtils.unquoteString((String) literal.getValue()))));

                StaticVariable found = staticVariable.getMatch();
                hideThese.add(Pair.make(found.getFieldName(), found.getInferredJavaType().getJavaTypeInstance()));
                return res;
            }
        });

        StructuredStatementTransformer transformer = new ExpressionRewriterTransformer(expressionRewriter);

        /*
         * Apply transformers to all methods, and to all fields.
         */
        for (ClassFileField field : classFile.getFields()) {
            Expression initialValue = field.getInitialValue();
            field.setInitialValue(expressionRewriter.rewriteExpression(initialValue, null, null, ExpressionRewriterFlags.RVALUE));
        }

        for (Method testMethod : classFile.getMethods()) {
            if (testMethod.hasCodeAttribute()) {
                testMethod.getAnalysis().transform(transformer, new StructuredScope());
            }
        }

        for (Pair<String, JavaTypeInstance> hideThis : hideThese) {
            try {
                ClassFileField fileField = classFile.getFieldByName(hideThis.getFirst(), hideThis.getSecond());
                fileField.markHidden();
            } catch (NoSuchFieldException ignore) {
            }
        }
    }

    private boolean methodIsClassLookup(Method method) {
        if (method == null) return false;
        /*
         * Verify it matches expected pattern.
         */
        if (!method.getAccessFlags().contains(AccessFlagMethod.ACC_SYNTHETIC)) return false;
        if (!method.hasCodeAttribute()) return false;
        List<StructuredStatement> statements = MiscStatementTools.linearise(method.getAnalysis());
        if (statements == null) return false;
        MatchIterator<StructuredStatement> mi = new MatchIterator<StructuredStatement>(statements);
        WildcardMatch wcm1 = new WildcardMatch();

        List<LocalVariable> args = method.getMethodPrototype().getComputedParameters();
        if (args.size() != 1) return false;
        LocalVariable arg = args.get(0);
        if (!TypeConstants.STRING.equals(arg.getInferredJavaType().getJavaTypeInstance())) return false;

        Matcher<StructuredStatement> m =
                new MatchSequence(
                        new BeginBlock(null),
                        new StructuredTry(null, null),
                        new BeginBlock(null),
                        new StructuredReturn(BytecodeLoc.NONE, wcm1.getStaticFunction("forName",TypeConstants.CLASS, null, "forName", new LValueExpression(arg)), TypeConstants.CLASS),
                        new EndBlock(null),
                        new StructuredCatch(null, null, null, null),
                        new BeginBlock(null),
                        new StructuredThrow(
                                BytecodeLoc.NONE,
                                wcm1.getMemberFunction("initCause", "initCause",
                                        wcm1.getConstructorSimpleWildcard("nocd", TypeConstants.NOCLASSDEFFOUND_ERROR),
                                        wcm1.getExpressionWildCard("throwable"))
                        ),
                        new EndBlock(null),
                        new EndBlock(null)
                );

        // start
        mi.advance();
        return m.match(mi, null);
    }
}

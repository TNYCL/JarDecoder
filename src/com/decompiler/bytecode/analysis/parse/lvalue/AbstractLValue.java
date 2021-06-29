package com.decompiler.bytecode.analysis.parse.lvalue;

import com.decompiler.bytecode.analysis.parse.Expression;
import com.decompiler.bytecode.analysis.parse.LValue;
import com.decompiler.bytecode.analysis.parse.expression.misc.Precedence;
import com.decompiler.bytecode.analysis.parse.rewriters.CloneHelper;
import com.decompiler.bytecode.analysis.parse.utils.LValueUsageCollector;
import com.decompiler.bytecode.analysis.types.annotated.JavaAnnotatedTypeInstance;
import com.decompiler.bytecode.analysis.types.discovery.InferredJavaType;
import com.decompiler.entities.exceptions.ExceptionCheck;
import com.decompiler.state.TypeUsageCollector;
import com.decompiler.util.Troolean;
import com.decompiler.util.output.Dumper;
import com.decompiler.util.output.ToStringDumper;

public abstract class AbstractLValue implements LValue {
    private InferredJavaType inferredJavaType;

    public AbstractLValue(InferredJavaType inferredJavaType) {
        this.inferredJavaType = inferredJavaType;
    }

    String typeToString() {
        return inferredJavaType.toString();
    }

    @Override
    public InferredJavaType getInferredJavaType() {
        return inferredJavaType;
    }

    @Override
    public JavaAnnotatedTypeInstance getAnnotatedCreationType() {
        return null;
    }

    @Override
    public void collectTypeUsages(TypeUsageCollector collector) {
        inferredJavaType.getJavaTypeInstance().collectInto(collector);
    }

    @Override
    public void collectLValueUsage(LValueUsageCollector lValueUsageCollector) {
    }

    @Override
    public boolean doesBlackListLValueReplacement(LValue replace, Expression with) {
        return false;
    }

    @Override
    public LValue outerDeepClone(CloneHelper cloneHelper) {
        return cloneHelper.replaceOrClone(this);
    }

    @Override
    public boolean canThrow(ExceptionCheck caught) {
        return caught.mightCatchUnchecked();
    }

    @Override
    public boolean validIterator() {
        return true;
    }

    @Override
    public boolean isFakeIgnored() {
        return false;
    }

    @Override
    public final String toString() {
        return ToStringDumper.toString(this);
    }

    @Override
    public final Dumper dump(Dumper d) {
        return dumpWithOuterPrecedence(d, Precedence.WEAKEST, Troolean.NEITHER);
    }

    @Override
    public abstract Precedence getPrecedence();

    @Override
    public Dumper dump(Dumper d, boolean defines) {
        return dumpInner(d);
    }

    public abstract Dumper dumpInner(Dumper d);

    @Override
    public final Dumper dumpWithOuterPrecedence(Dumper d, Precedence outerP, Troolean isLhs) {
        Precedence innerP = getPrecedence();
        int cmp = innerP.compareTo(outerP);
        if (cmp > 0 || cmp == 0 && !innerP.isLtoR()) {
            d.separator("(");
            dumpInner(d);
            d.separator(")");
        } else {
            dumpInner(d);
        }
        return d;
    }

}

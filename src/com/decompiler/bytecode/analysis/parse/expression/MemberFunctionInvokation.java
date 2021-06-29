package com.decompiler.bytecode.analysis.parse.expression;

import java.util.List;

import com.decompiler.bytecode.analysis.loc.BytecodeLoc;
import com.decompiler.bytecode.analysis.parse.Expression;
import com.decompiler.bytecode.analysis.parse.expression.misc.Precedence;
import com.decompiler.bytecode.analysis.parse.rewriters.CloneHelper;
import com.decompiler.bytecode.analysis.parse.utils.*;
import com.decompiler.bytecode.analysis.types.JavaTypeInstance;
import com.decompiler.bytecode.analysis.types.MethodPrototype;
import com.decompiler.entities.constantpool.ConstantPool;
import com.decompiler.entities.constantpool.ConstantPoolEntryMethodRef;
import com.decompiler.util.StringUtils;
import com.decompiler.util.Troolean;
import com.decompiler.util.output.Dumper;

public class MemberFunctionInvokation extends AbstractMemberFunctionInvokation {
    private final boolean special;
    private final boolean isInitMethod;

    public MemberFunctionInvokation(BytecodeLoc loc, ConstantPool cp, ConstantPoolEntryMethodRef function, Expression object, JavaTypeInstance bestType, boolean special, List<Expression> args, List<Boolean> nulls) {
        super(loc, cp, function, object, bestType, args, nulls);
        // Most of the time a member function invokation for a constructor will
        // get pulled up into a constructorInvokation, however, when it's a super call, it won't.
        this.isInitMethod = function.isInitMethod();
        this.special = special;
    }

    private MemberFunctionInvokation(BytecodeLoc loc, ConstantPool cp, ConstantPoolEntryMethodRef function, Expression object, boolean special, List<Expression> args, List<Boolean> nulls) {
        super(loc, cp, function, object, args, nulls);
        this.isInitMethod = function.isInitMethod();
        this.special = special;
    }

    @Override
    public Expression deepClone(CloneHelper cloneHelper) {
        return new MemberFunctionInvokation(getLoc(), getCp(), getFunction(), cloneHelper.replaceOrClone(getObject()), special, cloneHelper.replaceOrClone(getArgs()), getNulls());
    }

    public MemberFunctionInvokation withReplacedObject(Expression object) {
        return new MemberFunctionInvokation(getLoc(), getCp(), getFunction(), object, special, getArgs(), getNulls());
    }

    @Override
    public Precedence getPrecedence() {
        return Precedence.PAREN_SUB_MEMBER;
    }

    @Override
    public Dumper dumpInner(Dumper d) {
        getObject().dumpWithOuterPrecedence(d, getPrecedence(), Troolean.NEITHER);

        MethodPrototype methodPrototype = getMethodPrototype();
        if (!isInitMethod) d.separator(".").methodName(getFixedName(), methodPrototype, false, false);
        d.separator("(");
        List<Expression> args = getArgs();
        boolean first = true;
        for (int x = 0; x < args.size(); ++x) {
            if (methodPrototype.isHiddenArg(x)) continue;
            Expression arg = args.get(x);
            first = StringUtils.comma(first, d);
            methodPrototype.dumpAppropriatelyCastedArgumentString(arg, d);
        }
        d.separator(")");
        return d;
    }

    public boolean isInitMethod() {
        return isInitMethod;
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;
        if (o == this) return true;
        if (!(o instanceof MemberFunctionInvokation)) return false;
        return getName().equals(((MemberFunctionInvokation) o).getName());
    }

    @Override
    public boolean equivalentUnder(Object o, EquivalenceConstraint constraint) {
        if (!super.equivalentUnder(o, constraint)) return false;
        if (o == this) return true;
        if (!(o instanceof MemberFunctionInvokation)) return false;
        MemberFunctionInvokation other = (MemberFunctionInvokation) o;
        return constraint.equivalent(getName(), other.getName());
    }
}

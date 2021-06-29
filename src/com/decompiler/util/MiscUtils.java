package com.decompiler.util;

import java.util.regex.Pattern;

import com.decompiler.bytecode.analysis.parse.Expression;
import com.decompiler.bytecode.analysis.parse.LValue;
import com.decompiler.bytecode.analysis.parse.expression.LValueExpression;
import com.decompiler.bytecode.analysis.parse.lvalue.LocalVariable;
import com.decompiler.bytecode.analysis.types.JavaTypeInstance;
import com.decompiler.util.functors.Predicate;

public class MiscUtils {
    public static <T> boolean xor(T a, T b, T required) {
        boolean b1 = a.equals(required);
        boolean b2 = b.equals(required);
        return b1 ^ b2;
    }

    public static Predicate<String> mkRegexFilter(String pat, boolean anywhere) {
        if (pat == null) {
            return new Predicate<String>() {
                @Override
                public boolean test(String in) {
                    return true;
                }
            };
        }

        final boolean positive = !pat.startsWith("!");
        if (!positive) {
            pat = pat.substring(1);
        }

        if (anywhere) pat = "^.*" + pat + ".*$";
        final Pattern p = Pattern.compile(pat);
        return new Predicate<String>() {
            @Override
            public boolean test(String in) {
                boolean matches = p.matcher(in).matches();
                return positive == matches;
            }
        };
    }

    /* Sometimes, we want a no-op inside an if statement, and we don't want to ugly things
     * up by letting code analysers say we're no good kids, hanging around wasting time.
     */
    public static void handyBreakPoint() {
    }

    public static boolean isThis(Expression obj, JavaTypeInstance thisType) {
        if (!(obj instanceof LValueExpression)) return false;
        LValue thisExp = ((LValueExpression) obj).getLValue();
        return isThis(thisExp, thisType);
    }

    public static boolean isThis(LValue thisExp, JavaTypeInstance thisType) {
        if (!(thisExp instanceof LocalVariable)) return false;
        LocalVariable lv = (LocalVariable)thisExp;
        if (!(lv.getIdx() == 0 && MiscConstants.THIS.equals(lv.getName().getStringName()))) return false;
        if (!thisType.equals(lv.getInferredJavaType().getJavaTypeInstance())) return false;
        return true;
    }
}

package com.decompiler.bytecode.analysis.opgraph.op4rewriters.util;

import java.util.Map;
import java.util.Set;

import com.decompiler.bytecode.analysis.parse.Expression;
import com.decompiler.bytecode.analysis.parse.expression.MemberFunctionInvokation;
import com.decompiler.bytecode.analysis.parse.expression.StaticFunctionInvokation;
import com.decompiler.bytecode.analysis.parse.utils.Pair;
import com.decompiler.bytecode.analysis.types.JavaTypeInstance;
import com.decompiler.bytecode.analysis.types.RawJavaType;
import com.decompiler.bytecode.analysis.types.TypeConstants;
import com.decompiler.util.collections.MapFactory;
import com.decompiler.util.collections.SetFactory;

public class BoxingHelper {
    @SuppressWarnings("unchecked")
    private static Set<Pair<String, String>> unboxing = SetFactory.newSet(
            Pair.make(TypeConstants.boxingNameInt, "intValue"),
            Pair.make(TypeConstants.boxingNameLong, "longValue"),
            Pair.make(TypeConstants.boxingNameDouble, "doubleValue"),
            Pair.make(TypeConstants.boxingNameShort, "shortValue"),
            Pair.make(TypeConstants.boxingNameByte, "byteValue"),
            Pair.make(TypeConstants.boxingNameBoolean, "booleanValue")
    );

    private static Map<String, String> unboxingByRawName;

    @SuppressWarnings("unchecked")
    private static Set<Pair<String, String>> boxing = SetFactory.newSet(
            Pair.make(TypeConstants.boxingNameInt, "valueOf"),
            Pair.make(TypeConstants.boxingNameLong, "valueOf"),
            Pair.make(TypeConstants.boxingNameDouble, "valueOf"),
            Pair.make(TypeConstants.boxingNameShort, "valueOf"),
            Pair.make(TypeConstants.boxingNameByte, "valueOf"),
            Pair.make(TypeConstants.boxingNameBoolean, "valueOf")
    );

    static {
        unboxingByRawName = MapFactory.newMap();
        for (Pair<String, String> pair : unboxing) {
            unboxingByRawName.put(pair.getFirst(), pair.getSecond());
        }
    }

    public static Expression sugarUnboxing(MemberFunctionInvokation memberFunctionInvokation) {
        String name = memberFunctionInvokation.getName();
        JavaTypeInstance type = memberFunctionInvokation.getObject().getInferredJavaType().getJavaTypeInstance();
        String rawTypeName = type.getRawName();
        Pair<String, String> testPair = Pair.make(rawTypeName, name);
        if (unboxing.contains(testPair)) {
            Expression expression = memberFunctionInvokation.getObject();
            return expression;
        }
        return memberFunctionInvokation;
    }

    public static String getUnboxingMethodName(JavaTypeInstance type) {
        return unboxingByRawName.get(type.getRawName());
    }

    public static Expression sugarBoxing(StaticFunctionInvokation staticFunctionInvokation) {
        String name = staticFunctionInvokation.getName();
        JavaTypeInstance type = staticFunctionInvokation.getClazz();
        if (staticFunctionInvokation.getArgs().size() != 1) return staticFunctionInvokation;
        Expression arg1 = staticFunctionInvokation.getArgs().get(0);
        String rawTypeName = type.getRawName();
        Pair<String, String> testPair = Pair.make(rawTypeName, name);
        if (boxing.contains(testPair)) {
            JavaTypeInstance argType = arg1.getInferredJavaType().getJavaTypeInstance();
            if (argType.implicitlyCastsTo(type, null)) {
                return staticFunctionInvokation.getArgs().get(0);
            }
        }
        return staticFunctionInvokation;
    }

    public static boolean isBoxedTypeInclNumber(JavaTypeInstance type) {
        if (RawJavaType.getUnboxedTypeFor(type) != null) return true;
        if (type.getRawName().equals(TypeConstants.boxingNameNumber)) return true;
        return false;
    }

    public static boolean isBoxedType(JavaTypeInstance type) {
        return (RawJavaType.getUnboxedTypeFor(type) != null);
    }


}

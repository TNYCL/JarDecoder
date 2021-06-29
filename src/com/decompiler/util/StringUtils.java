package com.decompiler.util;

import java.util.Collection;

import com.decompiler.util.output.Dumper;

public class StringUtils {

    public static boolean dot(boolean first, StringBuilder sb) {
        if (!first) sb.append(".");
        return false;
    }

    public static boolean comma(boolean first, StringBuilder sb) {
        if (!first) sb.append(", ");
        return false;
    }

    public static boolean comma(boolean first, Dumper d) {
        if (!first) d.separator(", ");
        return false;
    }

    public static boolean space(boolean first, Dumper d) {
        if (!first) d.print(" ");
        return false;
    }

    public static String join(String[] in, String sep) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String s : in) {
            if (first) {
                first = false;
            } else {
                sb.append(sep);
            }
            sb.append(s);
        }
        return sb.toString();
    }

    public static String join(Collection<String> in, String sep) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String s : in) {
            if (first) {
                first = false;
            } else {
                sb.append(sep);
            }
            sb.append(s);
        }
        return sb.toString();
    }


}

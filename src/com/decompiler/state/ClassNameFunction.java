package com.decompiler.state;

import java.util.Map;

public interface ClassNameFunction {
    Map<String, String> apply(Map<String, String> names);
}

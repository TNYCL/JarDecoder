package com.decompiler.util.getopt;

import java.util.Map;

public interface GetOptSinkFactory<T> extends PermittedOptionProvider {
    T create(Map<String, String> opts);
}

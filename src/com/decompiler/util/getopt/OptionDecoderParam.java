package com.decompiler.util.getopt;

import com.decompiler.util.functors.TrinaryFunction;

public interface OptionDecoderParam<T, ARG> extends TrinaryFunction<String, ARG, Options, T> {
    String getRangeDescription();

    String getDefaultValue();
}
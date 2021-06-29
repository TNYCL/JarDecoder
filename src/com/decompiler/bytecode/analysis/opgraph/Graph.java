package com.decompiler.bytecode.analysis.opgraph;

import java.util.List;

public interface Graph<T> {
    List<T> getSources();
    List<T> getTargets();
}

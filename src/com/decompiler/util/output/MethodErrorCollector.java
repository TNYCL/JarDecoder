package com.decompiler.util.output;

import com.decompiler.bytecode.analysis.types.JavaTypeInstance;
import com.decompiler.entities.Method;

public interface MethodErrorCollector {
    void addSummaryError(Method method, String s);

    static class SummaryDumperMethodErrorCollector implements MethodErrorCollector {
        private final JavaTypeInstance type;
        private final SummaryDumper summaryDumper;

        public SummaryDumperMethodErrorCollector(JavaTypeInstance type, SummaryDumper summaryDumper) {
            this.type = type;
            this.summaryDumper = summaryDumper;
        }

        @Override
        public void addSummaryError(Method method, String s) {
            summaryDumper.notifyError(type, method, s);
        }
    }
}

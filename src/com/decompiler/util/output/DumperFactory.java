package com.decompiler.util.output;

import com.decompiler.bytecode.analysis.types.JavaTypeInstance;
import com.decompiler.state.TypeUsageInformation;

public interface DumperFactory {

    Dumper getNewTopLevelDumper(JavaTypeInstance classType, SummaryDumper summaryDumper, TypeUsageInformation typeUsageInformation, IllegalIdentifierDump illegalIdentifierDump);

    // If we support line numbers, we'll be wrapped around the top level dumper.
    Dumper wrapLineNoDumper(Dumper dumper);

    ProgressDumper getProgressDumper();

    SummaryDumper getSummaryDumper();

    ExceptionDumper getExceptionDumper();

    DumperFactory getFactoryWithPrefix(String prefix, int version);
}

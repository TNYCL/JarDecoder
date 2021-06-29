package com.decompiler.apiunreleased;

import com.decompiler.api.ClassFileSource;
import com.decompiler.util.AnalysisType;

// TODO : Experimental API - before moving, snip ClassFileSource link.
public interface ClassFileSource2 extends ClassFileSource {
    /**
     * CFR would like to know about all classes contained within the jar at {@code jarPath}
     *
     * @param jarPath path to a jar.
     * @return @{link JarContent} for this jar.
     */
    JarContent addJarContent(String jarPath, AnalysisType analysisType);
}

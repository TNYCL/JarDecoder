package com.decompiler.apiunreleased;

import java.util.Collection;
import java.util.Map;

import com.decompiler.util.AnalysisType;

public interface JarContent {
    /**
     * @return All files which we may want to process.
     */
    Collection<String> getClassFiles();

    /**
     * Nb: Java has a perfectly good {@link java.util.jar.Manifest} class since 1.2
     * which could be used here.  This simplifies the content.
     * @return Map of manifest entries.
     */
    Map<String, String> getManifestEntries();

    /**
     * @return source of data - jar? war?
     */
    AnalysisType getAnalysisType();
}

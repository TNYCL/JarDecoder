package com.decompiler.state;

import java.util.Collection;
import java.util.Map;

import com.decompiler.apiunreleased.JarContent;
import com.decompiler.util.AnalysisType;

public class JarContentImpl implements JarContent {
    private final Collection<String> classFiles;
    private final Map<String, String> manifestEntries;
    private final AnalysisType analysisType;

    JarContentImpl(Collection<String> classFiles, Map<String, String> manifestEntries, AnalysisType analysisType) {
        this.classFiles = classFiles;
        this.manifestEntries = manifestEntries;
        this.analysisType = analysisType;
    }

    @Override
    public Collection<String> getClassFiles() {
        return classFiles;
    }

    @Override
    public Map<String, String> getManifestEntries() {
        return manifestEntries;
    }

    @Override
    public AnalysisType getAnalysisType() {
        return analysisType;
    }
}

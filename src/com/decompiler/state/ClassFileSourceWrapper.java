package com.decompiler.state;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

import com.decompiler.api.ClassFileSource;
import com.decompiler.apiunreleased.ClassFileSource2;
import com.decompiler.apiunreleased.JarContent;
import com.decompiler.bytecode.analysis.parse.utils.Pair;
import com.decompiler.util.AnalysisType;

/*
 * Compatibility for old class file source.
 *
 * I guess I picked the wrong day to commit to an API.
 */
public class ClassFileSourceWrapper implements ClassFileSource2 {
    private final ClassFileSource classFileSource;

    public ClassFileSourceWrapper(ClassFileSource classFileSource) {
        this.classFileSource = classFileSource;
    }

    @Override
    public JarContent addJarContent(String jarPath, AnalysisType type) {
        return new JarContentImpl(
            classFileSource.addJar(jarPath),
                Collections.<String, String>emptyMap(), type);
    }

    @Override
    public void informAnalysisRelativePathDetail(String usePath, String classFilePath) {
        classFileSource.informAnalysisRelativePathDetail(usePath, classFilePath);
    }

    @Override
    public Collection<String> addJar(String jarPath) {
        return classFileSource.addJar(jarPath);
    }

    @Override
    public String getPossiblyRenamedPath(String path) {
        return classFileSource.getPossiblyRenamedPath(path);
    }

    @Override
    public Pair<byte[], String> getClassFileContent(String path) throws IOException {
        return classFileSource.getClassFileContent(path);
    }
}

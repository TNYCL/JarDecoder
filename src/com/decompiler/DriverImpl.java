package com.decompiler;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.decompiler.api.CfrDriver;
import com.decompiler.api.ClassFileSource;
import com.decompiler.api.OutputSinkFactory;
import com.decompiler.apiunreleased.ClassFileSource2;
import com.decompiler.state.ClassFileSourceChained;
import com.decompiler.state.ClassFileSourceImpl;
import com.decompiler.state.ClassFileSourceWrapper;
import com.decompiler.state.DCCommonState;
import com.decompiler.util.AnalysisType;
import com.decompiler.util.collections.ListFactory;
import com.decompiler.util.getopt.Options;
import com.decompiler.util.getopt.OptionsImpl;
import com.decompiler.util.output.DumperFactory;
import com.decompiler.util.output.InternalDumperFactoryImpl;
import com.decompiler.util.output.SinkDumperFactory;

public class DriverImpl implements CfrDriver {
    private final Options options;
    private final ClassFileSource2 classFileSource;
    private final OutputSinkFactory outputSinkFactory;

    public DriverImpl(ClassFileSource source, OutputSinkFactory outputSinkFactory, Options options, boolean fallbackToDefaultSource) {
        if (options == null) {
            options = new OptionsImpl(new HashMap<String, String>());
        }
        ClassFileSource2 tmpSource;
        if (source == null) {
            tmpSource = new ClassFileSourceImpl(options);
        } else {
            tmpSource = source instanceof ClassFileSource2 ? (ClassFileSource2)source : new ClassFileSourceWrapper(source);
            if (fallbackToDefaultSource) {
                tmpSource = new ClassFileSourceChained(Arrays.asList(tmpSource, new ClassFileSourceImpl(options)));
            }
        }
        this.outputSinkFactory = outputSinkFactory;
        this.options = options;
        this.classFileSource = tmpSource;
    }

    @Override
    public void analyse(List<String> toAnalyse) {
        /*
         * There's an interesting question here - do we want to skip inner classes, if we've been given a wildcard?
         * (or a wildcard expanded by the operating system).
         *
         * Assume yes.
         */
        boolean skipInnerClass = toAnalyse.size() > 1 && options.getOption(OptionsImpl.SKIP_BATCH_INNER_CLASSES);

        // Can't sort a 1.6 singleton list.
        toAnalyse = ListFactory.newList(toAnalyse);
        Collections.sort(toAnalyse);
        for (String path : toAnalyse) {
            // TODO : We shouldn't have to discard state here.  But we do, because
            // it causes test fails.  (used class name table retains useful symbols).
            classFileSource.informAnalysisRelativePathDetail(null, null);
            // Note - both of these need to be reset, as they have caches.
            DCCommonState dcCommonState = new DCCommonState(options, classFileSource);
            DumperFactory dumperFactory = outputSinkFactory != null ?
                    new SinkDumperFactory(outputSinkFactory, options) :
                    new InternalDumperFactoryImpl(options);

            AnalysisType type = options.getOption(OptionsImpl.ANALYSE_AS);
            if (type == null || type == AnalysisType.DETECT) {
                type = dcCommonState.detectClsJar(path);
            }

            if (type == AnalysisType.JAR || type == AnalysisType.WAR) {
                Driver.doJar(dcCommonState, path, type, dumperFactory);
            } else if (type == AnalysisType.CLASS) {
                Driver.doClass(dcCommonState, path, skipInnerClass, dumperFactory);
            }
        }
    }
}

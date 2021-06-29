package com.decompiler.bytecode.analysis.variables;

import com.decompiler.entities.attributes.AttributeLocalVariableTable;
import com.decompiler.entities.constantpool.ConstantPool;

public class VariableNamerFactory {
    public static VariableNamer getNamer(AttributeLocalVariableTable source, ConstantPool cp) {
        if (source == null) return new VariableNamerDefault();
        return new VariableNamerHinted(source.getLocalVariableEntryList(), cp);
    }
}

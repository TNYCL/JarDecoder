package com.decompiler.entities.constantpool;

import com.decompiler.bytecode.analysis.types.StackType;
import com.decompiler.entities.AbstractConstantPoolEntry;
import com.decompiler.util.bytestream.ByteData;
import com.decompiler.util.output.Dumper;

public class ConstantPoolEntryDouble extends AbstractConstantPoolEntry implements ConstantPoolEntryLiteral {
    private final double value;

    public ConstantPoolEntryDouble(ConstantPool cp, ByteData data) {
        super(cp);
        this.value = data.getDoubleAt(1);
    }

    @Override
    public long getRawByteLength() {
        return 9;
    }

    @Override
    public void dump(Dumper d) {
        d.print("CONSTANT_Double " + value);
    }

    public double getValue() {
        return value;
    }

    @Override
    public StackType getStackType() {
        return StackType.DOUBLE;
    }
}

package com.decompiler.entities.constantpool;

import com.decompiler.bytecode.analysis.types.StackType;
import com.decompiler.entities.AbstractConstantPoolEntry;
import com.decompiler.util.bytestream.ByteData;
import com.decompiler.util.output.Dumper;

public class ConstantPoolEntryLong extends AbstractConstantPoolEntry implements ConstantPoolEntryLiteral {
    private final long value;

    public ConstantPoolEntryLong(ConstantPool cp, ByteData data) {
        super(cp);
        this.value = data.getLongAt(1);
    }

    @Override
    public long getRawByteLength() {
        return 9;
    }

    @Override
    public void dump(Dumper d) {
        d.print("CONSTANT_Long " + value);
    }

    @Override
    public String toString() {
        return "CONSTANT_Long[" + value + "]";
    }

    public long getValue() {
        return value;
    }

    @Override
    public StackType getStackType() {
        return StackType.LONG;
    }
}

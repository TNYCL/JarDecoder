package com.decompiler.entities.constantpool;

import com.decompiler.bytecode.analysis.types.StackType;
import com.decompiler.entities.AbstractConstantPoolEntry;
import com.decompiler.util.bytestream.ByteData;
import com.decompiler.util.output.Dumper;

public class ConstantPoolEntryInteger extends AbstractConstantPoolEntry implements ConstantPoolEntryLiteral {
    private static final long OFFSET_OF_BYTES = 1;

    private final int value;

    public ConstantPoolEntryInteger(ConstantPool cp, ByteData data) {
        super(cp);
        this.value = data.getS4At(OFFSET_OF_BYTES);
    }

    @Override
    public long getRawByteLength() {
        return 5;
    }

    @Override
    public void dump(Dumper d) {
        d.print("CONSTANT_Integer value=" + value);
    }

    public int getValue() {
        return value;
    }

    @Override
    public StackType getStackType() {
        return StackType.INT;
    }

    @Override
    public String toString() {
        return ("CONSTANT_Integer value=" + value);
    }
}

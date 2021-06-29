package com.decompiler.entities.constantpool;

import com.decompiler.bytecode.analysis.stack.StackDelta;
import com.decompiler.bytecode.analysis.types.JavaTypeInstance;
import com.decompiler.entities.AbstractConstantPoolEntry;
import com.decompiler.util.bytestream.ByteData;
import com.decompiler.util.output.Dumper;

public class ConstantPoolEntryNameAndType extends AbstractConstantPoolEntry {
    private static final long OFFSET_OF_NAME_INDEX = 1;
    private static final long OFFSET_OF_DESCRIPTOR_INDEX = 3;

    private final int nameIndex;
    private final int descriptorIndex;
    private StackDelta[] stackDelta = new StackDelta[2];

    public ConstantPoolEntryNameAndType(ConstantPool cp, ByteData data) {
        super(cp);
        this.nameIndex = data.getU2At(OFFSET_OF_NAME_INDEX);
        this.descriptorIndex = data.getU2At(OFFSET_OF_DESCRIPTOR_INDEX);
    }

    @Override
    public long getRawByteLength() {
        return 5;
    }

    @Override
    public void dump(Dumper d) {
        d.print("CONSTANT_NameAndType nameIndex=" + nameIndex + ", descriptorIndex=" + descriptorIndex);
    }

    @Override
    public String toString() {
        return "CONSTANT_NameAndType nameIndex=" + nameIndex + ", descriptorIndex=" + descriptorIndex;
    }

    public ConstantPoolEntryUTF8 getName() {
        return getCp().getUTF8Entry(nameIndex);
    }

    public ConstantPoolEntryUTF8 getDescriptor() {
        return getCp().getUTF8Entry(descriptorIndex);
    }

    public JavaTypeInstance decodeTypeTok() {
        return ConstantPoolUtils.decodeTypeTok(getDescriptor().getValue(), getCp());
    }

    public StackDelta getStackDelta(boolean member) {
        int idx = member ? 1 : 0;
        ConstantPool cp = getCp();
        if (stackDelta[idx] == null)
            stackDelta[idx] = ConstantPoolUtils.parseMethodPrototype(member, cp.getUTF8Entry(descriptorIndex), cp);
        return stackDelta[idx];
    }
}

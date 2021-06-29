package com.decompiler.entities.constantpool;

import com.decompiler.bytecode.analysis.parse.utils.QuotingUtils;
import com.decompiler.bytecode.analysis.types.*;
import com.decompiler.entities.AbstractConstantPoolEntry;
import com.decompiler.entities.bootstrap.MethodHandleBehaviour;
import com.decompiler.util.bytestream.ByteData;
import com.decompiler.util.output.Dumper;

public class ConstantPoolEntryMethodHandle extends AbstractConstantPoolEntry {
    private static final long OFFSET_OF_REFERENCE_KIND = 1;
    private static final long OFFSET_OF_REFERENCE_INDEX = 2;

    private final MethodHandleBehaviour referenceKind;
    private final int referenceIndex;

    public ConstantPoolEntryMethodHandle(ConstantPool cp, ByteData data) {
        super(cp);
        this.referenceKind = MethodHandleBehaviour.decode(data.getS1At(OFFSET_OF_REFERENCE_KIND));
        this.referenceIndex = data.getU2At(OFFSET_OF_REFERENCE_INDEX);
    }

    @Override
    public long getRawByteLength() {
        return 4;
    }

    @Override
    public void dump(Dumper d) {
        d.print(this.toString());
    }

    public MethodHandleBehaviour getReferenceKind() {
        return referenceKind;
    }

    public ConstantPoolEntryMethodRef getMethodRef() {
        return getCp().getMethodRefEntry(referenceIndex);
    }

    public ConstantPoolEntryFieldRef getFieldRef() {
        return getCp().getFieldRefEntry(referenceIndex);
    }

    public boolean isFieldRef() {
        switch (referenceKind) {
            case GET_FIELD:
            case GET_STATIC:
            case PUT_FIELD:
            case PUT_STATIC:
                return true;
            default:
                return false;
        }
    }

    public String getLiteralName() {
        if (isFieldRef()) {
            return QuotingUtils.enquoteString(getFieldRef().getLocalName());
        } else {
            return getMethodRef().getMethodPrototype().toString();
        }
    }

    @Override
    public String toString() {
        return "MethodHandle value=" + referenceKind + "," + referenceIndex;
    }
}

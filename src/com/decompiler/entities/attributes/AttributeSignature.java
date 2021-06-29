package com.decompiler.entities.attributes;

import com.decompiler.entities.constantpool.ConstantPool;
import com.decompiler.entities.constantpool.ConstantPoolEntryUTF8;
import com.decompiler.util.bytestream.ByteData;
import com.decompiler.util.output.Dumper;

/*
 * See 4.4.4 in Java class file format.
 *
 * ClassSignature:
 *   FormalTypeParametersopt SuperclassSignature SuperinterfaceSignature*
 *
 * FormalTypeParameters:
 *   <FormalTypeParameter+>
 *
 * FormalTypeParameter:
 *   Identifier ClassBound InterfaceBound*
 *
 * ClassBound:
 *  : FieldTypeSignatureopt
 *
 * InterfaceBound:
 *  : FieldTypeSignature
 *
 * SuperclassSignature:
 *   ClassTypeSignature
 *
 * SuperinterfaceSignature:
 *   ClassTypeSignature
 *
 * FieldTypeSignature:
 *   ClassTypeSignature
 *   ArrayTypeSignature
 *   TypeVariableSignature
 */
public class AttributeSignature extends Attribute {
    public static final String ATTRIBUTE_NAME = "Signature";

    private static final long OFFSET_OF_ATTRIBUTE_LENGTH = 2;
    private static final long OFFSET_OF_REMAINDER = 6;

    private final int length;
    private final ConstantPoolEntryUTF8 signature;

    public AttributeSignature(ByteData raw, ConstantPool cp) {
        this.length = raw.getS4At(OFFSET_OF_ATTRIBUTE_LENGTH);
        this.signature = cp.getUTF8Entry(raw.getU2At(OFFSET_OF_REMAINDER));
    }

    @Override
    public String getRawName() {
        return ATTRIBUTE_NAME;
    }

    @Override
    public Dumper dump(Dumper d) {
        return d.print("Signature : " + signature);
    }

    @Override
    public long getRawByteLength() {
        return OFFSET_OF_REMAINDER + length;
    }

    public ConstantPoolEntryUTF8 getSignature() {
        return signature;
    }
}

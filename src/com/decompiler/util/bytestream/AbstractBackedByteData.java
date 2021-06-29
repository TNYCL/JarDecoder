package com.decompiler.util.bytestream;

import java.io.DataInputStream;

import com.decompiler.util.ConfusedDecompilerException;

public abstract class AbstractBackedByteData implements ByteData {

    abstract DataInputStream rawDataAsStream(int offset, int length);

    @Override
    public int getS4At(long o) throws ConfusedDecompilerException {
        // Let's find an EFFICIENT way to do this later!
        DataInputStream dis = rawDataAsStream((int) o, 4);
        try {
            return dis.readInt();
        } catch (Exception e) {
            throw new ConfusedDecompilerException(e);
        }
    }

    @Override
    public double getDoubleAt(long o) throws ConfusedDecompilerException {
        DataInputStream dis = rawDataAsStream((int) o, 8);
        try {
            return dis.readDouble();
        } catch (Exception e) {
            throw new ConfusedDecompilerException(e);
        }
    }

    @Override
    public float getFloatAt(long o) throws ConfusedDecompilerException {
        DataInputStream dis = rawDataAsStream((int) o, 8);
        try {
            return dis.readFloat();
        } catch (Exception e) {
            throw new ConfusedDecompilerException(e);
        }
    }

    @Override
    public long getLongAt(long o) throws ConfusedDecompilerException {
        DataInputStream dis = rawDataAsStream((int) o, 8);
        try {
            return dis.readLong();
        } catch (Exception e) {
            throw new ConfusedDecompilerException(e);
        }
    }

    @Override
    public short getS2At(long o) throws ConfusedDecompilerException {
        // Let's find an EFFICIENT way to do this later!
        DataInputStream dis = rawDataAsStream((int) o, 2);
        try {
            return dis.readShort();
        } catch (Exception e) {
            throw new ConfusedDecompilerException(e);
        }
    }

    @Override
    public int getU2At(long o) throws ConfusedDecompilerException {
        // Let's find an EFFICIENT way to do this later!
        DataInputStream dis = rawDataAsStream((int) o, 2);
        try {
            return dis.readUnsignedShort();
        } catch (Exception e) {
            throw new ConfusedDecompilerException(e);
        }
    }

    @Override
    public short getU1At(long o) throws ConfusedDecompilerException {
        // Let's find an EFFICIENT way to do this later!
        DataInputStream dis = rawDataAsStream((int) o, 1);
        try {
            return (short) dis.readUnsignedByte();
        } catch (Exception e) {
            throw new ConfusedDecompilerException(e);
        }
    }
}

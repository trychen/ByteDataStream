package com.trychen.bytedatastream;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

public class DataInput extends DataInputStream {
    public DataInput(InputStream in) {
        super(in);
    }

    public DataInput(byte[] bytes) {
        super(new ByteArrayInputStream(bytes));
    }

    public byte[] readBytes() throws IOException {
        int length = readInt();
        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            bytes[i] = readByte();
        }
        return bytes;
    }

    public Date readDate() throws IOException {
        return new Date(readLong());
    }
}

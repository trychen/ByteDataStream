package com.trychen.bytedatastream;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;

public class DataOutput extends DataOutputStream {
    public DataOutput() {
        super(new ByteVectorStream());
    }

    public DataOutput(byte[] bytes) throws IOException {
        super(new ByteVectorStream());
        writeBytes(bytes);
    }

    public void writeBytes(byte[] bytes) throws IOException {
        writeInt(bytes.length);
        write(bytes);
    }

    public void writeDate(Date date) throws IOException {
        writeLong(date.getTime());
    }

    public void write(Object... objects) throws IOException {
        write(ByteSerialization.serialize(objects));
    }

    public synchronized byte[] toByteArray() {
        return ((ByteVectorStream) out).toByteArray();
    }
}

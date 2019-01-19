package com.trychen.bytedatastream;

import java.io.DataOutputStream;
import java.io.IOException;
import java.time.LocalTime;
import java.util.Date;
import java.util.UUID;

public class DataOutput extends DataOutputStream {
    public DataOutput() {
        super(new ByteVectorStream());
    }

    public DataOutput(byte[] bytes) throws IOException {
        super(new ByteVectorStream());
        writeBytes(bytes);
    }
    
    public void write(Object... objects) throws IOException {
        write(ByteSerialization.serialize(objects));
    }
    
    public void writeBytes(byte[] bytes) throws IOException {
        writeInt(bytes.length);
        write(bytes);
    }

    public void writeDate(Date date) throws IOException {
        writeLong(date.getTime());
    }

    public void writeEnum(Enum en) throws IOException {
        writeUTF(en.name());
    }

    public void writeUUID(UUID uuid) throws IOException {
        writeUTF(uuid.toString());
    }

    public void writeLocalTime(LocalTime localTime) throws IOException {
        if (localTime.getNano() == 0) {
            if (localTime.getSecond() == 0) {
                if (localTime.getMinute() == 0) {
                    writeByte(~localTime.getHour());
                } else {
                    writeByte(localTime.getHour());
                    writeByte(~localTime.getMinute());
                }
            } else {
                writeByte(localTime.getHour());
                writeByte(localTime.getMinute());
                writeByte(~localTime.getSecond());
            }
        } else {
            writeByte(localTime.getHour());
            writeByte(localTime.getMinute());
            writeByte(localTime.getSecond());
            writeInt(localTime.getNano());
        }
    }

    public synchronized byte[] toByteArray() {
        return ((ByteVectorStream) out).toByteArray();
    }
}

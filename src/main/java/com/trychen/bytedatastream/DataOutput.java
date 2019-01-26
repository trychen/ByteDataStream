package com.trychen.bytedatastream;

import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
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


    public void write(Object[] objects, Type[] types) throws IOException {
        write(ByteSerialization.serialize(objects, types));
    }

    public void write(Object object, Type type) throws IOException {
        write(ByteSerialization.serialize(object, type));
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

    public void writeArray(Object[] objects) throws IOException {
//        int length = Array.getLength(objects);
        writeInt(objects.length);
        for (Object object : objects) {
            write(object, objects.getClass().getComponentType());
        }
//        for (int i = 0; i < length; i++) {
//            write(Array.get(objects, i), objects.getClass().getComponentType());
//        }
    }

    public void writeList(List list, Type type) throws IOException {
        writeInt(list.size());
        Class actualType = TypeUtils.findListActualType(type);
        if (actualType == null) throw new RuntimeException("Couldn't find the actual type for list");
        for (Object o : list) {
            write(o, actualType);
        }
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

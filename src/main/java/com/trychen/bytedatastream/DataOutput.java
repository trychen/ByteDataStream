package com.trychen.bytedatastream;

import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalTime;
import java.util.*;

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

    public void writeList(List list, Class type) throws IOException {
        writeCollection(list, type);
    }
    public void writeSet(Set set, Class type) throws IOException {
        writeCollection(set, type);
    }

    public void writeCollection(Collection collection, Class type) throws IOException {
        if (type == null) throw new RuntimeException("Couldn't find the actual type for list");
        writeInt(collection.size());
        for (Object o : collection) {
            write(o, type);
        }
    }

    public void writeMap(Map map, Type type) throws IOException {
        writeInt(map.size());
        Class[] actualType = TypeUtils.findTwoParameterizedType(type);
        if (actualType == null) throw new RuntimeException("Couldn't find the actual type for map");
        for (Map.Entry entry : (Set<Map.Entry>)map.entrySet()) {
            write(entry.getKey(), actualType[0]);
            write(entry.getValue(), actualType[1]);
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

package com.trychen.bytedatastream;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.time.LocalTime;
import java.util.*;

public class DataInput extends DataInputStream {
    public DataInput(InputStream in) {
        super(in);
    }

    public DataInput(byte[] bytes) {
        super(new ByteArrayInputStream(bytes));
    }

    public <T> T read(Class<T> type) throws IOException {
        return (T) ByteSerialization.deserialize(this, type);
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

    /**
     * @param clazz component type
     */
    public Object readArray(Class clazz) throws IOException {
        int length = readInt();
        Object array = Array.newInstance(clazz, length);
        for (int i = 0; i < length; i++) {
            Array.set(array, i, read(clazz));
        }
        return array;
    }

    public LocalTime readLocalTime() throws IOException {
        int hour = readByte();
        int minute = 0;
        int second = 0;
        int nano = 0;
        if (hour < 0) {
            hour = ~hour;
        } else {
            minute = readByte();
            if (minute < 0) {
                minute = ~minute;
            } else {
                second = readByte();
                if (second < 0) {
                    second = ~second;
                } else {
                    nano = readInt();
                }
            }
        }
        return LocalTime.of(hour, minute, second, nano);
    }

    public <T extends Enum<T>> T readEnum(Class<T> clazz) throws IOException {
        return Enum.valueOf(clazz, readUTF());
    }

    public List readList(Class type) throws IOException {
        return readCollection(new ArrayList(), type);
    }

    public Set readSet(Class type) throws IOException {
        return readCollection(new HashSet(), type);
    }

    public <T extends Collection> T readCollection(T collection, Class type) throws IOException {
        if (type == null) throw new RuntimeException("Couldn't find the actual type for list");
        int size = readInt();
        for (int i = 0; i < size; i++) {
            collection.add(read(type));
        }
        return collection;
    }

    public Map readMap(Type type) throws IOException {
        int size = readInt();
        Map map = new HashMap(size);
        Class[] actualType = TypeUtils.findTwoParameterizedType(type);
        if (actualType == null) throw new RuntimeException("Couldn't find the actual type for list");
        for (int i = 0; i < size; i++) {
            map.put(read(actualType[0]), read(actualType[1]));
        }
        return map;
    }

    public UUID readUUID() throws IOException {
        return UUID.fromString(readUTF());
    }
}

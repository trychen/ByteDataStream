package com.trychen.bytedatastream;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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

    public List readList(Type type) throws IOException {
        int size = readInt();
        List list = new ArrayList(size);
        Class actualType = TypeUtils.findListActualType(type);
        if (actualType == null) throw new RuntimeException("Couldn't find the actual type for list");
        for (int i = 0; i < size; i++) {
            list.add(read(actualType));
        }
        return list;
    }

    public UUID readUUID() throws IOException {
        return UUID.fromString(readUTF());
    }
}

package com.trychen.bytedatastream;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.sql.Time;
import java.time.LocalTime;
import java.util.*;

public final class ByteSerialization {
    private static Map<Type, ByteSteamSerializer> serializers = new HashMap<>();
    private static Map<Type, ByteSteamDeserializer> deserializers = new HashMap<>();

    private ByteSerialization() {
    }

    public static void serialize(DataOutput out, Object object) throws IOException {
        serialize(out, object, object.getClass());
    }

    public static void serialize(DataOutput out, Object object, Type type) throws IOException {
        ByteSteamSerializer byteSerializable = getSerializer(type);

        if (byteSerializable == null && object instanceof ByteSteamSerializable)
            ((ByteSteamSerializable) object).serialize(out);
        else if (object instanceof Enum) out.writeEnum((Enum) object);
        else if (byteSerializable == null) new RuntimeException("Couldn't find any serialization");
        else byteSerializable.serialize(out, object);
    }

    public static byte[] serialize(Object... object) throws IOException {
        DataOutput out = new DataOutput();
        for (Object o : object) {
            serialize(out, o);
        }
        return out.toByteArray();
    }

    public static byte[] serialize(Object[] object, Type[] types) throws IOException {
        if (object.length != types.length) throw new IllegalArgumentException("objects.length != types.length");
        DataOutput out = new DataOutput();
        for (int i = 0; i < object.length; i++) {
            serialize(out, object[i], types[i]);
        }
        return out.toByteArray();
    }

    public static Object deserialize(DataInput in, Type type) throws IOException {
        ByteSteamDeserializer byteSerializable = getDeserializer(type);
        if (byteSerializable == null) {
            if (type instanceof Class && Enum.class.isAssignableFrom((Class) type)) return in.readEnum((Class) type);
            return null;
        }
        return byteSerializable.deserialize(in);
    }

    public static Object[] deserialize(byte[] data, Type... types) throws IOException {
        DataInput in = new DataInput(data);
        Object[] objects = new Object[types.length];
        for (int i = 0; i < types.length; i++) {
            ByteSteamDeserializer deserializer = getDeserializer(types[i]);
            if (deserializer == null)
                throw new IllegalArgumentException("Couldn't find any serialization for type " + types[i].getTypeName());
            objects[i] = deserialize(in, types[i]);
        }
        return objects;
    }

    public static Object deserialize(byte[] data, Type type) throws IOException {
        DataInput in = new DataInput(data);
        ByteSteamDeserializer deserializer = getDeserializer(type);
        if (deserializer == null)
            throw new IllegalArgumentException("Couldn't find any deserializer for " + type.getTypeName());
        return deserializer.deserialize(in);
    }

    public static ByteSteamSerializer getSerializer(Type type) {
        return serializers.get(type);
    }

    public static ByteSteamDeserializer getDeserializer(Type type) {
        if (!deserializers.containsKey(type)) {
            ByteSteamDeserializer deserializer = null;
            if (type instanceof Class && ByteSteamDeserializable.class.isAssignableFrom((Class<?>) type)) try {
                Method deserialize = ((Class<?>) type).getDeclaredMethod("deserialize", DataInput.class);
                register(type, deserializer = new OwnDeserializer(deserialize));
            } catch (Exception e) {
                e.printStackTrace();
            }
            deserializers.put(type, deserializer);
        }
        return deserializers.get(type);
    }

    public static Map<Type, ByteSteamSerializer> getSerializers() {
        return serializers;
    }

    public static Map<Type, ByteSteamDeserializer> getDeserializers() {
        return deserializers;
    }

    public static <T> void register(Class<T> clazz, ByteSteamSerializer<T> serializer, ByteSteamDeserializer<T> deserializer) {
        if (clazz.isPrimitive()) {
            Class<?> box = TypeUtils.CLASS_PRIMITIVE_MAPPING.get(clazz);
            if (box == null) return;
            register(box, serializer);
            register(box, deserializer);
        }
        register(clazz, serializer);
        register(clazz, deserializer);
    }

    public static <T> void register(Class<T> clazz, ByteSteamDeserializer<T> deserializer) {
        deserializers.put(clazz, deserializer);
    }

    public static <T> void register(Class<T> clazz, ByteSteamSerializer<T> serializer) {
        serializers.put(clazz, serializer);
    }

    public static <T> void register(Type type, ByteSteamDeserializer<T> deserializer) {
        deserializers.put(type, deserializer);
    }

    public static <T> void register(Type type, ByteSteamSerializer<T> serializer) {
        serializers.put(type, serializer);
    }

    static {
        register(int.class, (out, o) -> out.writeInt(o), in -> in.readInt());
        register(long.class, (out, o) -> out.writeLong(o), in -> in.readLong());
        register(float.class, (out, o) -> out.writeFloat(o), in -> in.readFloat());
        register(double.class, (out, o) -> out.writeDouble(o), in -> in.readDouble());
        register(boolean.class, (out, o) -> out.writeBoolean(o), in -> in.readBoolean());
        register(short.class, (out, o) -> out.writeShort(o), in -> in.readShort());
        register(byte.class, (out, o) -> out.writeByte(o), in -> in.readByte());
        register(byte[].class, (out, o) -> out.writeBytes(o), in -> in.readBytes());

        register(String.class, (out, o) -> out.writeUTF(o), in -> in.readUTF());
        register(Date.class, (out, o) -> out.writeDate(o), in -> in.readDate());
        register(LocalTime.class, (out, o) -> out.writeLocalTime(o), in -> in.readLocalTime());
        register(Locale.class, (out, o) -> out.writeUTF(o.toLanguageTag()), in -> Locale.forLanguageTag(in.readUTF()));
        register(UUID.class, (out, o) -> out.writeUUID(o), in -> in.readUUID());
    }

}

package com.trychen.bytedatastream;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface TypeUtils {
    Map<Class<?>, Class<?>> CLASS_PRIMITIVE_MAPPING = new HashMap<Class<?>, Class<?>>() {{
        put(byte.class, Byte.class);
        put(short.class, Short.class);
        put(int.class, Integer.class);
        put(long.class, Long.class);
        put(float.class, Float.class);
        put(double.class, Double.class);
        put(boolean.class, Boolean.class);
        put(char.class, Character.class);
        put(void.class, Void.class);
    }};

    static Class findListActualType(Type type) {
        Class classType = null;
        if (!(type instanceof ParameterizedType)) return null;

        ParameterizedType pType = (ParameterizedType) type;
        if (pType.getActualTypeArguments().length <= 0) return null;

        Type clazz = pType.getActualTypeArguments()[0];
        if (clazz instanceof Class) {
            classType = (Class) clazz;
        }
        return classType;
    }

    static boolean isList(Type type) {
        return type instanceof ParameterizedType && ((ParameterizedType) type).getRawType() instanceof Class && List.class.isAssignableFrom((Class<?>) ((ParameterizedType) type).getRawType());
    }

    static boolean isArray(Object obj) {
        return obj != null && obj.getClass().isArray();
    }

    static boolean isArray(Type type) {
        return type instanceof Class && ((Class) type).isArray();
    }
}

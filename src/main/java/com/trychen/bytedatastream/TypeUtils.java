package com.trychen.bytedatastream;

import com.trychen.bytedatastream.reflect.ParameterizedTypeImpl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

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

    static ParameterizedType getParameterizedType(Class rawType, Type... parameters) {
        return new ParameterizedTypeImpl(rawType, parameters, null);
    }

    static Class findOneParameterizedType(Type type) {
        Class classType = null;
        if (!(type instanceof ParameterizedType)) return null;

        ParameterizedType pType = (ParameterizedType) type;
        if (pType.getActualTypeArguments().length != 1) return null;

        Type clazz = pType.getActualTypeArguments()[0];
        if (clazz instanceof Class) {
            classType = (Class) clazz;
        }
        return classType;
    }

    static Class[] findTwoParameterizedType(Type type) {
        Class[] classType = new Class[2];
        if (!(type instanceof ParameterizedType)) return null;

        ParameterizedType pType = (ParameterizedType) type;
        if (pType.getActualTypeArguments().length != 2) return null;

        Type keyClass = pType.getActualTypeArguments()[0];
        Type valueClass = pType.getActualTypeArguments()[1];
        if (keyClass instanceof Class && valueClass instanceof Class) {
            classType[0] = (Class) keyClass;
            classType[1] = (Class) valueClass;
            return classType;
        } else {
            return null;
        }
    }

    static boolean isList(Type type) {
        return type instanceof ParameterizedType && ((ParameterizedType) type).getRawType() instanceof Class && List.class.isAssignableFrom((Class<?>) ((ParameterizedType) type).getRawType());
    }

    static boolean isSet(Type type) {
        return type instanceof ParameterizedType && ((ParameterizedType) type).getRawType() instanceof Class && Set.class.isAssignableFrom((Class<?>) ((ParameterizedType) type).getRawType());
    }

    static boolean isArray(Object obj) {
        return obj != null && obj.getClass().isArray();
    }

    static boolean isArray(Type type) {
        return type instanceof Class && ((Class) type).isArray();

    }

    static boolean isMap(Type type) {
        return type instanceof ParameterizedType && ((ParameterizedType) type).getRawType() instanceof Class && Map.class.isAssignableFrom((Class<?>) ((ParameterizedType) type).getRawType());
    }
}

package com.trychen.bytedatastream;

import java.util.HashMap;
import java.util.Map;

public interface Utils {
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

}

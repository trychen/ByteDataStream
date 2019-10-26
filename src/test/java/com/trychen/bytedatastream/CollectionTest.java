package com.trychen.bytedatastream;

import org.junit.Test;

import java.lang.reflect.Type;
import java.util.*;

import static org.junit.Assert.assertArrayEquals;

public class CollectionTest {
    @Test
    public void test() throws Exception {
        Type TYPE_TOKEN = TypeUtils.getParameterizedType(Collection.class, String.class);

        Map<Integer, String> map = new HashMap<>();
        map.put(1, "Hello");
        map.put(2, "Why");
        map.put(3, "List");

        byte[] serialize = ByteSerialization.serialize(map.values(), TYPE_TOKEN);
        Collection<String> result = (Collection<String>) ByteSerialization.deserialize(serialize, TYPE_TOKEN);
        assertArrayEquals(map.values().toArray(), result.toArray());
    }
}

package com.trychen.bytedatastream;

import org.junit.Test;

import java.lang.reflect.Type;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class MapTest {
    @Test
    public void test() throws Exception {
        Type TYPE_TOKEN = TypeUtils.getParameterizedType(Map.class, String.class, Date.class);

        Map<String, Date> map = new HashMap<>();
        map.put("Hello", new Date(2019, 4, 20));
        map.put("Why", new Date(1999, 12, 30));
        map.put("List", new Date(2050, 8, 2));

        byte[] serialize = ByteSerialization.serialize(map, TYPE_TOKEN);
        Map<String, Date> result = (Map<String, Date>) ByteSerialization.deserialize(serialize, TYPE_TOKEN);

        assertEquals(map.size(), result.size());
        assertEquals(new Date(2019, 4, 20), result.get("Hello"));
        assertEquals(new Date(2050, 8, 2), result.get("List"));
    }
}

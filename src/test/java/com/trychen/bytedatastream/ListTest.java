package com.trychen.bytedatastream;

import org.junit.Test;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;

public class ListTest {
    @Test
    public void test() throws Exception {
        Type TYPE_TOKEN = TypeUtils.getParameterizedType(List.class, String.class);

        List<String> strings = new ArrayList<>();
        strings.add("Hello");
        strings.add("Why");
        strings.add("List");

        byte[] serialize = ByteSerialization.serialize(strings, TYPE_TOKEN);
        List<String> result = (List<String>) ByteSerialization.deserialize(serialize, TYPE_TOKEN);
        assertArrayEquals(strings.toArray(), result.toArray());
    }
}

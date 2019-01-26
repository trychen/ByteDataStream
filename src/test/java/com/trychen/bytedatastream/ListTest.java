package com.trychen.bytedatastream;

import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ListTest {
    private List<String> token;
    @Test
    public void test() throws Exception {
        Type TYPE_TOKEN = ListTest.class.getDeclaredField("token").getGenericType();

        List<String> strings = new ArrayList<>();
        strings.add("Hello");
        strings.add("Why");
        strings.add("List");

        byte[] serialize = ByteSerialization.serialize(strings, TYPE_TOKEN);
        List<String> result = (List<String>) ByteSerialization.deserialize(serialize, TYPE_TOKEN);
        assertArrayEquals(strings.toArray(), result.toArray());
    }
}

package com.trychen.bytedatastream;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Type;

public class MultiArrayTest {
    @Test
    public void test() throws Exception {
        int[] objects = {1, 0, 1, 0};
        byte[] serialize = ByteSerialization.serialize(new Object[]{objects}, new Type[]{int[].class});

        Object deserialize = ByteSerialization.deserialize(serialize, objects.getClass());
        Assert.assertArrayEquals(objects, (int[]) deserialize);
    }
}

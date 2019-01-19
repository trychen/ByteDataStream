package com.trychen.bytedatastream;

import org.junit.Test;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.*;

public class SimpleTest {
    private final SimpleData begin = new SimpleData(1651, 165161868L, 0.15161, 1.165F, (short)5, (byte) 0x2, true, new byte[]{0x1, 0x2, 0x4},  "Hello World", new Date(), SimpleData.Type.HELLO, UUID.randomUUID());

    @Test
    public void data() throws IOException {
        byte[] bytes = ByteSerialization.serialize(begin);
        SimpleData data = (SimpleData)ByteSerialization.deserialize(bytes, SimpleData.class);
        assertEquals(begin, data);
    }

    @Test
    public void simple() throws IOException {
        Object[] data = {"Hello", 15};
        byte[] bytes = ByteSerialization.serialize(data);
        Object[] result1 = ByteSerialization.deserialize(bytes, String.class, int.class);
        Object[] result2 = ByteSerialization.deserialize(bytes, String.class, Integer.class);
        assertArrayEquals(data, result1);
        assertArrayEquals(data, result2);
    }
}

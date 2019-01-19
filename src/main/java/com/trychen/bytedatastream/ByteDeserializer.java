package com.trychen.bytedatastream;

import java.io.IOException;

@FunctionalInterface
public interface ByteDeserializer<T> {
    T deserialize(DataInput in) throws IOException;
}

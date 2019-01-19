package com.trychen.bytedatastream;

import java.io.IOException;

@FunctionalInterface
public interface ByteSerializer<T> {
    void serialize(DataOutput out, T object) throws IOException;
}

package com.trychen.bytedatastream;

import java.io.IOException;

@FunctionalInterface
public interface ByteSteamDeserializer<T> {
    T deserialize(DataInput in) throws IOException;
}

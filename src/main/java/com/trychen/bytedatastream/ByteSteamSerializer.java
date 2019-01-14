package com.trychen.bytedatastream;

import java.io.IOException;

@FunctionalInterface
public interface ByteSteamSerializer<T> {
    void serialize(DataOutput out, T object) throws IOException;
}

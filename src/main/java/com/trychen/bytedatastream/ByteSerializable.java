package com.trychen.bytedatastream;

import java.io.DataOutputStream;
import java.io.IOException;

public interface ByteSerializable {
    void serialize(DataOutput out) throws IOException;
}

package com.trychen.bytedatastream;

import java.io.DataOutputStream;
import java.io.IOException;

public interface ByteSteamSerializable {
    void serialize(DataOutput out) throws IOException;
}

package com.trychen.bytedatastream;

import com.github.mouse0w0.fastreflection.FastReflection;
import com.github.mouse0w0.fastreflection.MethodAccessor;

import java.io.IOException;
import java.lang.reflect.Method;

public class OwnDeserializer implements ByteDeserializer {
    private MethodAccessor methodAccessor;

    public OwnDeserializer(Method method) throws Exception {
        this.methodAccessor = FastReflection.create(method);
    }

    @Override
    public Object deserialize(DataInput in) throws IOException {
        if (methodAccessor == null) return null;
        try {
            return methodAccessor.invoke(null, in);
        } catch (Exception e) {
            e.printStackTrace();
            methodAccessor = null;
        }
        return null;
    }

    public MethodAccessor getMethodAccessor() {
        return methodAccessor;
    }

    public void setMethodAccessor(MethodAccessor methodAccessor) {
        this.methodAccessor = methodAccessor;
    }
}

package com.trychen.bytedatastream;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

public class ByteVectorStream extends OutputStream {
    /**
     * The content of this vector.
     */
    protected byte[] data;

    /**
     * Actual number of bytes in this vector.
     */
    protected int length;

    public ByteVectorStream() {
        this(64);
    }

    /**
     * Constructs a new {@link ByteVectorStream ByteVector} with the given initial
     * size.
     *
     * @param initialSize
     *            the initial size of the byte vector to be constructed.
     */
    public ByteVectorStream(final int initialSize) {
        data = new byte[initialSize];
    }

    public synchronized byte[] toByteArray() {
        return Arrays.copyOf(data, length);
    }

    /**
     * Enlarge this byte vector so that it can receive n more bytes.
     *
     * @param size
     *            number of additional bytes that this byte vector should be
     *            able to receive.
     */
    private void enlarge(final int size) {
        int length1 = 2 * data.length;
        int length2 = length + size;
        byte[] newData = new byte[length1 > length2 ? length1 : length2];
        System.arraycopy(data, 0, newData, 0, length);
        data = newData;
    }

    /**
     * Puts a byte into this byte vector. The byte vector is automatically
     * enlarged if necessary.
     *
     * @param b
     *            a byte.
     */
    @Override
    public void write(int b) throws IOException {
        int length = this.length;
        if (length + 1 > data.length) {
            enlarge(1);
        }
        data[length++] = (byte) b;
        this.length = length;
    }
}

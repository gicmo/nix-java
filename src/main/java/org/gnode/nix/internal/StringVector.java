package org.gnode.nix.internal;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.annotation.*;

@Platform(value = "linux", include = "<vector>")
@Name("std::vector<std::string>")
public class StringVector extends Pointer {
    static {
        Loader.load();
    }

    public StringVector(String... array) {
        this(array.length);
        put(array);
    }

    public StringVector() {
        allocate();
    }

    public StringVector(long n) {
        allocate(n);
    }

    public StringVector(Pointer p) {
        super(p);
    }

    private native void allocate();

    private native void allocate(@Cast("size_t") long n);

    public native long size();

    public native void resize(@Cast("size_t") long n);

    @Index
    @ByRef
    public native String get(@Cast("size_t") long i);

    public native StringVector put(@Cast("size_t") long i, String value);

    public StringVector put(String... array) {
        if (size() < array.length) {
            resize(array.length);
        }
        for (int i = 0; i < array.length; i++) {
            put(i, array[i]);
        }
        return this;
    }
}
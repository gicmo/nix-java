package org.gnode.nix.internal;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.annotation.*;
import org.gnode.nix.Value;

import java.util.ArrayList;
import java.util.List;

@Platform(value = "linux", include = {"<nix/Value.hpp>"}, link = {"nix"})
@Name("std::vector<nix::Value>")
public class ValueVector extends Pointer {
    static {
        Loader.load();
    }

    public ValueVector(List<Value> lst) {
        allocate(lst.size());
        put(lst);
    }

    private native void allocate(@Cast("size_t") long n);

    private native long size();

    private native void resize(@Cast("size_t") long n);

    @Index
    private native
    @ByRef
    Value get(@Cast("size_t") long i);

    private native ValueVector put(@Cast("size_t") long i, Value value);

    private ValueVector put(List<Value> lst) {
        if (size() != lst.size()) {
            resize(lst.size());
        }
        for (int i = 0; i < lst.size(); i++) {
            put(i, lst.get(i));
        }
        return this;
    }

    public ArrayList<Value> getValues() {
        ArrayList<Value> values = new ArrayList<Value>();
        for (int i = 0; i < size(); i++) {
            values.add(get(i));
        }
        return values;
    }
}
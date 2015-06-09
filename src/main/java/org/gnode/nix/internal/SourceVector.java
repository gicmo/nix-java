package org.gnode.nix.internal;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.annotation.*;
import org.gnode.nix.Source;

import java.util.ArrayList;
import java.util.List;

@Platform(value = "linux", include = {"<nix/Source.hpp>"}, link = {"nix"})
@Name("std::vector<nix::Source>")
public class SourceVector extends Pointer {
    static {
        Loader.load();
    }

    public SourceVector(List<Source> lst) {
        allocate(lst.size());
        put(lst);
    }

    private native void allocate(@Cast("size_t") long n);

    private native long size();

    private native void resize(@Cast("size_t") long n);

    @Index
    private native
    @ByRef
    Source get(@Cast("size_t") long i);

    private native SourceVector put(@Cast("size_t") long i, Source source);

    private SourceVector put(List<Source> lst) {
        if (size() != lst.size()) {
            resize(lst.size());
        }
        for (int i = 0; i < lst.size(); i++) {
            put(i, lst.get(i));
        }
        return this;
    }

    public ArrayList<Source> getSources() {
        ArrayList<Source> sources = new ArrayList<Source>();
        for (int i = 0; i < size(); i++) {
            sources.add(get(i));
        }
        return sources;
    }
}

package org.gnode.nix.internal;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.annotation.*;
import org.gnode.nix.DataArray;

import java.util.ArrayList;
import java.util.List;

@Platform(value = "linux", include = {"<nix/DataArray.hpp>"}, link = {"nix"})
@Name("std::vector<nix::DataArray>")
public class DataArrayVector extends Pointer {
    static {
        Loader.load();
    }

    public DataArrayVector(List<DataArray> lst) {
        allocate(lst.size());

        for (int i = 0; i < lst.size(); i++) {
            put(i, lst.get(i));
        }
    }

    private native void allocate(@Cast("size_t") long n);

    private native long size();

    @Index
    private native
    @ByRef
    DataArray get(@Cast("size_t") long i);

    private native DataArrayVector put(@Cast("size_t") long i, DataArray dataArray);

    public ArrayList<DataArray> getDataArrays() {
        ArrayList<DataArray> dataArrays = new ArrayList<DataArray>();
        for (int i = 0; i < size(); i++) {
            dataArrays.add(get(i));
        }
        return dataArrays;
    }
}

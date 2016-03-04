package org.g_node.nix;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.LongPointer;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.annotation.*;
import org.g_node.nix.internal.BuildLibs;

/**
 * <h1>NDSize</h1>
 * Used for setting extent and dimensions for data.
 */

@Properties(value = {
        @Platform(include = {"<nix/NDSize.hpp>"}),
        @Platform(value = "linux", link = BuildLibs.NIX_1, preload = BuildLibs.HDF5_7),
        @Platform(value = "macosx", link = BuildLibs.NIX, preload = BuildLibs.HDF5),
        @Platform(value = "windows",
                link = BuildLibs.NIX,
                preload = {BuildLibs.HDF5, BuildLibs.MSVCP120, BuildLibs.MSVCR120, BuildLibs.SZIP, BuildLibs.ZLIB})})
@Namespace("nix")
@Name("NDSizeBase<nix::ndsize_t>")
public class NDSize extends Pointer {
    static {
        Loader.load();
    }

    //--------------------------------------------------
    // Constructors
    //--------------------------------------------------

    /**
     * Constructor
     */
    public NDSize() {
        allocate();
    }

    private native void allocate();

    /**
     * Constructor
     *
     * @param rank rank of NDArray
     */
    public NDSize(@Cast("size_t") long rank) {
        allocate(rank);
    }

    private native void allocate(@Cast("size_t") long rank);

    /**
     * Constructor
     *
     * @param rank      rank of NDArray
     * @param fillValue fill value
     */
    public NDSize(@Cast("size_t") long rank, @Cast("nix::ndsize_t") long fillValue) {
        allocate(rank, fillValue);
    }

    private native void allocate(@Cast("size_t") long rank, @Cast("nix::ndsize_t") long fillValue);

    /**
     * Specify dimensions as array.
     *
     * @param args dimensions.
     */
    public NDSize(@Cast({"", "std::vector<int>&"}) @StdVector int[] args) {
        allocate(args);
    }

    private native void allocate(@Cast({"", "std::vector<int>&"}) @StdVector int[] args);

    /**
     * Get rank.
     *
     * @return rank.
     */
    public native
    @Name("size")
    long getSize();

    /**
     * Get product of elements.
     *
     * @return multiplicative product.
     */
    public native
    @Name("nelms")
    long getElementsProduct();

    private native
    @Cast("const nix::ndsize_t*")
    LongPointer data();

    /**
     * Get dimensions.
     *
     * @return dimensions array
     */
    public int[] getData() {
        LongPointer lp = data();
        int len = (int) getSize();
        int[] data = new int[len];
        for (int i = 0; i < len; i++) {
            data[i] = (int) lp.get(i);
        }
        return data;
    }

    /**
     * Fill dimensions with a value.
     *
     * @param value fill value.
     */
    public native void fill(@Cast("nix::ndsize_t") long value);

    /**
     * Check if empty.
     *
     * @return True if empty otherwise false.
     */
    public native
    @Name("empty")
    @Cast("bool")
    boolean isEmpty();

    //--------------------------------------------------
    // Overrides
    //--------------------------------------------------

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof NDSize)) {
            return false;
        }

        int[] dims1 = this.getData();
        int[] dims2 = ((NDSize) obj).getData();

        if (dims1.length != dims2.length) {
            return false;
        }

        for (int i = 0; i < dims1.length; i++) {
            if (dims1[i] != dims2[i]) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        StringBuilder data = new StringBuilder("[ ");
        int[] dims = getData();
        for (int i = 0; i < dims.length; i++) {
            data.append(dims[i]);
            if (i != dims.length - 1) {
                data.append(", ");
            } else {
                data.append(" ]");
            }
        }

        return "NDSize: {rank = " + this.getSize()
                + ", elements product = " + this.getElementsProduct()
                + ", data = " + data.toString() + "}";
    }
}

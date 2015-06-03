package org.gnode.nix;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.LongPointer;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.annotation.Name;
import org.bytedeco.javacpp.annotation.Namespace;
import org.bytedeco.javacpp.annotation.Platform;

@Platform(value = "linux",
        include = {"<nix/NDSize.hpp>"},
        link = {"nix"})
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
    public long[] getData() {
        LongPointer lp = data();
        int len = (int) getSize();
        long[] data = new long[len];
        for (int i = 0; i < len; i++) {
            data[i] = lp.get(i);
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
}

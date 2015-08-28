package org.g_node.nix;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.annotation.*;
import org.g_node.nix.internal.None;
import org.g_node.nix.internal.VectorUtils;
import org.g_node.nix.base.ImplContainer;
import org.g_node.nix.internal.BuildLibs;

import java.util.List;

/**
 * <h1>SetDimension</h1>
 * Dimension descriptor for a dimension that represents just a list or set of values.
 * <p>
 * The SetDimension is used in cases where data is given as a set or list. This can be just a collection of values but
 * also a list of recorded signals or a stack of images. Optionally an array of labels, one for each index of this
 * dimension, can be specified.
 *
 * @see Dimension
 * @see RangeDimension
 * @see SampledDimension
 */

@Properties(value = {
        @Platform(include = {"<nix/Dimensions.hpp>"}),
        @Platform(value = "linux", link = BuildLibs.NIX_1, preload = BuildLibs.HDF5_7),
        @Platform(value = "windows",
                link = BuildLibs.NIX,
                preload = {BuildLibs.HDF5, BuildLibs.MSVCP120, BuildLibs.MSVCR120, BuildLibs.SZIP, BuildLibs.ZLIB})})
@Namespace("nix")
public class SetDimension<T extends SetDimension> extends ImplContainer implements Comparable<T> {
    static {
        Loader.load();
    }

    //--------------------------------------------------
    // Constructors
    //--------------------------------------------------

    /**
     * Constructor that creates an uninitialized SetDimension.
     * <p>
     * Calling any method on an uninitialized dimension will throw a {@link java.lang.RuntimeException}.
     */
    public SetDimension() {
        allocate();
    }

    private native void allocate();

    //--------------------------------------------------
    // Base class methods
    //--------------------------------------------------

    public native
    @Cast("bool")
    boolean isNone();

    //--------------------------------------------------
    // Methods concerning SetDimension
    //--------------------------------------------------

    /**
     * The actual dimension that is described by the dimension descriptor.
     * <p>
     * The index of the dimension entity representing the dimension of the actual
     * data that is defined by this descriptor.
     *
     * @return The dimension index of the dimension.
     */
    public native
    @Name("index")
    @Cast("size_t")
    long getIndex();

    /**
     * The type of the dimension.
     * <p>
     * This field indicates whether the dimension is a SampledDimension, SetDimension or
     * RangeDimension.
     *
     * @return The dimension type. Constants specified in {@link DataType}
     * @see DataType
     */
    public native
    @Name("dimensionType")
    @ByVal
    @Cast("nix::DimensionType")
    int getDimensionType();

    private native
    @ByVal
    VectorUtils.StringVector labels();

    /**
     * Get the labels of the range dimension.
     * <p>
     * The labels serve as names for each index of the data at the respective
     * dimension.
     *
     * @return The labels of the dimension as a list of strings.
     */
    public List<String> getLabels() {
        return labels().getStrings();
    }

    private native void labels(@Const @ByRef VectorUtils.StringVector labels);

    private native void labels(@Const @ByVal None t);

    /**
     * Set the labels for the dimension.
     *
     * @param labels A list containing all new labels. If <tt>null</tt> removes the labels from the dimension.
     */
    public void setLabels(List<String> labels) {
        if (labels != null) {
            labels(new VectorUtils.StringVector(labels));
        } else {
            labels(new None());
        }
    }

    //--------------------------------------------------
    // Overrides
    //--------------------------------------------------

    @Override
    public int compareTo(T dimension) {
        if (this == dimension) {
            return 0;
        }
        return (int) (this.getIndex() - dimension.getIndex());
    }
}
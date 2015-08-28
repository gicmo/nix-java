package org.g_node.nix;

import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.annotation.*;
import org.g_node.nix.internal.None;
import org.g_node.nix.internal.VectorUtils;
import org.g_node.nix.base.ImplContainer;
import org.g_node.nix.internal.BuildLibs;
import org.g_node.nix.internal.OptionalUtils;

/**
 * <h1>RangeDimension</h1>
 * Dimension descriptor for a dimension that is irregularly sampled.
 * <p>
 * The RangeDimension covers cases when indexes of a dimension are mapped to other values
 * in a not regular fashion. A use-case for this would be for example irregularly sampled
 * time-series or certain kinds of histograms. To achieve the mapping of the indexes an
 * array of mapping values must be provided. Those values are stored in the dimensions {@link RangeDimension#setTicks(double[])}
 * property. In analogy to the sampled dimension a {@link RangeDimension#setUnit(String)} and a {@link RangeDimension#setLabel(String)} can be defined.
 *
 * @see Dimension
 * @see SampledDimension
 * @see SetDimension
 */

@Properties(value = {
        @Platform(include = {"<nix/Dimensions.hpp>"}),
        @Platform(value = "linux", link = BuildLibs.NIX_1, preload = BuildLibs.HDF5_7),
        @Platform(value = "windows",
                link = BuildLibs.NIX,
                preload = {BuildLibs.HDF5, BuildLibs.MSVCP120, BuildLibs.MSVCR120, BuildLibs.SZIP, BuildLibs.ZLIB})})
@Namespace("nix")
public class RangeDimension<T extends RangeDimension> extends ImplContainer implements Comparable<T> {
    static {
        Loader.load();
    }

    //--------------------------------------------------
    // Constructors
    //--------------------------------------------------

    /**
     * Constructor that creates an uninitialized RangeDimension.
     * <p>
     * Calling any method on an uninitialized dimension will throw a {@link java.lang.RuntimeException}.
     */
    public RangeDimension() {
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
    // Methods concerning RangeDimension
    //--------------------------------------------------

    /**
     * Tells if the RangeDimension uses the contents of a linked DataArray for ticks,
     * i.e. is an alias.
     *
     * @return bool true, if RangeDimension is an alias, false otherwise.
     */
    public native
    @Name("alias")
    boolean isAlias();

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
     * @return The dimension type.
     */
    public native
    @Name("dimensionType")
    @ByVal
    @Cast("nix::DimensionType")
    int getDimensionType();

    private native
    @ByVal
    OptionalUtils.OptionalString label();

    /**
     * Getter for the label of the dimension.
     * <p>
     * The label of a RangeDimension corresponds to the axis label
     * in a plot of the respective dimension.
     *
     * @return The label of the dimension. Returns <tt>null</tt> if not present.
     */
    public String getLabel() {
        OptionalUtils.OptionalString label = label();
        if (label.isPresent()) {
            return label.getString();
        }
        return null;
    }

    private native void label(@StdString String label);

    private native void label(@Const @ByVal None t);

    /**
     * Sets the label of the dimension. If <tt>null</tt> removes label.
     *
     * @param label The label of the dimension.
     */
    public void setLabel(String label) {
        if (label != null) {
            label(label);
        } else {
            label(new None());
        }
    }

    private native
    @ByVal
    OptionalUtils.OptionalString unit();

    /**
     * Gets the unit of a dimension.
     * <p>
     * The unit describes which SI unit applies to this dimension
     * and to its sampling interval.
     *
     * @return The unit of the dimension.
     */
    public String getUnit() {
        OptionalUtils.OptionalString unit = unit();
        if (unit.isPresent()) {
            return unit.getString();
        }
        return null;
    }

    private native void unit(@StdString String unit);

    private native void unit(@Const @ByVal None t);

    /**
     * Sets the unit of a dimension. If <tt>null</tt> removes the unit.
     *
     * @param unit The unit to set.
     */
    public void setUnit(String unit) {
        if (unit != null) {
            unit(unit);
        } else {
            unit(new None());
        }
    }

    private native
    @StdVector
    DoublePointer ticks();

    /**
     * Get the ticks of the dimension.
     * <p>
     * The ticks map the index of the data at the respective dimension to other
     * values. This can be used to store data that is sampled at irregular
     * intervals.
     *
     * @return A list with all ticks for the dimension.
     */
    public double[] getTicks() {
        return VectorUtils.convertPointerToArray(ticks());
    }

    /**
     * Set the ticks array for the dimension.
     * <p>
     * Ticks must be ordered in ascending order.
     *
     * @param ticks The new ticks for the dimension provided as an array.
     */
    public native
    @Name("ticks")
    void setTicks(@StdVector double[] ticks);

    /**
     * Returns the entry of the range dimension at a given index.
     *
     * @param index The index.
     * @return The tick at the given index.
     */
    public native
    @Name("tickAt")
    double getTickAt(@Cast("const size_t") long index);

    /**
     * Returns the index of the given position.
     * <p>
     * Method will return the index equal or larger than position
     *
     * @param position The position.
     * @return The respective index.
     */
    public native
    @Name("indexOf")
    @Cast("size_t")
    long getIndexOf(double position);

    private native
    @StdVector
    DoublePointer axis(@Cast("const size_t") long count, @Cast("const size_t") long startIndex);

    /**
     * Returns a list containing a number of ticks.
     * <p>
     * The result list contains a given number of ticks starting from a
     * starting index
     *
     * @param count      The number of ticks.
     * @param startIndex The starting index.
     * @return array containing the ticks.
     */
    public double[] getAxis(long count, long startIndex) {
        return VectorUtils.convertPointerToArray(axis(count, startIndex));
    }

    private native
    @StdVector
    DoublePointer axis(@Cast("const size_t") long count);

    /**
     * Returns a list containing a number of ticks.
     * <p>
     * The result list contains a given number of ticks starting from a
     * starting index
     *
     * @param count The number of ticks.
     * @return array containing the ticks.
     */
    public double[] getAxis(long count) {
        return VectorUtils.convertPointerToArray(axis(count));
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

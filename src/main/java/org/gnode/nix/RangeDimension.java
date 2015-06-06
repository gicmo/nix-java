package org.gnode.nix;

import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.annotation.*;
import org.gnode.nix.internal.None;
import org.gnode.nix.internal.OptionalString;
import org.gnode.nix.internal.Utils;

import java.util.List;

@Platform(value = "linux",
        include = {"<nix/Dimensions.hpp>"},
        link = {"nix"})
@Namespace("nix")
public class RangeDimension extends Dimension {
    static {
        Loader.load();
    }

    //--------------------------------------------------
    // Constructors
    //--------------------------------------------------

    /**
     * Constructor that creates an uninitialized RangeDimension.
     * <p/>
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
     * The actual dimension that is described by the dimension descriptor.
     * <p/>
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
     * <p/>
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
    OptionalString label();

    /**
     * Getter for the label of the dimension.
     * <p/>
     * The label of a RangeDimension corresponds to the axis label
     * in a plot of the respective dimension.
     *
     * @return The label of the dimension. {#link null} if not present.
     */
    public String getLabel() {
        OptionalString label = label();
        if (label.isPresent()) {
            return label.getString();
        }
        return null;
    }

    private native void label(@StdString String label);

    private native void label(@Const @ByVal None t);

    /**
     * Sets the label of the dimension. If {#link null} removes label.
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
    OptionalString unit();

    /**
     * Gets the unit of a dimension.
     * <p/>
     * The unit describes which SI unit applies to this dimension
     * and to its sampling interval.
     *
     * @return The unit of the dimension.
     */
    public String getUnit() {
        OptionalString unit = unit();
        if (unit.isPresent()) {
            return unit.getString();
        }
        return null;
    }

    private native void unit(@StdString String unit);

    private native void unit(@Const @ByVal None t);

    /**
     * Sets the unit of a dimension. If {#link null} removes the unit.
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
     * <p/>
     * The ticks map the index of the data at the respective dimension to other
     * values. This can be used to store data that is sampled at irregular
     * intervals.
     *
     * @return A list with all ticks for the dimension.
     */
    public List<Double> getTicks() {
        return Utils.convertPointerToList(ticks());
    }

    /**
     * Set the ticks array for the dimension.
     * <p/>
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
     * <p/>
     * Method will return the index closest to the given position.
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
     * Returns a vector containing a number of ticks.
     * <p/>
     * The result vector contains a given number of ticks starting from a
     * starting index
     *
     * @param count      The number of ticks.
     * @param startIndex The starting index.
     * @return list containing the ticks.
     */
    public List<Double> getAxis(long count, long startIndex) {
        return Utils.convertPointerToList(axis(count, startIndex));
    }

    private native
    @StdVector
    DoublePointer axis(@Cast("const size_t") long count);

    /**
     * Returns a vector containing a number of ticks.
     * <p/>
     * The result vector contains a given number of ticks starting from a
     * starting index
     *
     * @param count The number of ticks.
     * @return list containing the ticks.
     */
    public List<Double> getAxis(long count) {
        return Utils.convertPointerToList(axis(count));
    }
}

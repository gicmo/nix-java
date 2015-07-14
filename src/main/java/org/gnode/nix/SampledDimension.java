package org.gnode.nix;

import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.annotation.*;
import org.gnode.nix.base.ImplContainer;
import org.gnode.nix.internal.*;

import java.util.List;

@Properties(value = {
        @Platform(include = {"<nix/Dimensions.hpp>"}, link = "nix"),
        @Platform(value = "linux"),
        @Platform(value = "windows")})
@Namespace("nix")
public class SampledDimension<T extends SampledDimension> extends ImplContainer implements Comparable<T> {
    static {
        Loader.load();
    }

    //--------------------------------------------------
    // Constructors
    //--------------------------------------------------

    /**
     * Constructor that creates an uninitialized SampledDimension.
     * <p/>
     * Calling any method on an uninitialized dimension will throw a {@link java.lang.RuntimeException}.
     */
    public SampledDimension() {
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
    // Methods concerning SampledDimension
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
    OptionalUtils.OptionalString label();

    /**
     * Getter for the label of the dimension.
     * <p/>
     * The label of a SampledDimension corresponds to the axis label
     * in a plot of the respective dimension.
     *
     * @return The label of the dimension. {#link null} if not present.
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
    OptionalUtils.OptionalString unit();

    /**
     * Gets the unit of a dimension.
     * <p/>
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

    /**
     * Gets the sampling interval of the dimension.
     *
     * @return The sampling interval.
     */
    public native
    @Name("samplingInterval")
    double getSamplingInterval();

    /**
     * Sets the sampling interval of the dimension.
     *
     * @param interval The sampling interval to set.
     */
    public native
    @Name("samplingInterval")
    void setSamplingInterval(double interval);

    private native
    @ByVal
    OptionalUtils.OptionalDouble offset();

    /**
     * Gets the offset of the dimension.
     * <p/>
     * The offset defines at which position the sampling was started. The offset is
     * interpreted in the same unit as the sampling interval.
     * <p/>
     * By default the offset is 0.
     *
     * @return The offset of the SampledDimension.
     */
    public double getOffset() {
        OptionalUtils.OptionalDouble offset = offset();
        if (offset.isPresent()) {
            return offset.getDouble();
        }
        return 0.0;
    }

    /**
     * Sets the offset of the dimension.
     *
     * @param offset The offset of the dimension.
     */
    public native
    @Name("offset")
    void setOffset(double offset);

    /**
     * Returns the index of the given position.
     * <p/>
     * This method returns the index of the given position. Use this method for
     * example to find out which data point (index) relates to a given
     * time. Note: This method does not check if the position is within the
     * extent of the data!
     *
     * @param position The position
     * @return The respective index.
     */
    public native
    @Name("indexOf")
    @Cast("size_t")
    long getIndexOf(double position);

    /**
     * Returns the position of this dimension at a given index.
     * <p/>
     * This method returns the position at a given index. Use this method for
     * example to find the position that relates to a certain index. Note: This
     * method does not check if the index is the extent of the data!
     *
     * @param index The index.
     * @return The respective position
     */
    public native
    @Name("positionAt")
    double getPositionAt(@Cast("const size_t") long index);

    private native
    @StdVector
    DoublePointer axis(@Cast("const size_t") long count, @Cast("const size_t") long startIndex);

    /**
     * Returns a vector containing the positions defined by this
     * dimension.
     *
     * @param count      The number of indices
     * @param startIndex The start index
     * @return list containing the respective dimension.
     */
    public List<Double> getAxis(long count, long startIndex) {
        return VectorUtils.convertPointerToList(axis(count, startIndex));
    }

    private native
    @StdVector
    DoublePointer axis(@Cast("const size_t") long count);

    /**
     * Returns a list containing the positions defined by this
     * dimension with the start index set to 0.
     *
     * @param count The number of indices
     * @return list containing the respective dimension.
     */
    public List<Double> getAxis(long count) {
        return VectorUtils.convertPointerToList(axis(count));
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

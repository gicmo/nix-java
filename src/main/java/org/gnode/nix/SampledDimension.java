package org.gnode.nix;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.annotation.*;
import org.gnode.nix.internal.None;
import org.gnode.nix.internal.OptionalString;

@Platform(value = "linux",
        include = {"<nix/Dimensions.hpp>"},
        link = {"nix"})
@Namespace("nix")
public class SampledDimension extends Pointer {
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

    private native
    @Cast("bool")
    boolean isNone();

    /**
     * Checks if dimension is initialized
     *
     * @return true if initialized else false
     */
    public boolean isInitialized() {
        return !isNone();
    }

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
    OptionalString label();

    /**
     * Getter for the label of the dimension.
     * <p/>
     * The label of a SampledDimension corresponds to the axis label
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

}

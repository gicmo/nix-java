package org.gnode.nix;

import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.annotation.*;
import org.gnode.nix.internal.None;
import org.gnode.nix.internal.OptionalDouble;
import org.gnode.nix.internal.OptionalString;
import org.gnode.nix.internal.Utils;

import java.util.List;

@Platform(value = "linux",
        include = {"<nix/DataArray.hpp>"},
        link = {"nix"})
@Namespace("nix")
public class DataArray extends Pointer {
    static {
        Loader.load();
    }

    //--------------------------------------------------
    // Constructors
    //--------------------------------------------------

    /**
     * Constructor that creates an uninitialized DataArray.
     * <p/>
     * Calling any method on an uninitialized data array will throw a {@link java.lang.RuntimeException}
     * exception. The following code illustrates how to check if a data array is initialized:
     */
    public DataArray() {
        allocate();
    }

    private native void allocate();

    //--------------------------------------------------
    // Element getters and setters
    //--------------------------------------------------

    private native
    @ByVal
    OptionalString label();

    /**
     * Get the label for the values stored in the DataArray.
     *
     * @return The label of the data array. {#link null} if not present.
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
     * Set the label for the data stored. If {#link null} is passed the label is removed.
     *
     * @param label The label of the data array.
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
     * Get the unit of the data stored in this data array.
     *
     * @return The unit of the data array. {#link null} if not present.
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
     * Set the unit for the values stored in this DataArray. If {#link null} is passed the unit is removed.
     *
     * @param unit The unit of the data array.
     */
    public void setUnit(String unit) {
        if (unit != null) {
            unit(unit);
        } else {
            unit(new None());
        }
    }

    private native
    @ByVal
    OptionalDouble expansionOrigin();

    /**
     * Returns the expansion origin of the calibration polynom.
     * <p/>
     * The expansion origin is 0.0 by default.
     *
     * @return The expansion origin.
     */
    public double getExpansionOrigin() {
        OptionalDouble expansionOrigin = expansionOrigin();
        if (expansionOrigin.isPresent()) {
            return expansionOrigin.getDouble();
        }
        return 0.0;
    }

    /**
     * Set the expansion origin for the calibration.
     *
     * @param expansionOrigin The expansion origin for the calibration.
     */
    public native
    @Name("expansionOrigin")
    void SetExpansionOrigin(double expansionOrigin);

    /**
     * Set the polynom coefficients for the calibration.
     * <p/>
     * By default this is set to a two element vector of [0.0, 1.0] for a linear calibration
     * with zero offset.
     *
     * @param polynomCoefficients The new polynom coefficients for the calibration.
     */
    public native
    @Name("polynomCoefficients")
    void setPolynomCoefficients(@StdVector double[] polynomCoefficients);

    private native
    @StdVector
    DoublePointer polynomCoefficients();

    /**
     * Returns the polynom coefficients.
     *
     * @return The polynom coefficients for the calibration.
     */
    public List<Double> getPolynomCoefficients() {
        return Utils.convertPointerToList(polynomCoefficients());
    }

    private native void polynomCoefficients(@Const @ByVal None t);

    /**
     * Deleter for the polynomCoefficients attribute.
     */
    public void removePolynomCoefficients() {
        polynomCoefficients(new None());
    }

    //--------------------------------------------------
    // Methods concerning dimensions
    //--------------------------------------------------

    private native
    @StdVector
    Dimension dimensions();

    /**
     * Get all dimensions associated with this data array.
     *
     * @return The dimensions as a list
     */
    public List<Dimension> getDimensions() {
        return Utils.convertPointerToList(dimensions(), Dimension.class);
    }

    /**
     * Returns the number of dimensions stored in the DataArray.
     * <p/>
     * This matches the dimensionality of the data stored in this property.
     *
     * @return The number of dimensions.
     */
    public native
    @Name("dimensionCount")
    @Cast("size_t")
    long getDimensionCount();

    /**
     * Returns the Dimension object for the specified dimension of the data.
     *
     * @param id The index of the respective dimension.
     * @return The dimension object.
     */
    public native
    @ByVal
    Dimension getDimension(@Cast("size_t") long id);

    /**
     * Append a new SetDimension to the list of existing dimension descriptors.
     *
     * @return The newly created SetDimension.
     */
    public native
    @ByVal
    SetDimension appendSetDimension();

    /**
     * Append a new RangeDimension to the list of existing dimension descriptors.
     *
     * @param ticks The ticks of the RangeDimension to create.
     * @return The newly created RangeDimension
     */
    public native
    @ByVal
    RangeDimension appendRangeDimension(@StdVector double[] ticks);

    /**
     * Append a new SampledDimension to the list of existing dimension descriptors.
     *
     * @param samplingInterval The sampling interval of the SetDimension to create.
     * @return The newly created SampledDimension.
     */
    public native
    @ByVal
    SampledDimension appendSampledDimension(double samplingInterval);

    /**
     * Create a new SetDimension at a specified dimension index.
     * <p/>
     * This adds a new dimension descriptor of the type {@link SetDimension} that describes the dimension
     * of the data at the specified index.
     *
     * @param id The index of the dimension. Must be a value > 0 and <= `dimensionCount + 1`.
     * @return The created dimension descriptor.
     */
    public native
    @ByVal
    SetDimension createSetDimension(@Cast("size_t") long id);

    /**
     * Create a new RangeDimension at a specified dimension index.
     * <p/>
     * This adds a new dimension descriptor of the type {@link RangeDimension} that describes the dimension
     * of the data at the specified index.
     *
     * @param id    The index of the dimension. Must be a value > 0 and <= `dimensionCount + 1`.
     * @param ticks Vector with {@link RangeDimension#ticks}.
     * @return The created dimension descriptor.
     */
    public native
    @ByVal
    RangeDimension createRangeDimension(@Cast("size_t") long id, @StdVector double[] ticks);

    /**
     * Create a new SampledDimension at a specified dimension index.
     * <p/>
     * This adds a new dimension descriptor of the type {@link SampledDimension} that describes the dimension
     * of the data at the specified index.
     *
     * @param id               The index of the dimension. Must be a value > 0 and <= `dimensionCount + 1`.
     * @param samplingInterval The sampling interval of the dimension.
     * @return The created dimension descriptor.
     */
    public native
    @ByVal
    SampledDimension createSampledDimension(@Cast("size_t") long id, double samplingInterval);

    /**
     * Remove a dimension descriptor at a specified index.
     *
     * @param id The index of the dimension. Must be a value > 0 and < `getDimensionCount + 1`.
     */
    public native
    @Cast("bool")
    boolean deleteDimension(@Cast("size_t") long id);
}

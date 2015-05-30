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

}

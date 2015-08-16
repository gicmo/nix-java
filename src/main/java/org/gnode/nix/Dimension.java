package org.gnode.nix;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.annotation.*;
import org.gnode.nix.base.ImplContainer;

/**
 * <h1>Dimension</h1>
 * Instances of the Dimension subclasses are used to define the different dimensions of data in a DataArray.
 * <p>
 * The real dimension descriptor are defined in three subclasses: {@link RangeDimension},
 * {@link SampledDimension} and {@link SetDimension} .
 *
 * @see RangeDimension
 * @see SampledDimension
 * @see SetDimension
 */

@Properties(value = {
        @Platform(include = {"<nix/Dimensions.hpp>"}, link = "nix", preload = "hdf5"),
        @Platform(value = "linux"),
        @Platform(value = "windows")})
@Namespace("nix")
public class Dimension<T extends Dimension> extends ImplContainer implements Comparable<T> {
    static {
        Loader.load();
    }

    //--------------------------------------------------
    // Constructors
    //--------------------------------------------------

    /**
     * Constructor that creates an uninitialized Dimension.
     * <p>
     * Calling any method on an uninitialized dimension will throw a {@link java.lang.RuntimeException}.
     */
    public Dimension() {
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
    // Methods concerning Dimension
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
     * @return The dimension type. Constant specified in {@link DimensionType}
     * @see DimensionType
     * @see RangeDimension
     * @see SampledDimension
     * @see SetDimension     *
     */
    public native
    @Name("dimensionType")
    @ByVal
    @Cast("nix::DimensionType")
    int getDimensionType();

    /**
     * Returns as {@link SetDimension}.
     *
     * @return SetDimension object
     */
    public native
    @ByVal
    SetDimension asSetDimension();

    /**
     * Returns as {@link SampledDimension}.
     *
     * @return SampledDimension object
     */
    public native
    @ByVal
    SampledDimension asSampledDimension();

    /**
     * Returns as {@link RangeDimension}.
     *
     * @return RangeDimension object
     */
    public native
    @ByVal
    RangeDimension asRangeDimension();

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

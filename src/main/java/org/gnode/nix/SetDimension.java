package org.gnode.nix;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.annotation.*;
import org.gnode.nix.base.ImplContainer;
import org.gnode.nix.internal.None;
import org.gnode.nix.internal.VectorUtils;

import java.util.List;

@Properties(value = {
        @Platform(include = {"<nix/Dimensions.hpp>"}, link = "nix"),
        @Platform(value = "linux"),
        @Platform(value = "windows")})
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
     * <p/>
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
    VectorUtils.StringVector labels();

    /**
     * Get the labels of the range dimension.
     * <p/>
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
     * @param labels A list containing all new labels. If {@link null} removes the labels from the dimension.
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
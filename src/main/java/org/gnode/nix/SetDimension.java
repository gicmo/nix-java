package org.gnode.nix;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.annotation.*;
import org.gnode.nix.base.ImplContainer;
import org.gnode.nix.internal.None;
import org.gnode.nix.internal.StringVector;
import org.gnode.nix.internal.Utils;

import java.util.List;

@Platform(value = "linux",
        include = {"<nix/Dimensions.hpp>"},
        link = {"nix"})
@Namespace("nix")
public class SetDimension extends ImplContainer implements Comparable<SetDimension> {
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
    StringVector labels();

    /**
     * Get the labels of the range dimension.
     * <p/>
     * The labels serve as names for each index of the data at the respective
     * dimension.
     *
     * @return The labels of the dimension as a list of strings.
     */
    public List<String> getLabels() {
        return Utils.convertStringVectorToList(labels());
    }

    private native void labels(@Const @ByVal StringVector labels);

    private native void labels(@Const @ByVal None t);

    /**
     * Set the labels for the dimension.
     *
     * @param labels A list containing all new labels. If {@link null} removes the labels from the dimension.
     */
    public void setLabels(List<String> labels) {
        if (labels != null) {
            labels(Utils.convertListToStringVector(labels));
        } else {
            labels(new None());
        }
    }

    @Override
    public int compareTo(SetDimension setDimension) {
        if (this == setDimension) {
            return 0;
        }
        return (int) (this.getIndex() - setDimension.getIndex());
    }
}
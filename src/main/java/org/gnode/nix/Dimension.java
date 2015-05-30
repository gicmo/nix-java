package org.gnode.nix;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.annotation.Namespace;
import org.bytedeco.javacpp.annotation.Platform;

@Platform(value = "linux",
        include = {"<nix/Dimensions.hpp>"},
        link = {"nix"})
@Namespace("nix")
public class Dimension extends Pointer {
    static {
        Loader.load();
    }

    //--------------------------------------------------
    // Constructors
    //--------------------------------------------------

    /**
     * Constructor that creates an uninitialized Dimension.
     * <p/>
     * Calling any method on an uninitialized dimension will throw a {@link java.lang.RuntimeException}.
     */
    public Dimension() {
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

}

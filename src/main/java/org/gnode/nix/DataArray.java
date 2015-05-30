package org.gnode.nix;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.annotation.Namespace;
import org.bytedeco.javacpp.annotation.Platform;

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


}

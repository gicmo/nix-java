package org.gnode.nix.internal;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.annotation.Name;
import org.bytedeco.javacpp.annotation.Namespace;
import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.Properties;

/**
 * <h1>None</h1>
 * Low level wrapper to NIX None.hpp .
 */

@Properties(value = {
        @Platform(include = {"<nix/None.hpp>"}),
        @Platform(value = "linux"),
        @Platform(value = "windows")})
@Namespace("nix")
@Name("none_t")
public class None extends Pointer {

    static {
        Loader.load();
    }

    /**
     * Constructor
     */
    public None() {
        allocate();
    }

    private native void allocate();
}

package org.gnode.nix.internal;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.annotation.Name;
import org.bytedeco.javacpp.annotation.Namespace;
import org.bytedeco.javacpp.annotation.Platform;

@Platform(value = "linux",
        include = {"<nix/None.hpp>"})
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

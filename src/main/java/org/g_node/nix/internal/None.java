package org.g_node.nix.internal;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.annotation.*;

/**
 * <h1>None</h1>
 * Low level wrapper to NIX None.hpp .
 */



@Platform(include = {"<nix/None.hpp>", "nonehelper.hpp"})
@Namespace("nix")
@Name("none_t")
public class None extends Pointer {

    static {
        Loader.load();
    }


    public None() {
        super(get_none_pointer());
    }

    @Namespace("::nix_java")
    private static native @Cast("void*") Pointer get_none_pointer();
}

package org.gnode.nix;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.annotation.Namespace;
import org.bytedeco.javacpp.annotation.Platform;

@Platform(value = "linux",
        include = {"<nix/Block.hpp>"},
        link = {"nix"})
@Namespace("nix")
public class Block extends Pointer {

    static {
        Loader.load();
    }

    /**
     * Constructor that creates an uninitialized Block.
     * <p/>
     * Calling any method on an uninitialized block will throw a {@link java.lang.RuntimeException}.
     */
    public Block() {
        allocate();
    }

    private native void allocate();

}
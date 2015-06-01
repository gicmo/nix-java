package org.gnode.nix.base;

import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.annotation.Platform;

@Platform(value = "linux")
public abstract class ImplContainer extends Pointer {

    /**
     * Check if entity is null
     *
     * @return true if initialized else false
     */
    abstract public boolean isNone();

    /**
     * Checks if entity is initialized
     *
     * @return true if initialized else false
     */
    abstract public boolean isInitialized();
}

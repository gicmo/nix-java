package org.g_node.nix.base;

import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.Properties;

/**
 * <h1>ImplContainer</h1>
 * An abstract class for all entities.
 */
@Properties(value = {
        @Platform(value = "linux"),
        @Platform(value = "windows")})
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
    public boolean isInitialized() {
        return !isNone();
    }
}

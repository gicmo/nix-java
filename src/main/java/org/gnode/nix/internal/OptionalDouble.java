package org.gnode.nix.internal;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.annotation.Name;
import org.bytedeco.javacpp.annotation.Namespace;
import org.bytedeco.javacpp.annotation.Platform;

@Platform(value = "linux",
        include = {"<boost/optional.hpp>"})
@Namespace("boost")
@Name("optional<double>")
public class OptionalDouble extends Pointer {

    static {
        Loader.load();
    }

    /**
     * Get double from the optional. Use this only after calling {@link OptionalDouble#isPresent()}.
     *
     * @return double data
     */
    public native
    @Name("get")
    double getDouble();

    /**
     * To check if the data is present.
     *
     * @return True if data is available, False otherwise.
     */
    public native
    @Name("is_initialized")
    @Cast("bool")
    boolean isPresent();

}



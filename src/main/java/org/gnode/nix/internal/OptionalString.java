package org.gnode.nix.internal;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.annotation.*;

@Platform(value = "linux",
        include = {"<boost/optional.hpp>"})
@Namespace("boost")
@Name("optional<std::string>")
public class OptionalString extends Pointer {

    static {
        Loader.load();
    }

    /**
     * Get string from the optional. Use this only after calling {@link OptionalString#isPresent()}.
     *
     * @return return string
     */
    public native
    @Name("get")
    @StdString
    String getString();

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



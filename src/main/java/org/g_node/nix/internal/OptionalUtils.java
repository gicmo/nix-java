package org.g_node.nix.internal;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.annotation.*;

/**
 * <h1>OptionalUtils</h1>
 * Low level wrapper to Boost optionals.
 */

@Properties(value = {
        @Platform(include = {"<boost/optional.hpp>"}),
        @Platform(value = "linux"),
        @Platform(value = "windows")})
public class OptionalUtils {
    static {
        Loader.load();
    }

    //--------------------------------------------------
    // Optional Double
    //--------------------------------------------------

    /**
     * <h1>OptionalDouble</h1>
     * Low level <tt>boost::optional&lt;double&gt;</tt> wrapper
     */
    @Name("boost::optional<double>")
    public static class OptionalDouble extends Pointer {

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

    //--------------------------------------------------
    // Optional String
    //--------------------------------------------------

    /**
     * <h1>OptionalString</h1>
     * Low level <tt>boost::optional&lt;string&gt;</tt> wrapper
     */
    @Name("boost::optional<std::string>")
    public static class OptionalString extends Pointer {

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
}

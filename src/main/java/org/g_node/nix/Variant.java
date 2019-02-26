package org.g_node.nix;

import org.bytedeco.javacpp.*;
import org.bytedeco.javacpp.annotation.*;
import org.g_node.nix.internal.BuildLibs;

/**
 * <h1>Variant</h1>
 * Class that is a sum type int, double, string.
 */

@Properties(value = {
        @Platform(include = {"<nix/Variant.hpp>"}),
        @Platform(value = "linux", link = BuildLibs.NIX_1, preload = BuildLibs.HDF5_7),
        @Platform(value = "macosx", link = BuildLibs.NIX, preload = BuildLibs.HDF5),
        @Platform(value = "windows",
                link = BuildLibs.NIX,
                preload = {BuildLibs.HDF5, BuildLibs.MSVCP120, BuildLibs.MSVCR120, BuildLibs.SZIP, BuildLibs.ZLIB})})
@Namespace("nix")
@NoOffset
public class Variant extends Pointer {

    static {
        Loader.load();
    }

    //--------------------------------------------------
    // Constructors
    //--------------------------------------------------

    /**
     * Constructor.
     */
    public Variant() {
        allocate();
    }

    private native void allocate();

    /**
     * Constructor. Initialize with boolean data.
     *
     * @param value boolean value to set.
     */
    public Variant(boolean value) {
        allocate();
        set(value);
    }

    /**
     * Constructor. Initialize with integer data.
     *
     * @param value integer value to set.
     */
    public Variant(int value) {
        allocate();
        set(value);
    }

    /**
     * Constructor. Initialize with long integer data.
     *
     * @param value long integer value to set.
     */
    public Variant(long value) {
        allocate();
        set(value);
    }

    /**
     * Constructor. Initialize with double data.
     *
     * @param value double value to set.
     */
    public Variant(double value) {
        allocate();
        set(value);
    }

    /**
     * Constructor. Initialize with string data.
     *
     * @param value string value to set.
     */
    public Variant(String value) {
        allocate();
        set(value);
    }

    //--------------------------------------------------
    // Setters
    //--------------------------------------------------

    private native void set(@Cast("bool") boolean value);

    /**
     * Set boolean data.
     *
     * @param value boolean value to set.
     */
    public void setBoolean(boolean value) {
        set(value);
    }

    private native void set(@Cast("int32_t") int value);

    /**
     * Set integer data.
     *
     * @param value integer value to set
     */
    public void setInt(int value) {
        set(value);
    }

    private native void set(@Cast("int64_t") long value);

    /**
     * Set long integer data.
     *
     * @param value long integer value to set
     */
    public void setLong(long value) {
        set(value);
    }

    private native void set(double value);

    /**
     * Set double data.
     *
     * @param value double value to set
     */
    public void setDouble(double value) {
        set(value);
    }

    private native void set(@StdString String value);

    /**
     * Set string data.
     *
     * @param value string value to set
     */
    public void setString(String value) {
        set(value);
    }

    //--------------------------------------------------
    // Getters
    //--------------------------------------------------

    /**
     * Getter for boolean value.
     *
     * @return boolean data.
     */
    public native @Cast("bool") @Name("get<bool>") boolean getBoolean();

    /**
     * Getter for 32 bit integer value.
     *
     * @return 32 bit integer data as int.
     */
    public native @Cast("int32_t") @Name("get<int32_t>") int getInt();

    /**
     * Getter for 64 bit integer value.
     *
     * @return 64 bit int in NIX as long.
     */
    public native @Cast("int64_t") @Name("get<int64_t>") long getLong();

    /**
     * Getter for double value.
     *
     * @return double data.
     */
    public native @Cast("double") @Name("get<double>") double getDouble();

    /**
     * Getter for string value.
     *
     * @return string data.
     */
    public native @StdString @Name("get<std::string>") String getString();

    //--------------------------------------------------
    // Other functions
    //--------------------------------------------------

    /**
     * Get type of data stored.
     *
     * @return data type constant.
     * @see DataType
     */
    public native
    @Name("type")
    @ByVal
    @Cast("nix::DataType")
    int getType();

    /**
     * Swap value with another value.
     *
     * @param other value object.
     */
    public native void swap(@ByRef Variant other);

    /**
     * Check if value supports a data type.
     *
     * @param dtype {@link DataType} constant.
     * @return True is supported, otherwise false.
     */
    public static native
    @Name("supports_type")
    @Cast("bool")
    boolean supportsType(@Cast("nix::DataType") int dtype);
}

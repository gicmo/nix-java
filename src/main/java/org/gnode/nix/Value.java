package org.gnode.nix;

import org.bytedeco.javacpp.BoolPointer;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.annotation.*;

/**
 * <h1>Value</h1>
 * Class that corresponds to the odML Value.
 */

@Properties(value = {
        @Platform(include = {"<nix/Value.hpp>"}, link = "nix"),
        @Platform(value = "linux"),
        @Platform(value = "windows")})
@Namespace("nix")
@NoOffset
public class Value extends Pointer {

    static {
        Loader.load();
    }

    //--------------------------------------------------
    // Constructors
    //--------------------------------------------------

    /**
     * Constructor.
     */
    public Value() {
        allocate();
    }

    private native void allocate();

    /**
     * Constructor. Initialize with boolean data.
     *
     * @param value boolean value to set.
     */
    public Value(boolean value) {
        allocate();
        set(value);
    }

    /**
     * Constructor. Initialize with integer data.
     *
     * @param value integer value to set.
     */
    public Value(int value) {
        allocate();
        set(value);
    }

    /**
     * Constructor. Initialize with long integer data.
     *
     * @param value long integer value to set.
     */
    public Value(long value) {
        allocate();
        set(value);
    }

    /**
     * Constructor. Initialize with double data.
     *
     * @param value double value to set.
     */
    public Value(double value) {
        allocate();
        set(value);
    }

    /**
     * Constructor. Initialize with string data.
     *
     * @param value string value to set.
     */
    public Value(String value) {
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

    private native void get(@Cast("bool*") @ByRef BoolPointer out);

    /**
     * Getter for boolean value.
     *
     * @return boolean data.
     */
    public boolean getBoolean() {
        BoolPointer bp = new BoolPointer(1);
        get(bp);
        return bp.get();
    }

    private native void get(@ByRef int[] value);

    /**
     * Getter for integer value.
     *
     * @return integer data.
     */
    public int getInt() {
        int[] val = new int[1];
        get(val);
        return val[0];
    }

    private native void get(@ByRef long[] value);

    /**
     * Getter for long integer value.
     *
     * @return long integer data.
     */
    public long getLong() {
        long[] val = new long[1];
        get(val);
        return val[0];
    }

    private native void get(@ByRef double[] value);

    /**
     * Getter for double value.
     *
     * @return double data.
     */
    public double getDouble() {
        double[] val = new double[1];
        get(val);
        return val[0];
    }

    private native void get(@StdString BytePointer value);

    /**
     * Getter for string value.
     *
     * @return string data.
     */
    public String getString() {
        BytePointer bp = new BytePointer(1);
        get(bp);
        return bp.getString();
    }

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
    public native void swap(@ByRef Value other);

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

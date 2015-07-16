package org.gnode.nix;

/**
 * <h1>DataType</h1>
 * Constants for all valid data types.
 * <p>
 * Those data types are used by {@link DataArray} and {@link Property}
 * in order to indicate of what type the stored data of value is.
 *
 * @see DataArray
 * @see Property
 */
public class DataType {

    public static final int Bool = 0;
    public static final int Char = 1;
    public static final int Float = 2;
    public static final int Double = 3;
    public static final int Int8 = 4;
    public static final int Int16 = 5;
    public static final int Int32 = 6;
    public static final int Int64 = 7;
    public static final int UInt8 = 8;
    public static final int UInt16 = 9;
    public static final int UInt32 = 10;
    public static final int UInt64 = 11;
    public static final int String = 12;
    public static final int Date = 13;
    public static final int DateTime = 14;
    public static final int Opaque = 15;

    public static final int Nothing = -1;
}

package org.gnode.nix;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.annotation.*;

/**
 * <h1>DataView</h1>
 */

@Properties(value = {
        @Platform(include = {"<nix/DataView.hpp>"}, link = "nix"),
        @Platform(value = "linux"),
        @Platform(value = "windows")})
@Namespace("nix")
public class DataView extends Pointer {

    static {
        Loader.load();
    }

    /**
     * Constructor.
     *
     * @param da     DataArray
     * @param count  count
     * @param offset offset
     */
    public DataView(@ByVal DataArray da, @ByVal NDSize count, @ByVal NDSize offset) {
        allocate(da, count, offset);
    }

    private native void allocate(@ByVal DataArray da, @ByVal NDSize count, @ByVal NDSize offset);

    // the DataIO interface implementation

    /**
     * Set data extent.
     *
     * @param extent Data extent.
     * @see NDSize
     */
    public native
    @Name("dataExtent")
    void setDataExtent(@Const @ByRef NDSize extent);

    /**
     * Data extent.
     *
     * @return Data extent.
     * @see NDSize
     */
    public native
    @Name("dataExtent")
    @ByVal
    NDSize getDataExtent();

    /**
     * Get type of data.
     *
     * @return The type of data. Constant specified in {@link DataType}.
     * @see DataType
     */
    public native
    @Name("dataType")
    @ByVal
    @Cast("nix::DataType")
    int getDataType();
}

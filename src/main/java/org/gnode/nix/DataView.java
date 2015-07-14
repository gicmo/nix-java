package org.gnode.nix;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.annotation.*;

@Properties(value = {
        @Platform(include = {"<nix/DataView.hpp>"}, link = "nix"),
        @Platform(value = "linux"),
        @Platform(value = "windows")})
@Namespace("nix")
public class DataView extends Pointer {

    static {
        Loader.load();
    }

    public DataView(@ByVal DataArray da, @ByVal NDSize count, @ByVal NDSize offset) {
        allocate(da, count, offset);
    }

    private native void allocate(@ByVal DataArray da, @ByVal NDSize count, @ByVal NDSize offset);

    // the DataIO interface implementation

    /**
     * Set data extent.
     *
     * @param extent extent
     */
    public native
    @Name("dataExtent")
    void setDataExtent(@Const @ByRef NDSize extent);

    /**
     * Data extent.
     *
     * @return extent
     */
    public native
    @Name("dataExtent")
    @ByVal
    NDSize getDataExtent();

    /**
     * Get type of data.
     *
     * @return type
     */
    public native
    @Name("dataType")
    @ByVal
    @Cast("nix::DataType")
    int getDataType();
}

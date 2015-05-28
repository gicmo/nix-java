package org.gnode.nix;

import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.annotation.*;

import java.util.ArrayList;

@Platform(value = "linux",
        include = {"<nix/File.hpp>"},
        link = {"nix"},
        linkpath = {"/usr/local/lib/"},
        includepath = {"/usr/local/include/"})
@Namespace("nix")
public class File extends Pointer {

    static {
        Loader.load();
    }

    //--------------------------------------------------
    // Constructors
    //--------------------------------------------------

    /**
     * Constructor that creates an uninitialized File.
     * <p/>
     * Calling any method on an uninitialized file will throw a {@link java.lang.RuntimeException}.
     */
    public File() {
        allocate();
    }

    private native void allocate();

    //--------------------------------------------------
    // Base class methods
    //--------------------------------------------------

    private native
    @Cast("bool")
    boolean isNone();

    /**
     * Checks if file is initialized
     *
     * @return true if initialized else false
     */
    public boolean isInitialized() {
        return !isNone();
    }


    /**
     * Opens a file.
     *
     * @param name The name/path of the file.
     * @param mode The open mode. Check {@link FileMode} for options.
     * @param impl The back-end implementation the should be used to open the file. Check {@link Implementation} for options.
     * @return The opened file.
     * @see FileMode
     * @see Implementation
     */
    public static native
    @ByVal
    File open(@StdString String name, @Cast("nix::FileMode") int mode,
              @Cast("nix::Implementation") int impl);

    /**
     * Opens a file with back-end implementation in hdf5.
     *
     * @param name The name/path of the file.
     * @param mode The open mode. Check {@link FileMode} for options.
     * @return The opened file.
     * @see FileMode
     */
    public static File open(@StdString String name, int mode) {
        return open(name, mode, Implementation.Hdf5);
    }

    /**
     * Opens a file in ReadWrite mode and with back-end implementation in hdf5.
     *
     * @param name The name/path of the file.
     * @return The opened file.
     */
    public static native
    @ByVal
    File open(@StdString String name);

    //------------------------------------------------------
    // Methods for file attribute access.
    //------------------------------------------------------

    private native
    @StdVector
    IntPointer version();

    /**
     * Read the NIX format version from the file.
     * <p/>
     * The version consist of three integers standing for the major, minor and patch version of the nix format.
     *
     * @return The format version of the NIX file.
     */
    public ArrayList<Integer> getVersion() {
        return Utils.convertPointerToList(version());
    }

    /**
     * Read the format hint from the file.
     *
     * @return
     */
    public native
    @Name("format")
    @StdString
    String getFormat();

    /**
     * Return the location / uri.
     *
     * @return The uri string.
     */
    public native
    @Name("location")
    @StdString
    String getLocation();

    /**
     * Get the creation date of the file.
     *
     * @return The creation date of the file.
     */
    public native
    @Cast("time_t")
    long createdAt();

    /**
     * Get the date of the last update.
     *
     * @return The date of the last update.
     */
    public native
    @Cast("time_t")
    long updatedAt();

    /**
     * Sets the time of the last update to the current time if the field is not set.
     */
    public native void setUpdatedAt();

    /**
     * Sets the time of the last update to the current time.
     */
    public native void forceUpdatedAt();

    /**
     * Sets the creation time to the current time if the field is not set.
     */
    public native void setCreatedAt();

    /**
     * Sets the creation time to the provided value even if the attribute is set.
     *
     * @param time The creation time to set.
     */
    public native void forceCreatedAt(@Cast("time_t") long time);


    //------------------------------------------------------
    // Other functions
    //------------------------------------------------------

    /**
     * Close the file.
     */
    public native void close();

    /**
     * Check if the file is currently open.
     *
     * @return True if the file is open, false otherwise.
     */
    public native
    @Cast("bool")
    boolean isOpen();

}

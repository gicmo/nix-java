package org.g_node.nix.valid;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.annotation.*;
import org.g_node.nix.internal.BuildLibs;
import org.g_node.nix.internal.None;
import org.g_node.nix.internal.VectorUtils;

import java.util.List;

/**
 * <h1>Result</h1>
 * Class used to store error and warning messages.
 */

@Properties(value = {
        @Platform(include = {"<nix/valid/result.hpp>"}),
        @Platform(value = "linux", link = BuildLibs.NIX_1, preload = BuildLibs.HDF5_7),
        @Platform(value = "windows",
                link = BuildLibs.NIX,
                preload = {BuildLibs.HDF5, BuildLibs.MSVCP120, BuildLibs.MSVCR120, BuildLibs.SZIP, BuildLibs.ZLIB})})
@Namespace("nix::valid")
@NoOffset
public class Result extends Pointer {
    static {
        Loader.load();
    }

    //--------------------------------------------------
    // Constructors
    //--------------------------------------------------

    private native void allocate();

    private native void allocate(@Const @ByRef VectorUtils.MessageVector errs,
                                 @Const @ByRef VectorUtils.MessageVector warns);

    private native void allocate(@Const @ByVal None none,
                                 @Const @ByRef VectorUtils.MessageVector warns);

    private native void allocate(@Const @ByRef VectorUtils.MessageVector errs,
                                 @Const @ByVal None none);

    private native void allocate(@Const @ByVal None none,
                                 @Const @ByRef Message warn);

    private native void allocate(@Const @ByRef Message err,
                                 @Const @ByVal None none);

    /**
     * Constructor
     */
    public Result() {
        allocate();
    }

    /**
     * Standard constructor.
     * <p>
     * Standard constructor that expects a list of error msgs and a list
     * of warning msgs. Pass null if not present.
     *
     * @param errs  List of error messages
     * @param warns List of warning messages
     * @see Message
     */
    public Result(List<Message> errs, List<Message> warns) {
        if (errs == null && warns == null) {
            allocate();
        } else if (errs == null) {
            allocate(new None(), new VectorUtils.MessageVector(warns));
        } else if (warns == null) {
            allocate(new VectorUtils.MessageVector(errs), new None());
        } else {
            allocate(new VectorUtils.MessageVector(errs), new VectorUtils.MessageVector(warns));
        }
    }


    //--------------------------------------------------
    // Error and warning functions
    //--------------------------------------------------

    private native
    @Name("getWarnings")
    @ByVal
    VectorUtils.MessageVector fetchWarnings();

    /**
     * Returns the warnings list.
     *
     * @return list of warning msgs
     * @see Message
     */
    public List<Message> getWarnings() {
        return fetchWarnings().getMessages();
    }

    private native
    @Name("getErrors")
    @ByVal
    VectorUtils.MessageVector fetchErrors();

    /**
     * Returns the errors list.
     *
     * @return list of error msgs
     * @see Message
     */
    public List<Message> getErrors() {
        return fetchErrors().getMessages();
    }

    /**
     * Appends the warnings & errors of given Result to this one
     * <p>
     * Concatenates the errors and warnings lists
     * of the given {@link Result} object to those if this {@link Result}
     * object and returns a reference to this object.
     *
     * @return reference to this Result
     */
    public native
    @ByVal
    Result concat(@Const @ByRef Result result);

    /**
     * Adds an error message
     * <p>
     * Adds an error message to this {@link Result}
     * object and returns a reference to this object.
     *
     * @return reference to this Result
     * @see Message
     */
    public native
    @ByVal
    Result addError(@Const @ByRef Message error);

    /**
     * Adds a warning message
     * <p>
     * Adds a warning message to this {@link Result}
     * object and returns a reference to this object.
     *
     * @return reference to this Result
     * @see Message
     */
    public native
    @ByVal
    Result addWarning(@Const @ByRef Message warning);

    /**
     * Returns true if no msgs added at all
     * <p>
     * Returns true if neither errors nor warnings have been added,
     * thus both lists are empty. Returns false otherwise.
     *
     * @return bool indicating whether no msgs added at all
     */
    public native
    @Name("ok")
    @Cast("bool")
    boolean isOK();

    /**
     * Returns true if no error msgs added at all
     * <p>
     * Returns false if no errors have been added, thus list is empty.
     * Returns true otherwise.
     *
     * @return bool indicating whether error msgs added
     */
    public native
    @Cast("bool")
    boolean hasErrors();

    /**
     * Returns true if no warning msgs added at all
     * <p>
     * Returns false if no warnings have been added, thus list is empty.
     * Returns true otherwise.
     *
     * @return bool indicating whether warning msgs added
     */
    public native
    @Cast("bool")
    boolean hasWarnings();
}
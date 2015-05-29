package org.gnode.nix;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.annotation.*;
import org.gnode.nix.internal.None;
import org.gnode.nix.internal.OptionalString;
import org.gnode.nix.internal.Utils;

import java.util.Date;

@Platform(value = "linux",
        include = {"<nix/Block.hpp>"},
        link = {"nix"})
@Namespace("nix")
public class Block extends Pointer {

    static {
        Loader.load();
    }

    /**
     * Constructor that creates an uninitialized Block.
     * <p/>
     * Calling any method on an uninitialized block will throw a {@link java.lang.RuntimeException}.
     */
    public Block() {
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
     * Checks if block is initialized
     *
     * @return true if initialized, False otherwise
     */
    public boolean isInitialized() {
        return !isNone();
    }

    /**
     * Get id of the block
     *
     * @return id string
     */
    public native
    @Name("id")
    @StdString
    String getId();

    private native
    @Cast("time_t")
    long createdAt();

    /**
     * Get the creation date of the block.
     *
     * @return The creation date of the block.
     */
    public Date getCreatedAt() {
        return Utils.convertSecondsToDate(createdAt());
    }

    private native
    @Cast("time_t")
    long updatedAt();

    /**
     * Get the date of the last update.
     *
     * @return The date of the last update.
     */
    public Date getUpdatedAt() {
        return Utils.convertSecondsToDate(updatedAt());
    }

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

    private native void forceCreatedAt(@Cast("time_t") long time);

    /**
     * Sets the creation date to the provided value even if the attribute is set.
     *
     * @param date The creation date to set.
     */
    public void forceCreatedAt(Date date) {
        forceCreatedAt(Utils.convertDateToSeconds(date));
    }

    /**
     * Setter for the type of the block
     *
     * @param type The type of the block
     */
    public native
    @Name("type")
    void setType(@StdString String type);

    /**
     * Getter for the type of the block
     *
     * @return The type of the block
     */
    public native
    @Name("type")
    @StdString
    String getType();

    /**
     * Getter for the name of the block.
     *
     * @return The name of the block.
     */
    public native
    @Name("name")
    @StdString
    String getName();

    /**
     * Setter for the definition of the block.
     *
     * @param definition The definition of the block.
     */
    public native
    @Name("definition")
    void setDefinition(@StdString String definition);

    private native
    @ByVal
    OptionalString definition();

    /**
     * Getter for the definition of the block.
     *
     * @return The definition of the block.
     */
    public String getDefinition() {
        OptionalString defintion = definition();
        if (defintion.isPresent()) {
            return defintion.getString();
        }
        return null;
    }

    private native void definition(@Const @ByVal None t);

    /**
     * Deletes for the definition of the block.
     */
    public void removeDefinition() {
        definition(new None());
    }

}
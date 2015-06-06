package org.gnode.nix;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.annotation.*;
import org.gnode.nix.base.NamedEntity;
import org.gnode.nix.internal.None;
import org.gnode.nix.internal.OptionalString;
import org.gnode.nix.internal.Utils;

import java.util.Date;
import java.util.List;

@Platform(value = "linux",
        include = {"<nix/Block.hpp>"},
        link = {"nix"})
@Namespace("nix")
public class Block extends NamedEntity {

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

    public native
    @Cast("bool")
    boolean isNone();

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

    private native void definition(@Const @ByVal None t);

    private native void definition(@StdString String definition);

    /**
     * Setter for the definition of the block. If null is passed definition is removed.
     *
     * @param definition definition of block
     */
    public void setDefinition(String definition) {
        if (definition != null) {
            definition(definition);
        } else {
            definition(new None());
        }
    }

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

    //--------------------------------------------------
    // Methods concerning data arrays
    //--------------------------------------------------

    /**
     * Checks if a specific data array exists in this block.
     *
     * @param nameOrId Name or id of a data array.
     * @return True if the data array exists, false otherwise.
     */
    public native
    @Cast("bool")
    boolean hasDataArray(@StdString String nameOrId);

    /**
     * Checks if a specific data array exists in this block.
     *
     * @param dataArray The data array to check.
     * @return True if the data array exists, false otherwise.
     */
    public native
    @Cast("bool")
    boolean hasDataArray(@Const @ByRef DataArray dataArray);

    private native
    @Name("getDataArray")
    @ByVal
    DataArray fetchDataArray(@StdString String nameOrId);

    /**
     * Retrieves a specific data array from the block by name or id.
     *
     * @param nameOrId Name or id of an existing data array.
     * @return The data array with the specified id. If this
     * doesn't exist, an exception will be thrown.
     */
    public DataArray getDataArray(String nameOrId) {
        DataArray da = fetchDataArray(nameOrId);
        if (da.isInitialized()) {
            return da;
        }
        return null;
    }

    private native
    @Name("getDataArray")
    @ByVal
    DataArray fetchDataArray(@Cast("size_t") long index);

    /**
     * Retrieves a data array by index.
     *
     * @param index The index of the data array.
     * @return The data array at the specified index.
     */
    public DataArray getDataArray(long index) {
        DataArray da = fetchDataArray(index);
        if (da.isInitialized()) {
            return da;
        }
        return null;
    }

    private native
    @Name("dataArrays")
    @StdVector
    DataArray getDataArrays();

    /**
     * Get data arrays within this block.
     *
     * @return list of data arrays
     */
    public List<DataArray> dataArrays() {
        return Utils.convertPointerToList(getDataArrays(), DataArray.class);
    }

    /**
     * Returns the number of all data arrays of the block.
     *
     * @return The number of data arrays of the block.
     */
    public native
    @Name("dataArrayCount")
    long getDataArrayCount();


    private native
    @Name("createDataArray")
    @ByVal
    DataArray makeDataArray(@StdString String name,
                            @StdString String type, @Cast("nix::DataType")
                            int dataType,
                            @Const @ByRef NDSize shape);

    /**
     * Create a new data array associated with this block.
     *
     * @param name     The name of the data array to create.
     * @param type     The type of the data array.
     * @param dataType A {@link DataType} indicating the format to store values.
     * @param shape    A NDSize holding the extent of the array to create.
     * @return The newly created data array.
     */
    public DataArray createDataArray(String name, String type, int dataType, NDSize shape) {
        DataArray da = makeDataArray(name, type, dataType, shape);
        if (da.isInitialized()) {
            return da;
        }
        return null;
    }

    /**
     * Deletes a data array from this block.
     * <p/>
     * This deletes a data array and all its dimensions from the block and the file.
     * The deletion can't be undone.
     *
     * @param nameOrId Name or id of the data array to delete.
     * @return True if the data array was deleted, false otherwise.
     */
    public native
    @Cast("bool")
    boolean deleteDataArray(@StdString String nameOrId);

    /**
     * Deletes a data array from this block.
     * <p/>
     * This deletes a data array and all its dimensions from the block and the file.
     * The deletion can't be undone.
     *
     * @param dataArray The data array to delete.
     * @return True if the data array was deleted, false otherwise.
     */
    public native
    @Cast("bool")
    boolean deleteDataArray(@Const @ByRef DataArray dataArray);

    //--------------------------------------------------
    // Overrides
    //--------------------------------------------------

    @Override
    public String toString() {
        return "Block: {name = " + this.getName()
                + ", type = " + this.getType()
                + ", id = " + this.getId() + "}";
    }
}
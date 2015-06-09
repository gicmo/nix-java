package org.gnode.nix;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.annotation.*;
import org.gnode.nix.base.EntityWithMetadata;
import org.gnode.nix.internal.None;
import org.gnode.nix.internal.OptionalString;
import org.gnode.nix.internal.SourceVector;
import org.gnode.nix.internal.Utils;

import java.util.Date;
import java.util.List;

@Platform(value = "linux",
        include = {"<nix/Block.hpp>"},
        link = {"nix"})
@Namespace("nix")
public class Block extends EntityWithMetadata {

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

    private native
    @ByVal
    Section metadata();

    /**
     * Get metadata associated with this entity.
     *
     * @return The associated section, if no such section exists {#link null} is returned.
     */
    public
    @Name("metadata")
    Section getMetadata() {
        Section section = metadata();
        if (section.isInitialized()) {
            return section;
        } else {
            return null;
        }
    }

    /**
     * Associate the entity with some metadata.
     * <p/>
     * Calling this method will replace previously stored information.
     *
     * @param metadata The {@link Section} that should be associated
     *                 with this entity.
     */
    public native
    @Name("metadata")
    void setMetadata(@Const @ByRef Section metadata);

    /**
     * Associate the entity with some metadata.
     * <p/>
     * Calling this method will replace previously stored information.
     *
     * @param id The id of the {@link Section} that should be associated
     *           with this entity.
     */
    public native
    @Name("metadata")
    void setMetadata(@StdString String id);

    private native void metadata(@Const @ByVal None t);

    /**
     * Removes metadata associated with the entity.
     */
    public void removeMetadata() {
        metadata(new None());
    }


    //--------------------------------------------------
    // Methods concerning sources
    //--------------------------------------------------

    /**
     * Checks if this block has a specific root source.
     *
     * @param nameOrId Name or id of the source.
     * @return True if a source with the given id exists at the root, false
     * otherwise.
     */
    public native
    @Cast("bool")
    boolean hasSource(@StdString String nameOrId);

    /**
     * Checks if this block has a specific root source.
     *
     * @param source The source to check.
     * @return True if the source exists at the root, false
     * otherwise.
     */
    public native
    @Cast("bool")
    boolean hasSource(@Const @ByRef Source source);

    private native
    @Name("getSource")
    @ByVal
    Source fetchSource(@StdString String nameOrId);

    /**
     * Retrieves a specific root source by its id.
     *
     * @param nameOrId Name or id of the source.
     * @return The source with the given id. If it doesn't exist an exception
     * will be thrown.
     */
    public Source getSource(String nameOrId) {
        Source source = fetchSource(nameOrId);
        if (source.isInitialized()) {
            return source;
        }
        return null;
    }

    private native
    @Name("getSource")
    @ByVal
    Source fetchSource(@Cast("size_t") long index);

    /**
     * Retrieves a specific root source by index.
     *
     * @param index The index of the source.
     * @return The source at the specified index.
     */
    public Source getSource(long index) {
        Source source = fetchSource(index);
        if (source.isInitialized()) {
            return source;
        }
        return null;
    }

    /**
     * Returns the number of root sources in this block.
     *
     * @return The number of root sources.
     */
    public native
    @Name("sourceCount")
    long getSourceCount();

    private native
    @ByVal
    SourceVector sources();

    /**
     * Get all root sources associated with this block.
     *
     * @return list of source.
     */
    public List<Source> getSources() {
        return sources().getSources();
    }

    private native
    @Name("createSource")
    @ByVal
    Source makeSource(@StdString String name, @StdString String type);

    /**
     * Create a new root source.
     *
     * @param name The name of the source to create.
     * @param type The type of the source.
     * @return The created source object.
     */
    public Source createSource(String name, String type) {
        Source source = makeSource(name, type);
        if (source.isInitialized()) {
            return source;
        }
        return null;
    }

    /**
     * Deletes a root source.
     * <p/>
     * This will also delete all child sources of this root source from the file.
     * The deletion of a source can't be undone.
     *
     * @param nameOrId Name or id of the source to delete.
     * @return True if the source was deleted, false otherwise.
     */
    public native
    @Cast("bool")
    boolean deleteSource(@StdString String nameOrId);

    /**
     * Deletes a root source.
     * <p/>
     * This will also delete all child sources of this root source from the file.
     * The deletion of a source can't be undone.
     *
     * @param source The source to delete.
     * @return True if the source was deleted, false otherwise.
     */
    public native
    @Cast("bool")
    boolean deleteSource(@Const @ByRef Source source);

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
    // Methods concerning tags.
    //--------------------------------------------------

    /**
     * Checks if a specific tag exists in the block.
     *
     * @param nameOrId Name or id of a tag.
     * @return True if the tag exists, false otherwise.
     */
    public native
    @Cast("bool")
    boolean hasTag(@StdString String nameOrId);

    /**
     * Checks if a specific tag exists in the block.
     *
     * @param tag The tag to check.
     * @return True if the tag exists, false otherwise.
     */
    public native
    @Cast("bool")
    boolean hasTag(@Const @ByRef Tag tag);

    private native
    @Name("getTag")
    @ByVal
    Tag fetchTag(@StdString String nameOrId);

    /**
     * Retrieves a specific tag from the block by its id.
     *
     * @param nameOrId Name or id of the tag.
     * @return The tag with the specified id. If this tag doesn't exist
     * an exception will be thrown.
     */
    public Tag getTag(String nameOrId) {
        Tag tag = fetchTag(nameOrId);
        if (tag.isInitialized()) {
            return tag;
        }
        return null;
    }

    private native
    @Name("getTag")
    @ByVal
    Tag fetchTag(@Cast("size_t") long index);

    /**
     * Retrieves a specific tag by index.
     *
     * @param index The index of the tag.
     * @return The tag at the specified index.
     */
    public Tag getTag(long index) {
        Tag tag = fetchTag(index);
        if (tag.isInitialized()) {
            return tag;
        }
        return null;
    }

    private native
    @StdVector
    Tag tags();

    /**
     * Get tags within this block.
     *
     * @return list of all tags.
     */
    public List<Tag> getTags() {
        return Utils.convertPointerToList(tags(), Tag.class);
    }

    /**
     * Returns the number of tags within this block.
     *
     * @return The number of tags.
     */
    public native
    @Name("tagCount")
    long getTagCount();

    private native
    @Name("createTag")
    @ByVal
    Tag makeTag(@StdString String name, @StdString String type,
                @StdVector double[] position);

    /**
     * Create a new tag associated with this block.
     *
     * @param name     The name of the tag to create.
     * @param type     The type of the tag.
     * @param position The position of the tag.
     * @return The newly created tag.
     */
    public Tag createTag(String name, String type, double[] position) {
        Tag tag = makeTag(name, type, position);
        if (tag.isInitialized()) {
            return tag;
        }
        return null;
    }

    /**
     * Deletes a tag from the block.
     * <p/>
     * Deletes a tag with all its features from the block and the file.
     * The deletion can't be undone.
     *
     * @param nameOrId Name or id of the tag to remove.
     * @return True if the tag was removed, false otherwise.
     */
    public native
    @Cast("bool")
    boolean deleteTag(@StdString String nameOrId);

    /**
     * Deletes a tag from the block.
     * <p/>
     * Deletes a tag with all its features from the block and the file.
     * The deletion can't be undone.
     *
     * @param tag The tag to remove.
     * @return True if the tag was removed, false otherwise.
     */
    public native
    @Cast("bool")
    boolean deleteTag(@Const @ByRef Tag tag);


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
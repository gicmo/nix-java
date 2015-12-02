package org.g_node.nix;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.annotation.*;
import org.bytedeco.javacpp.annotation.Properties;
import org.g_node.nix.internal.*;
import org.g_node.nix.base.EntityWithMetadata;

import java.util.*;
import java.util.function.Predicate;

/**
 * <h1>Block</h1>
 * Class for grouping further data entities.
 * <p>
 * The Block entity is a top-level, summarizing element that allows to
 * group the other data entities belonging for example to the same recording session.
 * All data entities such as {@link Source}, {@link DataArray}, {@link Tag} and
 * {@link MultiTag} have to be associated with one Block.
 * <p>
 * <h2>Create a new Block</h2>
 * A block can only be created on an existing file object. Do not use the blocks constructors for this
 * purpose.
 * <pre><code>
 *      File f = ...;
 *      Block b = f.createBlock("session one", "recording_session");
 * </code></pre>
 * <p>
 * <h2>Working with blocks</h2>
 * After a block was created it can be used to create further entities. See the documentation of
 * {@link Source}, {@link DataArray}, {@link Tag} and {@link MultiTag}
 * for more information.
 * The next example shows how some properties of a block can be accessed.
 * <pre> <code>
 *      File f = ...;
 *      Block b = f.createBlock("session one", "recording_session");<p>
 *      // add metadata to a block
 *      Section s = f.getSection(sec_id);
 *      b.setMetadata(s); <p>
 *      // get associated metadata from a block
 *      Section s1 = b.getMetadata();<p>
 *      // remove associated metadata from a block
 *      b.removeMetadata();
 * </code></pre>
 * <p>
 * <h2>Deleting a block</h2>
 * When a block is deleted from a file all contained data e.g. sources, data arrays and tags will
 * be removed too.
 * <pre><code>
 *      File f = ...;
 *      boolean deleted = f.deleteBlock(some_id);
 *      System.out.println("The block was " + (deleted ? "" : "not ") + "deleted.");
 * </code></pre>
 *
 * @see Source
 * @see DataArray
 * @see Tag
 * @see MultiTag
 */

@Properties(value = {
        @Platform(include = {"<nix/Block.hpp>"}),
        @Platform(value = "linux", link = BuildLibs.NIX_1, preload = BuildLibs.HDF5_7),
        @Platform(value = "windows",
                link = BuildLibs.NIX,
                preload = {BuildLibs.HDF5, BuildLibs.MSVCP120, BuildLibs.MSVCR120, BuildLibs.SZIP, BuildLibs.ZLIB})})
@Namespace("nix")
public class Block extends EntityWithMetadata {

    static {
        Loader.load();
    }

    /**
     * Constructor that creates an uninitialized Block.
     * <p>
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
     * @return ID string.
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
        return DateUtils.convertSecondsToDate(createdAt());
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
        return DateUtils.convertSecondsToDate(updatedAt());
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
        forceCreatedAt(DateUtils.convertDateToSeconds(date));
    }

    /**
     * Setter for the type of the block.
     *
     * @param type The type of the block.
     */
    public native
    @Name("type")
    void setType(@StdString String type);

    /**
     * Getter for the type of the block.
     *
     * @return The type of the block.
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
     * Setter for the definition of the block. If <tt>null</tt> is passed definition is removed.
     *
     * @param definition The definition of block.
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
    OptionalUtils.OptionalString definition();

    /**
     * Getter for the definition of the block.
     *
     * @return The definition of the block.
     */
    public String getDefinition() {
        OptionalUtils.OptionalString defintion = definition();
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
     * @return The associated section, if no such section exists <tt>null</tt>  is returned.
     * @see Section
     */
    public
    @Name("metadata")
    Section getMetadata() {
        Section section = metadata();
        if (section.isNone()) {
            section = null;
        }
        return section;
    }

    /**
     * Associate the entity with some metadata.
     * <p>
     * Calling this method will replace previously stored information.
     *
     * @param metadata The {@link Section} that should be associated
     *                 with this entity.
     * @see Section
     */
    public native
    @Name("metadata")
    void setMetadata(@Const @ByRef Section metadata);

    /**
     * Associate the entity with some metadata.
     * <p>
     * Calling this method will replace previously stored information.
     *
     * @param id The id of the {@link Section} that should be associated
     *           with this entity.
     * @see Section
     */
    public native
    @Name("metadata")
    void setMetadata(@StdString String id);

    private native void metadata(@Const @ByVal None t);

    /**
     * Removes metadata associated with the entity.
     *
     * @see Section
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
     * @see Source
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
     * @see Source
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
     * @see Source
     */
    public Source getSource(String nameOrId) {
        Source source = fetchSource(nameOrId);
        if (source.isNone()) {
            source = null;
        }
        return source;
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
     * @see Source
     */
    public Source getSource(long index) {
        Source source = fetchSource(index);
        if (source.isNone()) {
            source = null;
        }
        return source;
    }

    /**
     * Returns the number of root sources in this block.
     *
     * @return The number of root sources.
     * @see Source
     */
    public native
    @Name("sourceCount")
    long getSourceCount();

    private native
    @ByVal
    VectorUtils.SourceVector sources();

    /**
     * Get all root sources associated with this block.
     *
     * @return list of source.
     * @see Source
     */
    public List<Source> getSources() {
        return sources().getSources();
    }

    /**
     * Get all root sources associated with this block.
     * <p>
     * The parameter filter can be used to filter sources by various
     * criteria.
     *
     * @param filter A filter function.
     * @return A list containing the matching root sources.
     * @see Source
     */
    public List<Source> getSources(Predicate<Source> filter) {
        List<Source> result = new ArrayList<>();
        for (Source source : getSources()) {
            if (filter.test(source)) {
                result.add(source);
            }
        }
        return result;
    }

    /**
     * Get all sources in this block recursively.
     * <p>
     * This method traverses the tree of all sources in the block. The traversal
     * is accomplished via breadth first and can be limited in depth. On each node or
     * source a filter is applied. If the filter returns true the respective source
     * will be added to the result list.
     * By default a filter is used that accepts all sources.
     *
     * @param filter   A filter function.
     * @param maxDepth The maximum depth of traversal.
     * @return A list containing the matching sources.
     * @see Source
     */
    public List<Source> findSources(Predicate<Source> filter, int maxDepth) {
        List<Source> result = new ArrayList<>();
        for (Source source : getSources()) {
            result.addAll(source.findSources(filter, maxDepth));
        }
        return result;
    }

    /**
     * Get all sources in this block recursively.
     * <p>
     * This method traverses the tree of all sources in the block. The traversal
     * is accomplished via breadth first and can be limited in depth. On each node or
     * source a filter is applied. If the filter returns true the respective source
     * will be added to the result list.
     * By default a sources at all depths are considered.
     *
     * @param filter A filter function.
     * @return A list containing the matching sources.
     * @see Source
     */
    public List<Source> findSources(Predicate<Source> filter) {
        return findSources(filter, Integer.MAX_VALUE);
    }

    /**
     * Get all sources in this block recursively.
     * <p>
     * This method traverses the tree of all sources in the block. The traversal
     * is accomplished via breadth first and can be limited in depth. On each node or
     * source a filter is applied. If the filter returns true the respective source
     * will be added to the result list.
     * By default a filter is used that accepts all sources.
     *
     * @param maxDepth The maximum depth of traversal.
     * @return A list containing the matching sources.
     * @see Source
     */
    public List<Source> findSources(int maxDepth) {
        return findSources((Source s) -> true, maxDepth);
    }

    /**
     * Get all sources in this block recursively.
     * <p>
     * This method traverses the tree of all sources in the block. The traversal
     * is accomplished via breadth first and can be limited in depth. On each node or
     * source a filter is applied. If the filter returns true the respective source
     * will be added to the result list.
     * By default a filter is used that accepts all sources at all depths.
     *
     * @return A list containing the matching sources.
     * @see Source
     */
    public List<Source> findSources() {
        return findSources((Source s) -> true, Integer.MAX_VALUE);
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
     * @see Source
     */
    public Source createSource(String name, String type) {
        Source source = makeSource(name, type);
        if (source.isNone()) {
            source = null;
        }
        return source;
    }

    /**
     * Deletes a root source.
     * <p>
     * This will also delete all child sources of this root source from the file.
     * The deletion of a source can't be undone.
     *
     * @param nameOrId Name or id of the source to delete.
     * @return True if the source was deleted, false otherwise.
     * @see Source
     */
    public native
    @Cast("bool")
    boolean deleteSource(@StdString String nameOrId);

    /**
     * Deletes a root source.
     * <p>
     * This will also delete all child sources of this root source from the file.
     * The deletion of a source can't be undone.
     *
     * @param source The source to delete.
     * @return True if the source was deleted, false otherwise.
     * @see Source
     */
    public native
    @Cast("bool")
    boolean deleteSource(@Const @ByRef Source source);

    //--------------------------------------------------
    // Methods concerning groups
    //--------------------------------------------------

    /**
     * Checks if a specific group exists in the block.
     *
     * @param nameOrId      Name or id of a group.
     *
     * @return True if the group exists, false otherwise.
     */
    @Cast("bool")
    public native boolean hasGroup(@StdString String nameOrId);

    /**
     * Checks if a specific group exists in the block.
     *
     * @param group         The group to check.
     *
     * @return True if the group exists, false otherwise.
     */
    @Cast("bool")
    public native boolean hasGroup(@Const @ByRef Group group);

    @ByVal @Name("getGroup")
    private native Group fetchGroup(@StdString String nameOrId);

    /**
     * Retrieves a specific group from the block by its name or id.
     *
     * @param nameOrId      Name or id of the group.
     *
     * @return The group with the specified id. If the group doesn't exist
     *         an exception will be thrown.
     */
    public Group getGroup(String nameOrId) {
        Group source = fetchGroup(nameOrId);
        if (source.isNone())
            return null;
        return source;
    }

    @ByVal @Name("getGroup")
    private native Group fetchGroup(@Cast("size_t") long index);

    /**
     * Retrieves a specific group by index.
     *
     * @param index     The index of the group.
     *
     * @return The group at the specified index.
     */
    public Group getGroup(long index) {
        Group source = fetchGroup(index);
        if (source.isNone())
            return null;
        return source;
    }

    /**
     * Returns the number of groups associated with this block.
     *
     * @return The number of groups.
     */
    @Name("groupCount")
    public native long getGroupCount();

    @ByVal @Name("createGroup")
    private native Group makeGroup(@StdString String name, @StdString String type);

    /**
     * Create a new group associated with this block.
     *
     * @param name       The name of the group to create.
     * @param type       The type of the tag.
     *
     * @return The newly created group.
     */
    public Group createGroup(String name, String type) {
        Group g = makeGroup(name, type);
        if (g.isNone())
            return null;
        return g;
    }

    /**
     * List all groups within this block.
     *
     * @return A vector that contains all filtered groups.
     */
    public List<Group> getGroups() {
        return ListBuilder.build(this::getGroupCount, this::getGroup);
    }

    /**
     * List all groups within this block.
     * The parameter filter can be used to filter groups by various criteria.
     *
     * @param filter        A filter function.
     *
     * @return A vector that contains all filtered groups.
     */
    public List<Group> getGroups(Predicate<Group> filter) {
        return ListBuilder.build(this::getGroupCount, this::getGroup);
    }

    /**
     * Deletes a Group from the block.
     * Deletes a group from the block and the file. The deletion can't be undone.
     *
     * @param nameOrId      Name or id of the group to remove.
     *
     * @return True if the group was removed, false otherwise.
     */
    @Cast("bool")
    public native boolean deleteGroup(@StdString String nameOrId);

    /**
     * Deletes a group from the block.
     * Deletes a group from the block and the file. The deletion can't be undone.
     *
     * @param group         The group to remove.
     *
     * @return True if the group was removed, false otherwise.
     */
    @Cast("bool")
    public native boolean deleteGroup(@Const @ByRef Group group);


    //--------------------------------------------------
    // Methods concerning data arrays
    //--------------------------------------------------

    /**
     * Checks if a specific data array exists in this block.
     *
     * @param nameOrId Name or id of a data array.
     * @return True if the data array exists, false otherwise.
     * @see DataArray
     */
    public native
    @Cast("bool")
    boolean hasDataArray(@StdString String nameOrId);

    /**
     * Checks if a specific data array exists in this block.
     *
     * @param dataArray The data array to check.
     * @return True if the data array exists, false otherwise.
     * @see DataArray
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
     * @see DataArray
     */
    public DataArray getDataArray(String nameOrId) {
        DataArray da = fetchDataArray(nameOrId);
        if (da.isNone()) {
            da = null;
        }
        return da;
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
     * @see DataArray
     */
    public DataArray getDataArray(long index) {
        DataArray da = fetchDataArray(index);
        if (da.isNone()) {
            da = null;
        }
        return da;
    }

    private native
    @ByVal
    VectorUtils.DataArrayVector dataArrays();

    /**
     * Get data arrays within this block.
     *
     * @return list of data arrays
     * @see DataArray
     */
    public List<DataArray> getDataArrays() {
        return dataArrays().getDataArrays();
    }

    /**
     * Get data arrays within this block.
     * <p>
     * The parameter filter can be used to filter data arrays by various
     * criteria.
     *
     * @param filter A filter function.
     * @return A list that contains all filtered data arrays.
     * @see DataArray
     */
    public List<DataArray> getDataArrays(Predicate<DataArray> filter) {
        List<DataArray> result = new ArrayList<>();
        for (DataArray dataArray : getDataArrays()) {
            if (filter.test(dataArray)) {
                result.add(dataArray);
            }
        }
        return result;
    }

    /**
     * Returns the number of all data arrays of the block.
     *
     * @return The number of data arrays of the block.
     * @see DataArray
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
     * @see DataArray
     */
    public DataArray createDataArray(String name, String type, int dataType, NDSize shape) {
        DataArray da = makeDataArray(name, type, dataType, shape);
        if (da.isNone()) {
            da = null;
        }
        return da;
    }

    /**
     * Deletes a data array from this block.
     * <p>
     * This deletes a data array and all its dimensions from the block and the file.
     * The deletion can't be undone.
     *
     * @param nameOrId Name or id of the data array to delete.
     * @return True if the data array was deleted, false otherwise.
     * @see DataArray
     */
    public native
    @Cast("bool")
    boolean deleteDataArray(@StdString String nameOrId);

    /**
     * Deletes a data array from this block.
     * <p>
     * This deletes a data array and all its dimensions from the block and the file.
     * The deletion can't be undone.
     *
     * @param dataArray The data array to delete.
     * @return True if the data array was deleted, false otherwise.
     * @see DataArray
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
     * @see Tag
     */
    public native
    @Cast("bool")
    boolean hasTag(@StdString String nameOrId);

    /**
     * Checks if a specific tag exists in the block.
     *
     * @param tag The tag to check.
     * @return True if the tag exists, false otherwise.
     * @see Tag
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
     * @see Tag
     */
    public Tag getTag(String nameOrId) {
        Tag tag = fetchTag(nameOrId);
        if (tag.isNone()) {
            tag = null;
        }
        return tag;
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
     * @see Tag
     */
    public Tag getTag(long index) {
        Tag tag = fetchTag(index);
        if (tag.isNone()) {
            tag = null;
        }
        return tag;
    }

    private native
    @ByVal
    VectorUtils.TagVector tags();

    /**
     * Get tags within this block.
     *
     * @return list of all tags.
     * @see Tag
     */
    public List<Tag> getTags() {
        return tags().getTags();
    }

    /**
     * Get tags within this block.
     * <p>
     * The parameter filter can be used to filter tags by various
     * criteria.
     *
     * @param filter A filter function.
     * @return A list that contains all filtered tags.
     * @see Tag
     */
    public List<Tag> getTags(Predicate<Tag> filter) {
        List<Tag> result = new ArrayList<>();
        for (Tag tag : getTags()) {
            if (filter.test(tag)) {
                result.add(tag);
            }
        }
        return result;
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
     * @see Tag
     */
    public Tag createTag(String name, String type, double[] position) {
        Tag tag = makeTag(name, type, position);
        if (tag.isNone()) {
            tag = null;
        }
        return tag;
    }

    /**
     * Deletes a tag from the block.
     * <p>
     * Deletes a tag with all its features from the block and the file.
     * The deletion can't be undone.
     *
     * @param nameOrId Name or id of the tag to remove.
     * @return True if the tag was removed, false otherwise.
     * @see Tag
     */
    public native
    @Cast("bool")
    boolean deleteTag(@StdString String nameOrId);

    /**
     * Deletes a tag from the block.
     * <p>
     * Deletes a tag with all its features from the block and the file.
     * The deletion can't be undone.
     *
     * @param tag The tag to remove.
     * @return True if the tag was removed, false otherwise.
     * @see Tag
     */
    public native
    @Cast("bool")
    boolean deleteTag(@Const @ByRef Tag tag);


    //--------------------------------------------------
    // Methods concerning multi tags.
    //--------------------------------------------------

    /**
     * Checks if a specific multi tag exists in the block.
     *
     * @param nameOrId Name or id of a multi tag.
     * @return True if the multi tag exists, false otherwise.
     * @see MultiTag
     */
    public native
    @Cast("bool")
    boolean hasMultiTag(@StdString String nameOrId);

    /**
     * Checks if a specific multi tag exists in the block.
     *
     * @param multiTag The multi tag to check.
     * @return True if the multi tag exists, false otherwise.
     * @see MultiTag
     */
    public native
    @Cast("bool")
    boolean hasMultiTag(@Const @ByRef MultiTag multiTag);

    private native
    @Name("getMultiTag")
    @ByVal
    MultiTag fetchMultiTag(@StdString String nameOrId);

    /**
     * Retrieves a specific multi tag from the block by its id.
     *
     * @param nameOrId Name or id of the multi tag.
     * @return The tag with the specified id. If this tag doesn't exist
     * an exception will be thrown.
     * @see MultiTag
     */
    public MultiTag getMultiTag(String nameOrId) {
        MultiTag multiTag = fetchMultiTag(nameOrId);
        if (multiTag.isNone()) {
            multiTag = null;
        }
        return multiTag;
    }

    private native
    @Name("getMultiTag")
    @ByVal
    MultiTag fetchMultiTag(@Cast("size_t") long index);

    /**
     * Retrieves a specific multi tag by index.
     *
     * @param index The index of the tag.
     * @return The multi tag at the specified index.
     * @see MultiTag
     */
    public MultiTag getMultiTag(long index) {
        MultiTag multiTag = fetchMultiTag(index);
        if (multiTag.isNone()) {
            multiTag = null;
        }
        return multiTag;
    }

    private native
    @ByVal
    VectorUtils.MultiTagVector multiTags();

    /**
     * Get multi tags within this block.
     *
     * @return A list that contains all filtered multi tags.
     * @see MultiTag
     */
    public List<MultiTag> getMultiTags() {
        return multiTags().getMultiTags();
    }

    /**
     * Get multi tags within this block.
     * <p>
     * The parameter filter can be used to filter multi tags by various     * criteria.
     *
     * @param filter A filter function.
     * @return A list that contains all filtered multi tags.
     * @see MultiTag
     */
    public List<MultiTag> getMultiTags(Predicate<MultiTag> filter) {
        List<MultiTag> result = new ArrayList<>();
        for (MultiTag multiTag : getMultiTags()) {
            if (filter.test(multiTag)) {
                result.add(multiTag);
            }
        }
        return result;
    }

    /**
     * Returns the number of multi tags associated with this block.
     *
     * @return The number of multi tags.
     * @see MultiTag
     */
    public native
    @Name("multiTagCount")
    long getMultiTagCount();

    private native
    @Name("createMultiTag")
    @ByVal
    MultiTag makeMultiTag(@StdString String name, @StdString String type,
                          @Const @ByRef DataArray positions);

    /**
     * Create a new multi tag associated with this block.
     *
     * @param name      The name of the multi tag to create.
     * @param type      The type of the tag.
     * @param positions The positions of the tag.
     * @return The newly created tag.
     * @see MultiTag
     */
    public MultiTag createMultiTag(String name, String type, DataArray positions) {
        MultiTag multiTag = makeMultiTag(name, type, positions);
        if (multiTag.isNone()) {
            multiTag = null;
        }
        return multiTag;
    }

    /**
     * Deletes a multi tag from the block.
     * <p>
     * Deletes a multi tag and all its features from the block and the file.
     * The deletion can't be undone.
     *
     * @param nameOrId Name or id of the tag to remove.
     * @return True if the tag was removed, false otherwise.
     * @see MultiTag
     */
    public native
    @Cast("bool")
    boolean deleteMultiTag(@StdString String nameOrId);

    /**
     * Deletes a multi tag from the block.
     * <p>
     * Deletes a multi tag and all its features from the block and the file.
     * The deletion can't be undone.
     *
     * @param multiTag The tag to remove.
     * @return True if the tag was removed, false otherwise.
     * @see MultiTag
     */
    public native
    @Cast("bool")
    boolean deleteMultiTag(@Const @ByRef MultiTag multiTag);


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
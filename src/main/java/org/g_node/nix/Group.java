package org.g_node.nix;

import java.util.*;
import java.util.function.Predicate;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.annotation.*;
import org.bytedeco.javacpp.annotation.Properties;
import org.g_node.nix.base.*;
import org.g_node.nix.internal.*;
import org.g_node.nix.internal.OptionalUtils.OptionalString;

/**
 * An additional grouping element within Block.
 */
@Properties(value = {
        @Platform(include = {"<nix/Group.hpp>"}),
        @Platform(value = "linux", link = BuildLibs.NIX_1, preload = BuildLibs.HDF5_7),
        @Platform(value = "windows",
                link = BuildLibs.NIX,
                preload = {BuildLibs.HDF5, BuildLibs.MSVCP120, BuildLibs.MSVCR120, BuildLibs.SZIP, BuildLibs.ZLIB})})
@Namespace("nix")
public class Group extends EntityWithSources {

    static {
        Loader.load();
    }

    private native void allocate();

    @Override @Cast("bool")
    public native boolean isNone();

    /**
     * Get id of the group.
     *
     * @return The ID string.
     */
    @Name("id") @StdString
    public native String getId();

    @Cast("time_t")
    private native long createdAt();

    /**
     * Get the creation date of the group.
     *
     * @return The creation date of the group.
     */
    public Date getCreatedAt() {
        return DateUtils.convertSecondsToDate(createdAt());
    }

    @Cast("time_t")
    private native long updatedAt();

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
     * Setter for the type of the group.
     *
     * @param type The type of the group.
     */
    @Name("type")
    public native void setType(@StdString String type);

    /**
     * Getter for the type of the group.
     *
     * @return The type of the group.
     */
    @Name("type") @StdString
    public native String getType();

    /**
     * Getter for the name of the group.
     *
     * @return The name of the group.
     */
    @Name("name") @StdString
    public native String getName();

    private native void definition(@Const @ByVal None none);

    private native void definition(@StdString String definition);

    /**
     * Setter for the definition of the group. If <tt>null</tt> is passed definition is removed.
     *
     * @param definition definition of the group.
     */
    public void setDefinition(String definition) {
        if (definition != null) {
            definition(definition);
        } else {
            definition(new None());
        }
    }

    @ByVal
    private native OptionalString definition();

    /**
     * Getter for the definition of the group.
     *
     * @return The definition of the group.
     */
    public String getDefinition() {
        OptionalString definition = definition();
        if (! definition.isPresent())
            return null;
        return definition.getString();
    }

    //--------------------------------------------------
    // Methods concerning metadata
    //--------------------------------------------------

    @ByVal
    private native Section metadata();

    private native void metadata(@Const @ByVal None t);

    /**
     * Get metadata associated with this entity.
     *
     * @return The associated section, if no such section exists <tt>null</tt> is returned.
     * @see Section
     */
    @Name("metadata")
    public Section getMetadata() {
        Section section = metadata();
        if (section.isNone())
            return null;
        return section;
    }

    /**
     * Associate the entity with some metadata.
     * Calling this method will replace an existing association.
     *
     * @param metadata The {@link Section} that should be associated
     *                 with this entity.
     * @see Section
     */
    @Name("metadata")
    public native void setMetadata(@Const @ByRef Section metadata);

    /**
     * Associate the entity with some metadata.
     * Calling this method will replace an existing association.
     *
     * @param id The id of the {@link Section} that should be associated
     *           with this entity.
     * @see Section
     */
    @Name("metadata")
    public native void setMetadata(@StdString String id);

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
     * Get the number of sources associated with this entity.
     *
     * @return The number sources.
     * @see Source
     */
    @Name("sourceCount")
    public native long getSourceCount();

    /**
     * Checks if a specific source is associated with this entity.
     *
     * @param id The source id to check.
     * @return True if the source is associated with this entity, false otherwise.
     * @see Source
     */
    @Cast("bool")
    public native boolean hasSource(@StdString String id);

    /**
     * Checks if a specific source is associated with this entity.
     *
     * @param source The source to check.
     * @return True if the source is associated with this entity, false otherwise.
     * @see Source
     */
    @Cast("bool")
    public native boolean hasSource(@Const @ByRef Source source);

    @Name("getSource") @ByVal
    private native Source fetchSource(@StdString String id);

    /**
     * Returns an associated source identified by its id.
     *
     * @param id The id of the associated source.
     * @see Source
     */
    public Source getSource(String id) {
        Source source = fetchSource(id);
        if (source.isNone())
            return null;
        return source;
    }

    @Name("getSource") @ByVal
    private native Source fetchSource(@Cast("size_t") long index);

    /**
     * Retrieves an associated source identified by its index.
     *
     * @param index The index of the associated source.
     * @return The source with the given id. If it doesn't exist an exception
     * will be thrown.
     * @see Source
     */
    public Source getSource(long index) {
        Source source = fetchSource(index);
        if (source.isNone())
            return null;
        return source;
    }

    /**
     * Get all sources associated with this entity.
     *
     * @return All associated sources that match the given filter as a list.
     * @see Source
     */
    public List<Source> getSources() {
        return ListBuilder.build(this::getSourceCount, this::getSource);
    }

    /**
     * Get all sources associated with this entity.
     *
     * @return All associated sources that match the given filter as a list.
     * @see Source
     */
    public List<Source> getSources(Predicate<Source> filter) {
        return ListBuilder.build(this::getSourceCount, this::getSource, filter);
    }

    /**
     * Set all sources associations for this entity.
     * All previously existing associations will be overwritten.
     *
     * @param sources A list with all sources.
     * @see Source
     */
    public void setSources(List<Source> sources) {
        // remove if not in sources
        getSources(s -> !sources.contains(s))
                .forEach(this::removeSource);
        // add if not already there
        sources.stream()
                .filter(s -> !hasSource(s))
                .forEach(this::addSource);
    }

    /**
     * Associate a new source with the entity.
     * If a source with the given id already is associated with the
     * entity, the call will have no effect.
     *
     * @param id The id of the source.
     * @see Source
     */
    public native void addSource(@StdString String id);

    /**
     * Associate a new source with the entity.
     * Calling this method will have no effect if the source is already associated to this entity.
     *
     * @param source The source to add.
     * @see Source
     */
    public native void addSource(@Const @ByRef Source source);

    /**
     * Remove a source from the list of associated sources.
     * This method just removes the association between the entity and the source.
     * The source itself will not be deleted from the file.
     *
     * @param id The id of the source to remove.
     * @return True if the source was removed, false otherwise.
     * @see Source
     */
    @Cast("bool")
    public native boolean removeSource(@StdString String id);

    /**
     * Remove a source from the list of associated sources.
     * This method just removes the association between the entity and the source.
     * The source itself will not be deleted from the file.
     *
     * @param source The source to remove.
     * @return True if the source was removed, false otherwise.
     * @see Source
     */
    @Cast("bool")
    public native boolean removeSource(@Const @ByRef Source source);

    //--------------------------------------------------
    // Methods concerning data arrays
    //--------------------------------------------------

    /**
     * Checks if a specific data array exists in this group.
     *
     * @param nameOrId      Name or id of a data array.
     *
     * @return True if the data array exists, false otherwise.
     * @see DataArray
     */
    @Cast("bool")
    public native boolean hasDataArray(@StdString String nameOrId);

    /**
     * Returns the number of all data arrays of the block.
     *
     * @return The number of data arrays of the block.
     * @see DataArray
     */
    @Name("dataArrayCount")
    public native long getDataArrayCount();

    /**
     * Checks if a specific data array exists in this group.
     *
     * @param dataArray     The data array to check.
     *
     * @return True if the data array exists, false otherwise.
     * @see DataArray
     */
    @Cast("bool")
    public native boolean hasDataArray(@Const @ByRef DataArray dataArray);

    @Name("getDataArray") @ByVal
    private native DataArray fetchDataArray(@StdString String nameOrId);

    @Name("getDataArray") @ByVal
    private native DataArray fetchDataArray(@Cast("size_t") long index);

    /**
     * Retrieves a specific data array from the group by name or id.
     *
     * @param nameOrId      Name or id of an existing data array.
     *
     * @return The data array with the specified id. If this
     *         doesn't exist, an exception will be thrown.
     * @see DataArray
     */
    public DataArray getDataArray(String nameOrId) {
        DataArray da = fetchDataArray(nameOrId);
        if (da.isNone())
            return null;
        return da;
    }

    /**
     * Retrieves a data array by index.
     *
     * @param index         The index of the data array.
     *
     * @return The data array at the specified index.
     * @see DataArray
     */
    public DataArray getDataArray(long index) {
        DataArray da = fetchDataArray(index);
        if (da.isNone())
            return null;
        return da;
    }

    /**
     * Get data arrays within this group.
     *
     * @return A list of data arrays
     * @see DataArray
     */
    public List<DataArray> getDataArrays() {
        return ListBuilder.build(this::getDataArrayCount, this::getDataArray);
    }

    /**
     * Get data arrays within this group.
     * The parameter filter can be used to filter data arrays by various criteria.
     *
     * @param filter        A filter function.
     *
     * @return A list that contains all filtered data arrays.
     * @see DataArray
     */
    public List<DataArray> getDataArrays(Predicate<DataArray> filter) {
        return ListBuilder.build(this::getDataArrayCount, this::getDataArray, filter);
    }

    /**
     * Add a DataArray to the list of referenced data of the group.
     *
     * @param nameOrId      The id of the DataArray to add.
     */
    public native void addDataArray(@StdString String nameOrId);

    /**
     * Add a DataArray to the list of referenced data of the group.
     *
     * @param dataArray     The DataArray to add.
     */
    public native void addDataArray(@Const @ByRef DataArray dataArray);

    /**
     * Remove a DataArray from the list of referenced data of the group.
     *
     * This method just removes the association between the data array and the
     * group, the data array itself will not be removed from the file.
     *
     * @param nameOrId      The id of the DataArray to remove.
     *
     * @return True if the DataArray was removed, false otherwise.
     */
    @Cast("bool")
    public native boolean removeDataArray(@StdString String nameOrId);

    /**
     * Remove a DataArray from the list of referenced data of the group.
     *
     * This method just removes the association between the data array and the
     * group, the data array itself will not be removed from the file.
     *
     * @param dataArray     The DataArray to remove.
     *
     * @return True if the DataArray was removed, false otherwise.
     */
    @Cast("bool")
    public native boolean removeDataArray(@Const @ByRef DataArray dataArray);


    //--------------------------------------------------
    // Methods concerning tags
    //--------------------------------------------------



    //--------------------------------------------------
    // Methods concerning multi tags
    //--------------------------------------------------
}

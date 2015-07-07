package org.gnode.nix;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.annotation.*;
import org.gnode.nix.base.EntityWithSources;
import org.gnode.nix.internal.DateUtils;
import org.gnode.nix.internal.None;
import org.gnode.nix.internal.OptionalUtils;
import org.gnode.nix.internal.VectorUtils;

import java.util.Date;
import java.util.List;

@Properties(value = {
        @Platform(include = {"<nix/MultiTag.hpp>"}, link = "nix"),
        @Platform(value = "linux"),
        @Platform(value = "windows")})
@Namespace("nix")
public class MultiTag extends EntityWithSources {
    static {
        Loader.load();
    }

    //--------------------------------------------------
    // Constructors
    //--------------------------------------------------

    /**
     * Constructor that creates an uninitialized MultiTag.
     * <p>
     * Calling any method on an uninitialized MultiTag will throw a {@link java.lang.RuntimeException}
     * exception.
     */
    public MultiTag() {
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
     * Get id of the multitag
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
     * Get the creation date of the multitag.
     *
     * @return The creation date of the multitag.
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
     * Setter for the type of the multitag.
     *
     * @param type The type of the multitag.
     */
    public native
    @Name("type")
    void setType(@StdString String type);

    /**
     * Getter for the type of the multitag.
     *
     * @return The type of the multitag.
     */
    public native
    @Name("type")
    @StdString
    String getType();

    /**
     * Getter for the name of the multitag.
     *
     * @return The name of the multitag.
     */
    public native
    @Name("name")
    @StdString
    String getName();

    private native void definition(@Const @ByVal None t);

    private native void definition(@StdString String definition);

    /**
     * Setter for the definition of the multitag. If null is passed definition is removed.
     *
     * @param definition definition of multitag.
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
     * Getter for the definition of the multitag.
     *
     * @return The definition of the multitag.
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
     * <p>
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
     * <p>
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

    /**
     * Get the number of sources associated with this entity.
     *
     * @return The number sources.
     */
    public native
    @Name("sourceCount")
    long getSourceCount();

    /**
     * Checks if a specific source is associated with this entity.
     *
     * @param id The source id to check.
     * @return True if the source is associated with this entity, false otherwise.
     */
    public native
    @Cast("bool")
    boolean hasSource(@StdString String id);

    /**
     * Checks if a specific source is associated with this entity.
     *
     * @param source The source to check.
     * @return True if the source is associated with this entity, false otherwise.
     */
    public native
    @Cast("bool")
    boolean hasSource(@Const @ByRef Source source);

    private native
    @Name("getSource")
    @ByVal
    Source fetchSource(@StdString String id);

    /**
     * Returns an associated source identified by its id.
     *
     * @param id The id of the associated source.
     */
    public Source getSource(String id) {
        Source source = fetchSource(id);
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
     * Retrieves an associated source identified by its index.
     *
     * @param index The index of the associated source.
     * @return The source with the given id. If it doesn't exist an exception
     * will be thrown.
     */
    public Source getSource(long index) {
        Source source = fetchSource(index);
        if (source.isInitialized()) {
            return source;
        }
        return null;
    }

    private native
    @ByVal
    VectorUtils.SourceVector sources();

    /**
     * Get all sources associated with this entity.
     *
     * @return All associated sources that match the given filter as a vector
     */
    public List<Source> getSources() {
        return sources().getSources();
    }

    private native void sources(@Const @ByRef VectorUtils.SourceVector sources);

    /**
     * Set all sources associations for this entity.
     * <p>
     * All previously existing associations will be overwritten.
     *
     * @param sources A vector with all sources.
     */
    public void setSources(List<Source> sources) {
        sources(new VectorUtils.SourceVector(sources));
    }

    /**
     * Associate a new source with the entity.
     * <p>
     * If a source with the given id already is associated with the
     * entity, the call will have no effect.
     *
     * @param id The id of the source.
     */
    public native void addSource(@StdString String id);

    /**
     * Associate a new source with the entity.
     * <p>
     * Calling this method will have no effect if the source is already associated to this entity.
     *
     * @param source The source to add.
     */
    public native void addSource(@Const @ByRef Source source);

    /**
     * Remove a source from the list of associated sources.
     * <p>
     * This method just removes the association between the entity and the source.
     * The source itself will not be deleted from the file.
     *
     * @param id The id of the source to remove.
     * @return True if the source was removed, false otherwise.
     */
    public native
    @Cast("bool")
    boolean removeSource(@StdString String id);

    /**
     * Remove a source from the list of associated sources.
     * <p>
     * This method just removes the association between the entity and the source.
     * The source itself will not be deleted from the file.
     *
     * @param source The source to remove.
     * @return True if the source was removed, false otherwise.
     */
    public native
    @Cast("bool")
    boolean removeSource(@Const @ByRef Source source);


    //--------------------------------------------------
    // Positions and extents
    //--------------------------------------------------

    private native
    @ByVal
    DataArray positions();

    /**
     * Getter for the positions of a tag.
     * <p>
     * The positions of a multi tag are defined in a DataArray. This array has to define a set of
     * origin vectors, each defining a point inside the referenced data or the beginning of a
     * region of interest.
     *
     * @return The DataArray defining the positions of the tag.
     */
    public DataArray getPositions() {
        DataArray da = positions();
        if (da.isInitialized()) {
            return da;
        }
        return null;
    }

    /**
     * Setter for the positions of the tag.
     *
     * @param nameOrId Name or id of the DataArray that defines the positions for this tag.
     */
    public native
    @Name("positions")
    void setPositions(@StdString String nameOrId);


    /**
     * Setter for the positions of the tag.
     *
     * @param positions The DataArray containing the positions of the tag.
     */
    public native
    @Name("positions")
    void setPositions(@Const @ByRef DataArray positions);

    /**
     * Determine whether this DataArray contains positions.
     *
     * @return True if the DataArray has positions, false otherwise.
     */
    public native
    @Cast("bool")
    boolean hasPositions();

    private native
    @ByVal
    DataArray extents();

    /**
     * Getter for the extents of a tag.
     * <p>
     * The extents of a multi tag are defined in an associated DataArray. This array has to define a set of
     * extent vectors, each defining the size of the corresponding region of interest.
     *
     * @return The DataArray defining the extents of the tag.
     */
    public DataArray getExtents() {
        DataArray da = extents();
        if (da.isInitialized()) {
            return da;
        }
        return null;
    }

    /**
     * Sets the extents DataArray of the tag.
     *
     * @param extents The DataArray containing the extents of the tag.
     */
    public native
    @Name("extents")
    void setExtents(@Const @ByRef DataArray extents);

    /**
     * Setter for the positions of the tag.
     *
     * @param nameOrId Name or id of the DataArray that defines the extents of the tag.
     */
    public native
    @Name("extents")
    void setExtents(@StdString String nameOrId);

    private native void extents(@Const @ByVal None t);

    /**
     * Deleter for the reference to the extents DataArray.
     * <p>
     * This function only removes the association between the tag and the data array,
     * but does not delete the data array itself.
     */
    public void removeExtents() {
        extents(new None());
    }

    private native
    @ByVal
    VectorUtils.StringVector units();

    /**
     * Gets for the units of the tag.
     * <p>
     * The units are applied to all values for position and extent in order to calculate the right
     * position vectors in referenced data arrays.
     *
     * @return All units of the tag as a list.
     */
    public List<String> getUnits() {
        return units().getStrings();
    }

    private native void units(@Const @ByRef VectorUtils.StringVector units);

    private native void units(@Const @ByVal None t);

    /**
     * Setter for the units of a tag.
     * <p>
     * All previously defined units will be replaced by the ones passed
     * to the units parameter.
     *
     * @param units All units as a list. If {@link null} removes the units.
     */
    public void setUnits(List<String> units) {
        if (units != null) {
            units(new VectorUtils.StringVector(units));
        } else {
            units(new None());
        }
    }

    //--------------------------------------------------
    // Methods concerning references.
    //--------------------------------------------------


    /**
     * Checks if the specified DataArray is referenced by the tag.
     *
     * @param reference The data array to check.
     * @return True if the data array is referenced, false otherwise.
     */
    public native
    @Cast("bool")
    boolean hasReference(@Const @ByRef DataArray reference);

    /**
     * Checks if the specified DataArray is referenced in this tag.
     *
     * @param nameOrId Name or id of the data array to check.
     * @return True if a data array with the given id is referenced, false otherwise.
     */
    public native
    @Cast("bool")
    boolean hasReference(@StdString String nameOrId);

    /**
     * Get the number of DataArray entities that are referenced by the tag.
     *
     * @return The number of referenced data arrays.
     */
    public native
    @Name("referenceCount")
    long getReferenceCount();

    private native
    @Name("getReference")
    @ByVal
    DataArray fetchReference(@StdString String nameOrId);

    /**
     * Get a referenced DataArray by its name or id.
     *
     * @param nameOrId Name or id of the data array.
     * @return The referenced data array.
     */
    public DataArray getReference(String nameOrId) {
        DataArray da = fetchReference(nameOrId);
        if (da.isInitialized()) {
            return da;
        }
        return null;
    }

    private native
    @Name("getReference")
    @ByVal
    DataArray fetchReference(@Cast("size_t") long index);

    /**
     * Get a referenced DataArray by its index.
     *
     * @param index The index of the data array.
     * @return The referenced data array.
     */
    public DataArray getReference(long index) {
        DataArray da = fetchReference(index);
        if (da.isInitialized()) {
            return da;
        }
        return null;
    }

    /**
     * Add a new DataArray to the list of referenced data.
     *
     * @param nameOrId Name or id of the data array.
     */
    public native void addReference(@StdString String nameOrId);

    /**
     * Adds a new DataArray to the list of referenced data.
     *
     * @param reference The DataArray that should be referenced.
     */
    public native void addReference(@Const @ByRef DataArray reference);

    /**
     * Remove a DataArray from the list of referenced data.
     * <p>
     * This function only removes the association between the tag and the data array,
     * but does not delete the data array itself.
     *
     * @param nameOrId Name or id of the data array.
     * @return True if the data array was removed, false otherwise.
     */
    public native
    @Cast("bool")
    boolean removeReference(@StdString String nameOrId);

    /**
     * Remove a DataArray from the list of referenced data.
     * <p>
     * This function only removes the association between the tag and the data array,
     * but does not delete the data array itself.
     *
     * @param reference The DataArray to remove.
     * @return True if the data array was removed, false otherwise.
     */
    public native
    @Cast("bool")
    boolean removeReference(@Const @ByRef DataArray reference);


    private native
    @ByVal
    VectorUtils.DataArrayVector references();

    /**
     * Get all referenced data arrays associated with the tag.
     * <p>
     * Always uses filter that accepts all sources.
     *
     * @return A vector containing all filtered DataArray entities.
     */
    public List<DataArray> getReferences() {
        return references().getDataArrays();
    }

    private native void references(@Const @ByRef VectorUtils.DataArrayVector references);

    /**
     * Setter for all referenced DataArrays.
     * <p>
     * Previously referenced data will be replaced.
     * removed.
     *
     * @param references All referenced arrays.
     */
    public void setReferences(List<DataArray> references) {
        references(new VectorUtils.DataArrayVector(references));
    }

    /**
     * Retrieves the data slice tagged by a certain position and extent
     * of a certain reference.
     *
     * @param positionIndex  the index of the requested position.
     * @param referenceIndex the index of the requested reference.
     * @return the requested data.
     */
    public native
    @ByVal
    DataView retrieveData(@Cast("size_t") long positionIndex, @Cast("size_t") long referenceIndex);

    //--------------------------------------------------
    // Methods concerning features.
    //--------------------------------------------------

    /**
     * Checks if a specific feature exists on the tag.
     *
     * @param nameOrId Name or id of a feature.
     * @return True if the feature exists, false otherwise.
     */
    public native
    @Cast("bool")
    boolean hasFeature(@StdString String nameOrId);

    /**
     * Checks if a specific feature exists on the tag.
     *
     * @param feature The Feature to check.
     * @return True if the feature exists, false otherwise.
     */
    public native
    @Cast("bool")
    boolean hasFeature(@Const @ByRef Feature feature);

    /**
     * Returns the number of features in this block.
     *
     * @return The number of features.
     */
    public native
    @Name("featureCount")
    long getFeatureCount();

    private native
    @Name("getFeature")
    @ByVal
    Feature fetchFeature(@StdString String id);

    /**
     * Retrieves a specific feature from the tag.
     *
     * @param nameOrId Name or id of the feature.
     * @return The feature with the specified name or id. If it doesn't exist
     * an exception will be thrown.
     */
    public Feature getFeature(String nameOrId) {
        Feature feature = fetchFeature(nameOrId);
        if (feature.isInitialized()) {
            return feature;
        }
        return null;
    }

    private native
    @Name("getFeature")
    @ByVal
    Feature fetchFeature(@Cast("size_t") long index);

    /**
     * Retrieves a specific feature from the tag.
     *
     * @param index The index of the feature.
     * @return The feature with the specified index.
     */
    public Feature getFeature(long index) {
        Feature feature = fetchFeature(index);
        if (feature.isInitialized()) {
            return feature;
        }
        return null;
    }

    private native
    @ByVal
    VectorUtils.FeatureVector features();

    /**
     * Get all Feature entities contained in the tag.
     *
     * @return A vector containing all filtered Feature entities.
     */
    public List<Feature> getFeatures() {
        return features().getFeatures();
    }

    private native
    @Name("createFeature")
    @ByVal
    Feature makeFeature(@Const @ByRef DataArray data, @Cast("nix::LinkType") int linkType);


    public Feature createFeature(DataArray data, int linkType) {
        Feature feature = makeFeature(data, linkType);
        if (feature.isInitialized()) {
            return feature;
        }
        return null;
    }

    private native
    @Name("createFeature")
    @ByVal
    Feature makeFeature(@StdString String dataArrayId, @Cast("nix::LinkType") int linkType);

    /**
     * Create a new feature.
     *
     * @param dataArrayId The data array that is part of the new feature.
     * @param linkType    The link type of this feature.
     * @return The created feature object.
     */
    public Feature createFeature(String dataArrayId, int linkType) {
        Feature feature = makeFeature(dataArrayId, linkType);
        if (feature.isInitialized()) {
            return feature;
        }
        return null;
    }

    /**
     * Delete a feature from the tag.
     *
     * @param nameOrId Name or id of the feature to remove.
     * @return True if the feature was removed, false otherwise.
     */
    public native
    @Cast("bool")
    boolean deleteFeature(@StdString String nameOrId);

    /**
     * Delete a feature from the tag.
     *
     * @param feature The feature to remove.
     * @return True if the feature was removed, false otherwise.
     */
    public native
    @Cast("bool")
    boolean deleteFeature(@Const @ByRef Feature feature);

    /**
     * Retrieves the data stored in a feature related to a certain
     * position of this tag.
     *
     * @param positionIndex The index of the requested position
     * @param featureIndex  The index of the selected feature
     * @return The data
     */
    public native
    @ByVal
    DataView retrieveFeatureData(@Cast("size_t") long positionIndex, @Cast("size_t") long featureIndex);

    //--------------------------------------------------
    // Overrides
    //--------------------------------------------------

    @Override
    public String toString() {
        return "MultiTag: {name = " + this.getName()
                + ", type = " + this.getType()
                + ", id = " + this.getId() + "}";
    }
}
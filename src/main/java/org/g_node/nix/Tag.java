package org.g_node.nix;

import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.annotation.*;
import org.g_node.nix.internal.*;
import org.g_node.nix.base.EntityWithSources;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

/**
 * <h1>Tag</h1>
 * A tag class that defines a single point or region of interest.
 * <p>
 * Besides the {@link DataArray} the tag entities can be considered as the other
 * core entities of the data model.
 * They are meant to attach annotations directly to the data and to establish meaningful
 * links between different kinds of stored data.
 * Most importantly tags allow the definition of points or regions of interest in data
 * that is stored in other {@link DataArray} entities. The data array entities the
 * tag applies to are defined by its property {@link Tag#getReferences(Predicate)}.
 * <p>
 * Further the referenced data is defined by an origin vector called {@link Tag#getPosition()}
 * and an optional {@link Tag#getExtent()} vector that defines its size.
 * Therefore position and extent of a tag, together with the references field
 * defines a group of points or regions of interest collected from a subset of all
 * available {@link DataArray} entities.
 * <p>
 * Further tags have a field called {@link Tag#getFeatures()} which makes it possible to associate
 * other data with the tag.  Semantically a feature of a tag is some additional data that
 * contains additional information about the points of hyperslabs defined by the tag.
 * This could be for example data that represents a stimulus (e.g. an image or a
 * signal) that was applied in a certain interval during the recording.
 *
 * @see DataArray
 * @see Feature
 * @see MultiTag
 */

@Properties(value = {
        @Platform(include = {"<nix/Tag.hpp>"}),
        @Platform(value = "linux", link = BuildLibs.NIX_1, preload = BuildLibs.HDF5_7),
        @Platform(value = "macosx", link = BuildLibs.NIX, preload = BuildLibs.HDF5),
        @Platform(value = "windows",
                link = BuildLibs.NIX,
                preload = {BuildLibs.HDF5, BuildLibs.MSVCP120, BuildLibs.MSVCR120, BuildLibs.SZIP, BuildLibs.ZLIB})})
@Namespace("nix")
public class Tag extends EntityWithSources {
    static {
        Loader.load();
    }

    //--------------------------------------------------
    // Constructors
    //--------------------------------------------------

    /**
     * Constructor that creates an uninitialized Tag.
     * <p>
     * Calling any method on an uninitialized Tag will throw a {@link RuntimeException}
     * exception.
     */
    public Tag() {
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
     * Get id of the tag.
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
     * Get the creation date of the tag.
     *
     * @return The creation date of the tag.
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
     * Setter for the type of the tag.
     *
     * @param type The type of the tag.
     */
    public native
    @Name("type")
    void setType(@StdString String type);

    /**
     * Getter for the type of the tag.
     *
     * @return The type of the tag.
     */
    public native
    @Name("type")
    @StdString
    String getType();

    /**
     * Getter for the name of the tag.
     *
     * @return The name of the tag.
     */
    public native
    @Name("name")
    @StdString
    String getName();

    private native void definition(@Const @ByVal None t);

    private native void definition(@StdString String definition);

    /**
     * Setter for the definition of the tag. If <tt>null</tt> is passed definition is removed.
     *
     * @param definition definition of tag.
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
     * Getter for the definition of the tag.
     *
     * @return The definition of the tag.
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
     * @return The associated section, if no such section exists <tt>null</tt> is returned.
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

    /**
     * Get the number of sources associated with this entity.
     *
     * @return The number sources.
     * @see Source
     */
    public native
    @Name("sourceCount")
    long getSourceCount();

    /**
     * Checks if a specific source is associated with this entity.
     *
     * @param id The source id to check.
     * @return True if the source is associated with this entity, false otherwise.
     * @see Source
     */
    public native
    @Cast("bool")
    boolean hasSource(@StdString String id);

    /**
     * Checks if a specific source is associated with this entity.
     *
     * @param source The source to check.
     * @return True if the source is associated with this entity, false otherwise.
     * @see Source
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
     * @see Source
     */
    public Source getSource(String id) {
        Source source = fetchSource(id);
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
     * Retrieves an associated source identified by its index.
     *
     * @param index The index of the associated source.
     * @return The source with the given id. If it doesn't exist an exception
     * will be thrown.
     * @see Source
     */
    public Source getSource(long index) {
        Source source = fetchSource(index);
        if (source.isNone()) {
            source = null;
        }
        return source;
    }

    private native
    @ByVal
    VectorUtils.SourceVector sources();

    /**
     * Get all sources associated with this entity.
     *
     * @return All associated sources that match the given filter as a vector.
     * @see Source
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
     * @see Source
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
     * @see Source
     */
    public native void addSource(@StdString String id);

    /**
     * Associate a new source with the entity.
     * <p>
     * Calling this method will have no effect if the source is already associated to this entity.
     *
     * @param source The source to add.
     * @see Source
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
     * @see Source
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
     * @see Source
     */
    public native
    @Cast("bool")
    boolean removeSource(@Const @ByRef Source source);


    //--------------------------------------------------
    // Element getters and setters
    //--------------------------------------------------

    private native
    @ByVal
    VectorUtils.StringVector units();

    /**
     * Gets the units of the tag.
     * <p>
     * The units are applied to all values for position and extent in order to calculate the
     * right position vectors in referenced data arrays.
     *
     * @return All units of the tag as a list.
     */
    public List<String> getUnits() {
        return units().getStrings();
    }

    private native void units(@Const @ByRef VectorUtils.StringVector units);

    private native void units(@Const @ByVal None t);

    /**
     * Sets the units of a tag.
     *
     * @param units All units as a list. If <tt>null</tt> removes the units.
     */
    public void setUnits(List<String> units) {
        if (units != null) {
            units(new VectorUtils.StringVector(units));
        } else {
            units(new None());
        }
    }

    private native
    @Name("position")
    @StdVector
    DoublePointer fetchPosition();

    /**
     * Gets the position of a tag.
     * <p>
     * The position is a vector that points into referenced DataArrays.
     *
     * @return The position array.
     */
    public double[] getPosition() {
        return VectorUtils.convertPointerToArray(fetchPosition());
    }

    /**
     * Sets the position of a tag.
     *
     * @param position The position vector list.
     */
    public native
    @Name("position")
    void setPosition(@StdVector double[] position);

    private native
    @StdVector
    DoublePointer extent();

    /**
     * Gets the extent of a tag.
     * <p>
     * Given a specified position vector, the extent vector defined the size
     * of a region of interest in the referenced DataArray entities.
     *
     * @return The extent of the tag.
     */
    public double[] getExtent() {
        return VectorUtils.convertPointerToArray(extent());
    }

    private native void extent(@StdVector double[] extent);

    private native void extent(@Const @ByVal None t);

    /**
     * Sets the extent of a tag.
     *
     * @param extent The extent vector list.
     */
    public void setExtent(double[] extent) {
        if (extent != null) {
            extent(extent);
        } else {
            extent(new None());
        }
    }

    //--------------------------------------------------
    // Methods concerning references.
    //--------------------------------------------------

    /**
     * Checks whether a DataArray is referenced by the tag.
     *
     * @param id The id of the DataArray to check.
     * @return True if the data array is referenced, false otherwise.
     * @see DataArray
     */
    public native
    @Cast("bool")
    boolean hasReference(@StdString String id);

    /**
     * Checks whether a DataArray is referenced by the tag.
     *
     * @param reference The DataArray to check.
     * @return True if the data array is referenced, false otherwise.
     * @see DataArray
     */
    public native
    @Cast("bool")
    boolean hasReference(@Const @ByRef DataArray reference);

    /**
     * Gets the number of referenced DataArray entities of the tag.
     *
     * @return The number of referenced data arrays.
     * @see DataArray
     */
    public native
    @Name("referenceCount")
    long getReferenceCount();

    private native
    @Name("getReference")
    @ByVal
    DataArray fetchReference(@StdString String id);

    /**
     * Gets a specific referenced DataArray from the tag.
     *
     * @param id The id of the referenced DataArray.
     * @return The referenced data array.
     * @see DataArray
     */
    public DataArray getReference(String id) {
        DataArray da = fetchReference(id);
        if (da.isNone()) {
            da = null;
        }
        return da;
    }

    private native
    @Name("getReference")
    @ByVal
    DataArray fetchReference(@Cast("size_t") long index);

    /**
     * Gets a referenced DataArray by its index.
     *
     * @param index The index of the DataArray.
     * @return The referenced data array.
     * @see DataArray
     */
    public DataArray getReference(long index) {
        DataArray da = fetchReference(index);
        if (da.isNone()) {
            da = null;
        }
        return da;
    }

    /**
     * Add a DataArray to the list of referenced data of the tag.
     *
     * @param reference The DataArray to add.
     * @see DataArray
     */
    public native void addReference(@Const @ByRef DataArray reference);

    /**
     * Add a DataArray to the list of referenced data of the tag.
     *
     * @param id The id of the DataArray to add.
     * @see DataArray
     */
    public native void addReference(@StdString String id);

    /**
     * Remove a DataArray from the list of referenced data of the tag.
     * <p>
     * This method just removes the association between the data array and the
     * tag, the data array itself will not be removed from the file.
     *
     * @param reference The DataArray to remove.
     * @return True if the DataArray was removed, false otherwise.
     * @see DataArray
     */
    public native
    @Cast("bool")
    boolean removeReference(@Const @ByRef DataArray reference);

    /**
     * Remove a DataArray from the list of referenced data of the tag.
     * <p>
     * This method just removes the association between the data array and the
     * tag, the data array itself will not be removed from the file.
     *
     * @param id The id of the DataArray to remove.
     * @return True if the DataArray was removed, false otherwise.
     * @see DataArray
     */
    public native
    @Cast("bool")
    boolean removeReference(@StdString String id);

    private native
    @ByVal
    VectorUtils.DataArrayVector references();

    /**
     * Get all referenced data arrays associated with this tag.
     * <p>
     * Always uses filter that accepts all sources.
     *
     * @return The filtered dimensions as a vector.
     * @see DataArray
     */
    public List<DataArray> getReferences() {
        return references().getDataArrays();
    }

    /**
     * Get referenced data arrays associated with this tag.
     * <p>
     * The parameter filter can be used to filter data arrays by various
     * criteria.
     *
     * @param filter A filter function.
     * @return A list containing the matching data arrays.
     * @see DataArray
     */
    public List<DataArray> getReferences(Predicate<DataArray> filter) {
        List<DataArray> result = new ArrayList<>();
        for (DataArray dataArray : getReferences()) {
            if (filter.test(dataArray)) {
                result.add(dataArray);
            }
        }
        return result;
    }

    private native void references(@Const @ByRef VectorUtils.DataArrayVector references);

    /**
     * Sets all referenced DataArray entities.
     * <p>
     * Previously referenced data arrays, that are not in the references vector
     * will be removed.
     *
     * @param references All referenced arrays.
     * @see DataArray
     */
    public void setReferences(List<DataArray> references) {
        references(new VectorUtils.DataArrayVector(references));
    }

    //--------------------------------------------------
    // Methods concerning features.
    //--------------------------------------------------

    /**
     * Checks if a specific feature exists on the tag.
     *
     * @param id The id of a feature.
     * @return True if the feature exists, false otherwise.
     * @see Feature
     */
    public native
    @Cast("bool")
    boolean hasFeature(@StdString String id);

    /**
     * Checks if a specific feature exists on the tag.
     *
     * @param feature The Feature to check.
     * @return True if the feature exists, false otherwise.
     * @see Feature
     */
    public native
    @Cast("bool")
    boolean hasFeature(@Const @ByRef Feature feature);

    /**
     * Gets the number of features in this block.
     *
     * @return The number of features.
     * @see Feature
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
     * @param id The id of the feature.
     * @return The feature with the specified id. If it doesn't exist
     * an exception will be thrown.
     * @see Feature
     */
    public Feature getFeature(String id) {
        Feature feature = fetchFeature(id);
        if (feature.isNone()) {
            feature = null;
        }
        return feature;
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
     * @see Feature
     */
    public Feature getFeature(long index) {
        Feature feature = fetchFeature(index);
        if (feature.isNone()) {
            feature = null;
        }
        return feature;
    }

    private native
    @ByVal
    VectorUtils.FeatureVector features();

    /**
     * Get all Features of this tag.
     *
     * @return A vector containing the matching features.
     * @see Feature
     */
    public List<Feature> getFeatures() {
        return features().getFeatures();
    }

    /**
     * Get all Features of this tag.
     * <p>
     * The parameter filter can be used to filter features by various
     * criteria.
     *
     * @param filter A filter function.
     * @return A list containing the matching features.
     * @see Feature
     */
    public List<Feature> getFeatures(Predicate<Feature> filter) {
        List<Feature> result = new ArrayList<>();
        for (Feature feature : getFeatures()) {
            if (filter.test(feature)) {
                result.add(feature);
            }
        }
        return result;
    }

    private native
    @Name("createFeature")
    @ByVal
    Feature makeFeature(@Const @ByRef DataArray data, @Cast("nix::LinkType") int linkType);

    /**
     * Create a new feature.
     *
     * @param data     The data array of this feature.
     * @param linkType The link type of this feature.
     * @return The created feature object.
     * @see Feature
     */
    public Feature createFeature(DataArray data, int linkType) {
        Feature feature = makeFeature(data, linkType);
        if (feature.isNone()) {
            feature = null;
        }
        return feature;
    }

    private native
    @Name("createFeature")
    @ByVal
    Feature makeFeature(@StdString String dataArrayId, @Cast("nix::LinkType") int linkType);

    /**
     * Create a new feature.
     *
     * @param dataArrayId The id of the data array of this feature.
     * @param linkType    The link type of this feature.
     * @return The created feature object.
     * @see Feature
     */
    public Feature createFeature(String dataArrayId, int linkType) {
        Feature feature = makeFeature(dataArrayId, linkType);
        if (feature.isNone()) {
            feature = null;
        }
        return feature;
    }

    /**
     * Deletes a feature from the tag.
     *
     * @param id The id of the feature to remove.
     * @return True if the feature was removed, false otherwise.
     * @see Feature
     */
    public native
    @Cast("bool")
    boolean deleteFeature(@StdString String id);

    /**
     * Deletes a feature from the tag.
     *
     * @param feature The feature to remove.
     * @return True if the feature was removed, false otherwise.
     * @see Feature
     */
    public native
    @Cast("bool")
    boolean deleteFeature(@Const @ByRef Feature feature);

    //--------------------------------------------------
    // Methods for data retrieval
    //--------------------------------------------------

    /**
     * Returns the data associated with a certain reference.
     *
     * @param referenceIndex The index of the reference of which
     *                       the data should be returned.
     * @return the data
     * @see DataView
     */
    public native
    @ByVal
    DataView taggedData(@Cast("size_t") long referenceIndex);

    /**
     * Returns the data stored in the selected Feature.
     *
     * @param featureIndex The index of the requested feature.
     * @return The data stored in the Feature.
     * @see DataView
     */
    public native
    @ByVal
    DataView retrieveFeatureData(@Cast("size_t") long featureIndex);

    //--------------------------------------------------
    // Overrides
    //--------------------------------------------------

    @Override
    public String toString() {
        return "Tag: {name = " + this.getName()
                + ", type = " + this.getType()
                + ", id = " + this.getId() + "}";
    }
}

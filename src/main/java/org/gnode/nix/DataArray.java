package org.gnode.nix;

import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.annotation.*;
import org.gnode.nix.base.EntityWithSources;
import org.gnode.nix.internal.*;

import java.util.Date;
import java.util.List;

@Platform(value = "linux",
        include = {"<nix/DataArray.hpp>"},
        link = {"nix"})
@Namespace("nix")
public class DataArray extends EntityWithSources {
    static {
        Loader.load();
    }

    //--------------------------------------------------
    // Constructors
    //--------------------------------------------------

    /**
     * Constructor that creates an uninitialized DataArray.
     * <p/>
     * Calling any method on an uninitialized data array will throw a {@link java.lang.RuntimeException}
     * exception. The following code illustrates how to check if a data array is initialized:
     */
    public DataArray() {
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
     * Get id of the data array
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
     * Get the creation date of the data array.
     *
     * @return The creation date of the data array.
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
     * Setter for the type of the data array
     *
     * @param type The type of the data array
     */
    public native
    @Name("type")
    void setType(@StdString String type);

    /**
     * Getter for the type of the data array
     *
     * @return The type of the data array
     */
    public native
    @Name("type")
    @StdString
    String getType();

    /**
     * Getter for the name of the data array.
     *
     * @return The name of the data array.
     */
    public native
    @Name("name")
    @StdString
    String getName();

    private native void definition(@Const @ByVal None t);

    private native void definition(@StdString String definition);

    /**
     * Setter for the definition of the data array. If null is passed definition is removed.
     *
     * @param definition definition of data array
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
     * Getter for the definition of the data array.
     *
     * @return The definition of the data array.
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

    private native void sources(@Const @ByVal VectorUtils.SourceVector sources);

    /**
     * Set all sources associations for this entity.
     * <p/>
     * All previously existing associations will be overwritten.
     *
     * @param sources A vector with all sources.
     */
    public void setSources(List<Source> sources) {
        sources(new VectorUtils.SourceVector(sources));
    }

    /**
     * Associate a new source with the entity.
     * <p/>
     * If a source with the given id already is associated with the
     * entity, the call will have no effect.
     *
     * @param id The id of the source.
     */
    public native void addSource(@StdString String id);

    /**
     * Associate a new source with the entity.
     * <p/>
     * Calling this method will have no effect if the source is already associated to this entity.
     *
     * @param source The source to add.
     */
    public native void addSource(@Const @ByRef Source source);

    /**
     * Remove a source from the list of associated sources.
     * <p/>
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
     * <p/>
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
    // Element getters and setters
    //--------------------------------------------------

    private native
    @ByVal
    OptionalString label();

    /**
     * Get the label for the values stored in the DataArray.
     *
     * @return The label of the data array. {#link null} if not present.
     */
    public String getLabel() {
        OptionalString label = label();
        if (label.isPresent()) {
            return label.getString();
        }
        return null;
    }

    private native void label(@StdString String label);

    private native void label(@Const @ByVal None t);

    /**
     * Set the label for the data stored. If {#link null} is passed the label is removed.
     *
     * @param label The label of the data array.
     */
    public void setLabel(String label) {
        if (label != null) {
            label(label);
        } else {
            label(new None());
        }
    }

    private native
    @ByVal
    OptionalString unit();

    /**
     * Get the unit of the data stored in this data array.
     *
     * @return The unit of the data array. {#link null} if not present.
     */
    public String getUnit() {
        OptionalString unit = unit();
        if (unit.isPresent()) {
            return unit.getString();
        }
        return null;
    }

    private native void unit(@StdString String unit);

    private native void unit(@Const @ByVal None t);

    /**
     * Set the unit for the values stored in this DataArray. If {#link null} is passed the unit is removed.
     *
     * @param unit The unit of the data array.
     */
    public void setUnit(String unit) {
        if (unit != null) {
            unit(unit);
        } else {
            unit(new None());
        }
    }

    private native
    @ByVal
    OptionalDouble expansionOrigin();

    /**
     * Returns the expansion origin of the calibration polynom.
     * <p/>
     * The expansion origin is 0.0 by default.
     *
     * @return The expansion origin.
     */
    public double getExpansionOrigin() {
        OptionalDouble expansionOrigin = expansionOrigin();
        if (expansionOrigin.isPresent()) {
            return expansionOrigin.getDouble();
        }
        return 0.0;
    }

    /**
     * Set the expansion origin for the calibration.
     *
     * @param expansionOrigin The expansion origin for the calibration.
     */
    public native
    @Name("expansionOrigin")
    void setExpansionOrigin(double expansionOrigin);

    private native void polynomCoefficients(@StdVector double[] polynomCoefficients);

    private native void polynomCoefficients(@Const @ByVal None t);

    /**
     * Set the polynom coefficients for the calibration.
     * <p/>
     * By default this is set to a two element vector of [0.0, 1.0] for a linear calibration
     * with zero offset.
     *
     * @param polynomCoefficients The new polynom coefficients for the calibration.
     *                            If {@link null}, deletes for the `polynomCoefficients` attribute.
     */
    public void setPolynomCoefficients(double[] polynomCoefficients) {
        if (polynomCoefficients != null) {
            polynomCoefficients(polynomCoefficients);
        } else {
            polynomCoefficients(new None());
        }
    }

    private native
    @StdVector
    DoublePointer polynomCoefficients();

    /**
     * Returns the polynom coefficients.
     *
     * @return The polynom coefficients for the calibration.
     */
    public List<Double> getPolynomCoefficients() {
        return VectorUtils.convertPointerToList(polynomCoefficients());
    }


    //--------------------------------------------------
    // Methods concerning dimensions
    //--------------------------------------------------

    private native
    @ByVal
    VectorUtils.DimensionVector dimensions();

    /**
     * Get all dimensions associated with this data array.
     *
     * @return The dimensions as a list
     */
    public List<Dimension> getDimensions() {
        return dimensions().getDimensions();
    }

    /**
     * Returns the number of dimensions stored in the DataArray.
     * <p/>
     * This matches the dimensionality of the data stored in this property.
     *
     * @return The number of dimensions.
     */
    public native
    @Name("dimensionCount")
    @Cast("size_t")
    long getDimensionCount();

    /**
     * Returns the Dimension object for the specified dimension of the data.
     *
     * @param id The index of the respective dimension.
     * @return The dimension object.
     */
    public native
    @ByVal
    Dimension getDimension(@Cast("size_t") long id);

    /**
     * Append a new SetDimension to the list of existing dimension descriptors.
     *
     * @return The newly created SetDimension.
     */
    public native
    @ByVal
    SetDimension appendSetDimension();

    /**
     * Append a new RangeDimension to the list of existing dimension descriptors.
     *
     * @param ticks The ticks of the RangeDimension to create.
     * @return The newly created RangeDimension
     */
    public native
    @ByVal
    RangeDimension appendRangeDimension(@StdVector double[] ticks);

    /**
     * Append a new SampledDimension to the list of existing dimension descriptors.
     *
     * @param samplingInterval The sampling interval of the SetDimension to create.
     * @return The newly created SampledDimension.
     */
    public native
    @ByVal
    SampledDimension appendSampledDimension(double samplingInterval);

    /**
     * Create a new SetDimension at a specified dimension index.
     * <p/>
     * This adds a new dimension descriptor of the type {@link SetDimension} that describes the dimension
     * of the data at the specified index.
     *
     * @param id The index of the dimension. Must be a value > 0 and <= `dimensionCount + 1`.
     * @return The created dimension descriptor.
     */
    public native
    @ByVal
    SetDimension createSetDimension(@Cast("size_t") long id);

    /**
     * Create a new RangeDimension at a specified dimension index.
     * <p/>
     * This adds a new dimension descriptor of the type {@link RangeDimension} that describes the dimension
     * of the data at the specified index.
     *
     * @param id    The index of the dimension. Must be a value > 0 and <= `dimensionCount + 1`.
     * @param ticks Vector with {@link RangeDimension#ticks}.
     * @return The created dimension descriptor.
     */
    public native
    @ByVal
    RangeDimension createRangeDimension(@Cast("size_t") long id, @StdVector double[] ticks);

    /**
     * Create a new SampledDimension at a specified dimension index.
     * <p/>
     * This adds a new dimension descriptor of the type {@link SampledDimension} that describes the dimension
     * of the data at the specified index.
     *
     * @param id               The index of the dimension. Must be a value > 0 and <= `dimensionCount + 1`.
     * @param samplingInterval The sampling interval of the dimension.
     * @return The created dimension descriptor.
     */
    public native
    @ByVal
    SampledDimension createSampledDimension(@Cast("size_t") long id, double samplingInterval);

    /**
     * Remove a dimension descriptor at a specified index.
     *
     * @param id The index of the dimension. Must be a value > 0 and < `getDimensionCount + 1`.
     */
    public native
    @Cast("bool")
    boolean deleteDimension(@Cast("size_t") long id);

    //--------------------------------------------------
    // Methods concerning data access.
    //--------------------------------------------------

    /**
     * Get the extent of the data of the DataArray entity.
     *
     * @return The data extent.
     */
    public native
    @Name("dataExtent")
    @ByVal
    NDSize getDataExtent();

    /**
     * Set the data extent of the DataArray entity.
     *
     * @param extent The extent of the data.
     */
    public native
    @Name("dataExtent")
    void setDataExtent(@Const @ByRef NDSize extent);

    /**
     * Get the data type of the data stored in the DataArray entity.
     *
     * @return The data type of the DataArray.
     */
    public native
    @Name("dataType")
    @ByVal
    @Cast("nix::DataType")
    int getDataType();

    //--------------------------------------------------
    // Overrides
    //--------------------------------------------------

    @Override
    public String toString() {
        return "DataArray: {name = " + this.getName()
                + ", type = " + this.getType()
                + ", id = " + this.getId() + "}";
    }
}
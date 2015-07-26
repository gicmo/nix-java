package org.gnode.nix;

import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.annotation.*;
import org.gnode.nix.base.EntityWithSources;
import org.gnode.nix.internal.DateUtils;
import org.gnode.nix.internal.None;
import org.gnode.nix.internal.OptionalUtils;
import org.gnode.nix.internal.VectorUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

/**
 * <h1>DataArray</h1>
 * A class that can store arbitrary n-dimensional data along with further
 * information.
 * <p>
 * The {@link DataArray} is the core entity of the NIX data model, its purpose is to
 * store arbitrary n-dimensional data. In addition to the common fields id, name, type, and definition
 * the DataArray stores sufficient information to understand the physical nature of the stored data.
 * <p>
 * A guiding principle of the data model is provides enough information to create a
 * plot of the stored data. In order to do so, the DataArray defines a property
 * {@link DataType} which provides the physical type of the stored data (for example
 * 16 bit integer or double precision IEEE floatingpoint number).
 * The property {@link DataArray#setUnit(String)} specifies the SI unit of the values stored in the
 * DataArray{} whereas the {@link DataArray#setLabel(String)} defines what is given in this units.
 * Together, both specify what corresponds to the the y-axis of a plot.
 * <p>
 * In some cases it is much more efficient or convenient to store data not as
 * floating point numbers but rather as (16 bit) integer values as, for example
 * read from a data acquisition board.
 * In order to convert such data to the correct values, we follow the approach
 * taken by the comedi data-acquisition library (http://www.comedi.org)
 * and provide {@link DataArray#setPolynomCoefficients(double[])} and an {@link DataArray#setExpansionOrigin(double)}.
 * <p>
 * <h2>Create a new data array</h2>
 * A DataArray can only be created at an existing block. Do not use the
 * DataArrays constructors for this purpose.
 * <pre><code>
 *     Block b = ...;
 *     DataArray da = b.createDataArray("array_one", "testdata", DataType.Double, new NDSize(new int[] { 3, 4, 2 }));
 * </code></pre>
 * <p>
 * <h2>Remove a data array from a file</h2>
 * Deleting a DataArray.
 * <pre><code>
 *     Block b = ...;
 *     boolean deleted = b.deleteDataArray(some_id);
 * </code></pre>
 *
 * @see Block
 * @see DataType
 */

@Properties(value = {
        @Platform(include = {"<nix/DataArray.hpp>"}, link = "nix"),
        @Platform(value = "linux"),
        @Platform(value = "windows")})
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
     * <p>
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
     * Get id of the data array.
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
     * Get the creation date of the data array.
     *
     * @return The creation date of the data array.
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
     * Setter for the type of the data array.
     *
     * @param type The type of the data array.
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
     * Setter for the definition of the data array. If <tt>null</tt> is passed definition is removed.
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
    OptionalUtils.OptionalString definition();

    /**
     * Getter for the definition of the data array.
     *
     * @return The definition of the data array.
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
     * @see Source
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
     * @return All associated sources that match the given filter as a list.
     * @see Source
     */
    public List<Source> getSources() {
        return sources().getSources();
    }

    private native void sources(@Const @ByVal VectorUtils.SourceVector sources);

    /**
     * Set all sources associations for this entity.
     * <p>
     * All previously existing associations will be overwritten.
     *
     * @param sources A list with all sources.
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
    OptionalUtils.OptionalString label();

    /**
     * Get the label for the values stored in the DataArray.
     *
     * @return The label of the data array. Returns <tt>null</tt> if not present.
     */
    public String getLabel() {
        OptionalUtils.OptionalString label = label();
        if (label.isPresent()) {
            return label.getString();
        }
        return null;
    }

    private native void label(@StdString String label);

    private native void label(@Const @ByVal None t);

    /**
     * Set the label for the data stored. If <tt>null</tt> is passed the label is removed.
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
    OptionalUtils.OptionalString unit();

    /**
     * Get the unit of the data stored in this data array.
     *
     * @return The unit of the data array. <tt>null</tt> if not present.
     */
    public String getUnit() {
        OptionalUtils.OptionalString unit = unit();
        if (unit.isPresent()) {
            return unit.getString();
        }
        return null;
    }

    private native void unit(@StdString String unit);

    private native void unit(@Const @ByVal None t);

    /**
     * Set the unit for the values stored in this DataArray. If <tt>null</tt> is passed the unit is removed.
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
    OptionalUtils.OptionalDouble expansionOrigin();

    /**
     * Returns the expansion origin of the calibration polynom.
     * <p>
     * The expansion origin is 0.0 by default.
     *
     * @return The expansion origin.
     */
    public double getExpansionOrigin() {
        OptionalUtils.OptionalDouble expansionOrigin = expansionOrigin();
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
     * <p>
     * By default this is set to a two element array of [0.0, 1.0] for a linear calibration
     * with zero offset.
     *
     * @param polynomCoefficients The new polynom coefficients for the calibration.
     *                            If <tt>null</tt>, deletes for the `polynomCoefficients` attribute.
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
    public double[] getPolynomCoefficients() {
        return VectorUtils.convertPointerToArray(polynomCoefficients());
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
     * @see Dimension
     */
    public List<Dimension> getDimensions() {
        return dimensions().getDimensions();
    }

    /**
     * Get all dimensions associated with this data array.
     * <p>
     * The parameter filter can be used to filter sources by various
     * criteria.
     *
     * @param filter A filter function.
     * @return The filtered dimensions as a list
     * @see Dimension
     */
    public List<Dimension> getDimensions(Predicate<Dimension> filter) {
        List<Dimension> result = new ArrayList<>();
        for (Dimension dimension : getDimensions()) {
            if (filter.test(dimension)) {
                result.add(dimension);
            }
        }
        return result;
    }

    /**
     * Returns the number of dimensions stored in the DataArray.
     * <p>
     * This matches the dimensionality of the data stored in this property.
     *
     * @return The number of dimensions.
     * @see Dimension
     */
    public native
    @Name("dimensionCount")
    @Cast("size_t")
    long getDimensionCount();

    private native
    @Name("getDimension")
    @ByVal
    Dimension fetchDimension(@Cast("size_t") long id);

    /**
     * Returns the Dimension object for the specified dimension of the data.
     *
     * @param id The index of the respective dimension.
     * @return The dimension object.
     * @see Dimension
     */
    public Dimension getDimension(long id) {
        Dimension dimension = fetchDimension(id);
        if (dimension.isInitialized()) {
            return dimension;
        }
        return null;
    }

    /**
     * Append a new SetDimension to the list of existing dimension descriptors.
     *
     * @return The newly created SetDimension.
     * @see SetDimension
     */
    public native
    @ByVal
    SetDimension appendSetDimension();

    /**
     * Append a new RangeDimension to the list of existing dimension descriptors.
     *
     * @param ticks The ticks of the RangeDimension to create.
     * @return The newly created RangeDimension
     * @see RangeDimension
     */
    public native
    @ByVal
    RangeDimension appendRangeDimension(@StdVector double[] ticks);

    /**
     * Append a new RangeDimension that uses the data stored in this DataArray as ticks.
     * This works only(!) if the DataArray in 1D and the stored data is numeric.
     *
     * @return The created RangeDimension
     * @see RangeDimension
     */
    public native
    @ByVal
    RangeDimension appendAliasRangeDimension();

    /**
     * Append a new SampledDimension to the list of existing dimension descriptors.
     *
     * @param samplingInterval The sampling interval of the SetDimension to create.
     * @return The newly created SampledDimension.
     * @see SampledDimension
     */
    public native
    @ByVal
    SampledDimension appendSampledDimension(double samplingInterval);

    /**
     * Create a new SetDimension at a specified dimension index.
     * <p>
     * This adds a new dimension descriptor of the type {@link SetDimension} that describes the dimension
     * of the data at the specified index.
     *
     * @param id The index of the dimension. Must be a value > 0 and <= `dimensionCount + 1`.
     * @return The created dimension descriptor.
     * @see SetDimension
     */
    public native
    @ByVal
    SetDimension createSetDimension(@Cast("size_t") long id);

    /**
     * Create a new RangeDimension at a specified dimension index.
     * <p>
     * This adds a new dimension descriptor of the type {@link RangeDimension} that describes the dimension
     * of the data at the specified index.
     *
     * @param id    The index of the dimension. Must be a value > 0 and <= `dimensionCount + 1`.
     * @param ticks Array with {@link RangeDimension#setTicks(double[])}.
     * @return The created dimension descriptor.
     * @see RangeDimension
     */
    public native
    @ByVal
    RangeDimension createRangeDimension(@Cast("size_t") long id, @StdVector double[] ticks);

    /**
     * Create a new RangeDimension that uses the data stored in this DataArray as ticks.
     *
     * @return The created dimension descriptor.
     * @see RangeDimension
     */
    public native
    @ByVal
    RangeDimension createAliasRangeDimension();

    /**
     * Create a new SampledDimension at a specified dimension index.
     * <p>
     * This adds a new dimension descriptor of the type {@link SampledDimension} that describes the dimension
     * of the data at the specified index.
     *
     * @param id               The index of the dimension. Must be a value > 0 and <= `dimensionCount + 1`.
     * @param samplingInterval The sampling interval of the dimension.
     * @return The created dimension descriptor.
     * @see SampledDimension
     */
    public native
    @ByVal
    SampledDimension createSampledDimension(@Cast("size_t") long id, double samplingInterval);

    /**
     * Remove a dimension descriptor at a specified index.
     *
     * @param id The index of the dimension. Must be a value > 0 and < `getDimensionCount + 1`.
     * @see Dimension
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
     * @see NDSize
     */
    public native
    @Name("dataExtent")
    @ByVal
    NDSize getDataExtent();

    /**
     * Set the data extent of the DataArray entity.
     *
     * @param extent The extent of the data.
     * @see NDSize
     */
    public native
    @Name("dataExtent")
    void setDataExtent(@Const @ByRef NDSize extent);

    /**
     * Get the data type of the data stored in the DataArray entity.
     *
     * @return The data type of the DataArray.
     * @see DataType
     */
    public native
    @Name("dataType")
    @ByVal
    @Cast("nix::DataType")
    int getDataType();

    private native void getDataDirect(@Cast("nix::DataType") int dtype,
                                      @Cast("void*") byte[] data,
                                      @Const @ByRef NDSize count,
                                      @Const @ByRef NDSize offset);

    private native void getDataDirect(@Cast("nix::DataType") int dtype,
                                      @Cast("void*") short[] data,
                                      @Const @ByRef NDSize count,
                                      @Const @ByRef NDSize offset);

    private native void getDataDirect(@Cast("nix::DataType") int dtype,
                                      @Cast("void*") int[] data,
                                      @Const @ByRef NDSize count,
                                      @Const @ByRef NDSize offset);

    private native void getDataDirect(@Cast("nix::DataType") int dtype,
                                      @Cast("void*") long[] data,
                                      @Const @ByRef NDSize count,
                                      @Const @ByRef NDSize offset);

    private native void getDataDirect(@Cast("nix::DataType") int dtype,
                                      @Cast("void*") float[] data,
                                      @Const @ByRef NDSize count,
                                      @Const @ByRef NDSize offset);

    private native void getDataDirect(@Cast("nix::DataType") int dtype,
                                      @Cast("void*") double[] data,
                                      @Const @ByRef NDSize count,
                                      @Const @ByRef NDSize offset);

    private native void setDataDirect(@Cast("nix::DataType") int dtype,
                                      @Cast("const void*") byte[] data,
                                      @Const @ByRef NDSize count,
                                      @Const @ByRef NDSize offset);

    private native void setDataDirect(@Cast("nix::DataType") int dtype,
                                      @Cast("const void*") short[] data,
                                      @Const @ByRef NDSize count,
                                      @Const @ByRef NDSize offset);

    private native void setDataDirect(@Cast("nix::DataType") int dtype,
                                      @Cast("const void*") int[] data,
                                      @Const @ByRef NDSize count,
                                      @Const @ByRef NDSize offset);

    private native void setDataDirect(@Cast("nix::DataType") int dtype,
                                      @Cast("const void*") long[] data,
                                      @Const @ByRef NDSize count,
                                      @Const @ByRef NDSize offset);

    private native void setDataDirect(@Cast("nix::DataType") int dtype,
                                      @Cast("const void*") float[] data,
                                      @Const @ByRef NDSize count,
                                      @Const @ByRef NDSize offset);

    private native void setDataDirect(@Cast("nix::DataType") int dtype,
                                      @Cast("const void*") double[] data,
                                      @Const @ByRef NDSize count,
                                      @Const @ByRef NDSize offset);

    //--------------------------------------------------
    // public getData methods
    //--------------------------------------------------

    /**
     * Get stored data. Data is stored in the array passed.
     *
     * @param data   byte array
     * @param count  dimensions
     * @param offset offset
     */
    public void getData(byte[] data, NDSize count, NDSize offset) {
        getDataDirect(DataType.Int8, data, count, offset);
    }

    /**
     * Get stored data. Data is stored in the array passed.
     *
     * @param data   short array
     * @param count  dimensions
     * @param offset offset
     */
    public void getData(short[] data, NDSize count, NDSize offset) {
        getDataDirect(DataType.Int16, data, count, offset);
    }

    /**
     * Get stored data. Data is stored in the array passed.
     *
     * @param data   int array
     * @param count  dimensions
     * @param offset offset
     */
    public void getData(int[] data, NDSize count, NDSize offset) {
        getDataDirect(DataType.Int32, data, count, offset);
    }

    /**
     * Get stored data. Data is stored in the array passed.
     *
     * @param data   long array
     * @param count  dimensions
     * @param offset offset
     */
    public void getData(long[] data, NDSize count, NDSize offset) {
        getDataDirect(DataType.Int64, data, count, offset);
    }

    /**
     * Get stored data. Data is stored in the array passed.
     *
     * @param data   float array
     * @param count  dimensions
     * @param offset offset
     */
    public void getData(float[] data, NDSize count, NDSize offset) {
        getDataDirect(DataType.Float, data, count, offset);
    }

    /**
     * Get stored data. Data is stored in the array passed.
     *
     * @param data   double array
     * @param count  dimensions
     * @param offset offset
     */
    public void getData(double[] data, NDSize count, NDSize offset) {
        getDataDirect(DataType.Double, data, count, offset);
    }

    //--------------------------------------------------
    // public setData methods
    //--------------------------------------------------

    /**
     * Set byte array data.
     *
     * @param data   data
     * @param count  dimensions
     * @param offset offset
     */
    public void setData(byte[] data, NDSize count, NDSize offset) {
        setDataDirect(DataType.Int8, data, count, offset);
    }

    /**
     * Set short array data.
     *
     * @param data   data
     * @param count  dimensions
     * @param offset offset
     */
    public void setData(short[] data, NDSize count, NDSize offset) {
        setDataDirect(DataType.Int16, data, count, offset);
    }

    /**
     * Set integer array data.
     *
     * @param data   data
     * @param count  dimensions
     * @param offset offset
     */
    public void setData(int[] data, NDSize count, NDSize offset) {
        setDataDirect(DataType.Int32, data, count, offset);
    }

    /**
     * Set long array data.
     *
     * @param data   data
     * @param count  dimensions
     * @param offset offset
     */
    public void setData(long[] data, NDSize count, NDSize offset) {
        setDataDirect(DataType.Int64, data, count, offset);
    }

    /**
     * Set float array data.
     *
     * @param data   data
     * @param count  dimensions
     * @param offset offset
     */
    public void setData(float[] data, NDSize count, NDSize offset) {
        setDataDirect(DataType.Float, data, count, offset);
    }

    /**
     * Set double array data.
     *
     * @param data   data
     * @param count  dimensions
     * @param offset offset
     */
    public void setData(double[] data, NDSize count, NDSize offset) {
        setDataDirect(DataType.Double, data, count, offset);
    }

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
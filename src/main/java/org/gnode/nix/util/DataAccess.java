package org.gnode.nix.util;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.annotation.*;
import org.gnode.nix.*;

/**
 * <h1>DataAccess</h1>
 * This class contains utility functions.
 */

@Properties(value = {
        @Platform(include = {"<nix/util/dataAccess.hpp>",
                "<nix/Block.hpp>",
                "<nix/DataArray.hpp>",
                "<nix/Dimensions.hpp>",
                "<nix/Feature.hpp>",
                "<nix/File.hpp>",
                "<nix/MultiTag.hpp>",
                "<nix/Property.hpp>",
                "<nix/Section.hpp>",
                "<nix/Source.hpp>",
                "<nix/Tag.hpp>"}, link = "nix", preload = "hdf5"),
        @Platform(value = "linux"),
        @Platform(value = "windows")})
@Namespace("nix::util")
public class DataAccess {
    static {
        Loader.load();
    }

    /**
     * Converts a position given in a unit into an index according to the dimension descriptor.
     * <p>
     * This function can be used to get the index of e.g. a certain point in time in a Dimension that
     * represents time. The units of the position and that provided by the Dimension must match, i.e.
     * must be scalable versions of the same SI unit.
     *
     * @param position  The position
     * @param unit      The unit in which the position is given, may be "none"
     * @param dimension The dimension descriptor for the respective dimension.
     * @return The calculated index.
     * @see SetDimension
     */
    public static native long positionToIndex(double position, @StdString String unit, @Const @ByRef SetDimension dimension);

    /**
     * Converts a position given in a unit into an index according to the dimension descriptor.
     * <p>
     * This function can be used to get the index of e.g. a certain point in time in a Dimension that
     * represents time. The units of the position and that provided by the Dimension must match, i.e.
     * must be scalable versions of the same SI unit.
     *
     * @param position  The position
     * @param unit      The unit in which the position is given, may be "none"
     * @param dimension The dimension descriptor for the respective dimension.
     * @return The calculated index.
     * @see SampledDimension
     */
    public static native long positionToIndex(double position, @StdString String unit, @Const @ByRef SampledDimension dimension);

    /**
     * Converts a position given in a unit into an index according to the dimension descriptor.
     * <p>
     * This function can be used to get the index of e.g. a certain point in time in a Dimension that
     * represents time. The units of the position and that provided by the Dimension must match, i.e.
     * must be scalable versions of the same SI unit.
     *
     * @param position  The position
     * @param unit      The unit in which the position is given, may be "none"
     * @param dimension The dimension descriptor for the respective dimension.
     * @return The calculated index.
     * @see RangeDimension
     */
    public static native long positionToIndex(double position, @StdString String unit, @Const @ByRef RangeDimension dimension);


    /**
     * Returns the offsets and element counts associated with position and extent of a Tag and
     * the referenced DataArray.
     *
     * @param tag     The tag.
     * @param array   A referenced data array.
     * @param offsets The resulting offset.
     * @param counts  The number of elements to read from data
     * @see DataArray
     * @see NDSize
     * @see Tag
     */
    public static native void getOffsetAndCount(@Const @ByRef Tag tag, @Const @ByRef DataArray array, @ByRef NDSize offsets, @ByRef NDSize counts);

    /**
     * Returns the offsets and element counts associated with position and extent of a Tag and
     * the referenced DataArray.
     *
     * @param tag     The tag.
     * @param array   A referenced data array.
     * @param index   index.
     * @param offsets The resulting offset.
     * @param counts  The number of elements to read from data
     * @see DataArray
     * @see MultiTag
     * @see NDSize
     */
    public static native void getOffsetAndCount(@Const @ByRef MultiTag tag, @Const @ByRef DataArray array, @Cast("size_t") long index, @ByRef NDSize offsets, @ByRef NDSize counts);

    /**
     * Retrieve the data referenced by the given position and extent of the MultiTag.
     *
     * @param tag            The multi tag.
     * @param positionIndex  The index of the position.
     * @param referenceIndex The index of the reference from which data should be returned.
     * @return The data referenced by position and extent.
     * @see DataView
     * @see MultiTag
     */
    public static native
    @ByVal
    DataView retrieveData(@Const @ByRef MultiTag tag, @Cast("size_t") long positionIndex, @Cast("size_t") long referenceIndex);

    /**
     * Retrieve the data referenced by the given position and extent of the Tag.
     *
     * @param tag            The multi tag.
     * @param referenceIndex The index of the reference from which data should be returned.
     * @return The data referenced by the position.
     * @see DataView
     * @see Tag
     */
    public static native
    @ByVal
    DataView retrieveData(@Const @ByRef Tag tag, @Cast("size_t") long referenceIndex);

    /**
     * Checks whether a given position is in the extent of the given DataArray.
     *
     * @param data     The data array.
     * @param position The position.
     * @return True if the position is in the extent of the data array, false otherwise.
     * @see DataArray
     * @see NDSize
     */
    public static native boolean positionInData(@Const @ByRef DataArray data, @Const @ByRef NDSize position);

    /**
     * Checks whether a given position plus count is in the extent of the given DataArray.
     *
     * @param data     The DataArray.
     * @param position The position
     * @param count    The number of elements per dimension.
     * @return True if position and count are in the extent of the data array, false otherwise.
     * @see DataArray
     * @see NDSize
     */
    public static native boolean positionAndExtentInData(@Const @ByRef DataArray data, @Const @ByRef NDSize position, @Const @ByRef NDSize count);

    /**
     * Retruns the feature data associated with a Tag.
     *
     * @param tag          The Tag whos feature data is requested
     * @param featureIndex The index of the desired feature. Default is 0.
     * @return The associated data.
     * @see DataView
     * @see Tag
     */
    public static native
    @ByVal
    DataView retrieveFeatureData(@Const @ByRef Tag tag, @Cast("size_t") long featureIndex);

    /**
     * Retruns the feature data associated with a Tag. Feature Index defaulted to 0.
     *
     * @param tag The Tag whos feature data is requested
     * @return The associated data.
     * @see DataView
     * @see Tag
     */
    public static native
    @ByVal
    DataView retrieveFeatureData(@Const @ByRef Tag tag);

    /**
     * Returns the feature data accosiated with the given MuliTag's position.
     *
     * @param tag           The MultiTag whos feature data is requested.
     * @param positionIndex The index of the selected position, respectively the selected tag of the MultiTag.
     * @param featureIndex  The index of the desired feature.
     * @return The associated data.
     * @see DataView
     * @see MultiTag
     */
    public static native
    @ByVal
    DataView retrieveFeatureData(@Const @ByRef MultiTag tag, @Cast("size_t") long positionIndex, @Cast("size_t") long featureIndex);

    /**
     * Returns the feature data accosiated with the given MuliTag's position. Feature Index defaulted to 0.
     *
     * @param tag           The MultiTag whos feature data is requested.
     * @param positionIndex The index of the selected position, respectively the selected tag of the MultiTag.
     * @return The associated data.
     * @see DataView
     * @see MultiTag
     */
    public static native
    @ByVal
    DataView retrieveFeatureData(@Const @ByRef MultiTag tag, @Cast("size_t") long positionIndex);
}

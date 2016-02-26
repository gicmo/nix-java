package org.g_node.nix;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.annotation.*;
import org.g_node.nix.internal.DateUtils;
import org.g_node.nix.base.Entity;
import org.g_node.nix.internal.BuildLibs;

import java.util.Date;

/**
 * <h1>Feature</h1>
 * Feature entities are used to attach further data to a {@link Tag} or
 * {@link MultiTag}
 * <p>
 * A Feature entity contains a link to an existing {@link DataArray} containing additional
 * data that belongs to the respective tag. The way how data and feature are connected is specified by the
 * link type.
 * <p>
 * <h2>Tagged</h2>
 * This link type  indicates, that only a certain subset of the linked {@link DataArray}
 * belongs to the Feature. This subset is defined by the position and extent of the
 * respective tag.
 * <p>
 * <h2>Untagged</h2>
 * This implies that the whole data stored in the linked {@link DataArray} belongs to
 * the Feature.
 * <p>
 * <h2>Indexed</h2>
 * This value is only valid for multi tags where it indicates that
 * the data linked via this Feature has to be accessed according
 * to the index in the respective position entry.
 *
 * @see DataArray
 * @see MultiTag
 * @see Tag
 */

@Properties(value = {
        @Platform(include = {"<nix/Feature.hpp>"}),
        @Platform(value = "linux", link = BuildLibs.NIX_1, preload = BuildLibs.HDF5_7),
        @Platform(value = "macosx", link = BuildLibs.NIX, preload = BuildLibs.HDF5),
        @Platform(value = "windows",
                link = BuildLibs.NIX,
                preload = {BuildLibs.HDF5, BuildLibs.MSVCP120, BuildLibs.MSVCR120, BuildLibs.SZIP, BuildLibs.ZLIB})})
@Namespace("nix")
public class Feature extends Entity {
    static {
        Loader.load();
    }

    //--------------------------------------------------
    // Constructors
    //--------------------------------------------------

    /**
     * Constructor that creates an uninitialized Feature.
     * <p>
     * Calling any method on an uninitialized feature will throw a {@link RuntimeException}.
     */
    public Feature() {
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
     * Get id of the feature.
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
     * Get the creation date of the feature.
     *
     * @return The creation date of the feature.
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

    //--------------------------------------------------
    // Methods concerning Feature
    //--------------------------------------------------

    /**
     * Setter for the link type.
     *
     * @param type The link type to set.
     * @see LinkType
     */
    public native
    @Name("linkType")
    void setLinkType(@Cast("nix::LinkType") int type);

    /**
     * Getter for the link type.
     *
     * @return The current link type of the feature.
     * @see LinkType
     */
    public native
    @Name("linkType")
    @ByVal
    @Cast("nix::LinkType")
    int getLinkType();

    /**
     * Sets the data array associated with this feature.
     *
     * @param nameOrId Name or id of the data array to set.
     */
    public native
    @Name("data")
    void setData(@StdString String nameOrId);

    /**
     * Sets the data array associated with this feature.
     *
     * @param data The data array to set.
     * @see DataArray
     */
    public native
    @Name("data")
    void setData(@Const @ByRef DataArray data);

    /**
     * Gets the data array associated with this feature.
     *
     * @return The associated data array.
     * @see DataArray
     */
    public native
    @Name("data")
    @ByVal
    DataArray getData();

    /**
     * Convert {@link LinkType} to string.
     *
     * @param linkType link to convert
     * @return link type as a string
     * @see LinkType
     */
    public static String linkTypeToString(int linkType) {
        String strLinkType = "";
        switch (linkType) {
            case LinkType.Tagged:
                strLinkType = "Tagged";
                break;
            case LinkType.Untagged:
                strLinkType = "Untagged";
                break;
            case LinkType.Indexed:
                strLinkType = "Indexed";
                break;
        }
        return strLinkType;
    }


    //--------------------------------------------------
    // Overrides
    //--------------------------------------------------

    @Override
    public String toString() {
        return "Feature: {link type = " + linkTypeToString(getLinkType())
                + ", id = " + this.getId() + "}";
    }

}
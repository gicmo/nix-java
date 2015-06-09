package org.gnode.nix;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.annotation.*;
import org.gnode.nix.base.Entity;
import org.gnode.nix.internal.Utils;

import java.util.Date;

@Platform(value = "linux",
        include = {"<nix/Feature.hpp>"},
        link = {"nix"})
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
     * <p/>
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
     * Get id of the property
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
     * Get the creation date of the property.
     *
     * @return The creation date of the property.
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

    //--------------------------------------------------
    // Methods concerning Feature
    //--------------------------------------------------

    /**
     * Setter for the link type.
     *
     * @param type The link type to set.
     */
    public native
    @Name("linkType")
    void setLinkType(@Cast("nix::LinkType") int type);

    /**
     * Getter for the link type.
     *
     * @return The current link type of the feature.
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
     */
    public native
    @Name("data")
    void setData(@Const @ByRef DataArray data);

    /**
     * Gets the data array associated with this feature.
     *
     * @return The associated data array.
     */
    public native
    @Name("data")
    @ByVal
    DataArray getData();

    /**
     * Convert link type to string.
     *
     * @param linkType link to convert
     * @return link type as a string
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
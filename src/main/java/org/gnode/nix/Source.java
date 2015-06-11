package org.gnode.nix;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.annotation.*;
import org.gnode.nix.base.EntityWithMetadata;
import org.gnode.nix.internal.None;
import org.gnode.nix.internal.OptionalString;
import org.gnode.nix.internal.Utils;
import org.gnode.nix.internal.VectorUtils;

import java.util.Date;
import java.util.List;

@Platform(value = "linux",
        include = {"<nix/Source.hpp>"},
        link = {"nix"})
@Namespace("nix")
public class Source extends EntityWithMetadata {

    static {
        Loader.load();
    }

    /**
     * Constructor that creates an uninitialized Source.
     * <p/>
     * Calling any method on an uninitialized source will throw a {@link java.lang.RuntimeException}.
     */
    public Source() {
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
     * Get id of the source.
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
     * Get the creation date of the source.
     *
     * @return The creation date of the source.
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
     * Setter for the type of the source.
     *
     * @param type The type of the source
     */
    public native
    @Name("type")
    void setType(@StdString String type);

    /**
     * Getter for the type of the source
     *
     * @return The type of the source
     */
    public native
    @Name("type")
    @StdString
    String getType();

    /**
     * Getter for the name of the source.
     *
     * @return The name of the source.
     */
    public native
    @Name("name")
    @StdString
    String getName();

    private native void definition(@Const @ByVal None t);

    private native void definition(@StdString String definition);

    /**
     * Setter for the definition of the source. If null is passed definition is removed.
     *
     * @param definition definition of source
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
     * Getter for the definition of the source.
     *
     * @return The definition of the source.
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
    // Methods concerning child sources
    //--------------------------------------------------

    /**
     * Checks if this source has a specific source as direct descendant.
     *
     * @param nameOrId The name or id of the source.
     * @return True if a source with the given name/id is a direct descendant, false
     * otherwise.
     */
    public native
    @Cast("bool")
    boolean hasSource(@StdString String nameOrId);

    /**
     * Checks if this source has a specific source as direct descendant.
     *
     * @param source The Source.
     * @return True if a source is a direct descendant, false otherwise.
     */
    public native
    @Cast("bool")
    boolean hasSource(@Const @ByRef Source source);

    private native
    @Name("getSource")
    @ByVal
    Source fetchSource(@StdString String nameOrId);

    /**
     * Retrieves a specific child source that is a direct descendant.
     *
     * @param nameOrId The name or id of the source.
     * @return The source with the given name/id. If it doesn't exist an exception
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
     * Retrieves a specific source by index.
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
     * Returns the number of sources that are direct descendants of this source.
     *
     * @return The number of direct child sources.
     */
    public native
    @Name("sourceCount")
    long getSourceCount();

    private native
    @ByVal
    VectorUtils.SourceVector sources();

    /**
     * Get all direct child sources associated with this source.
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
     * Delete a root source and all its child sources from
     * the source.
     *
     * @param nameOrId The name or id of the source to remove.
     * @return True if the source was deleted, false otherwise.
     */
    public native
    @Cast("bool")
    boolean deleteSource(@StdString String nameOrId);

    /**
     * Delete a root source and all its child sources from
     * the source.
     *
     * @param source The Source to delete.
     * @return True if the source was deleted, false otherwise.
     */
    public native
    @Cast("bool")
    boolean deleteSource(@Const @ByRef Source source);

    //--------------------------------------------------
    // Overrides
    //--------------------------------------------------

    @Override
    public String toString() {
        return "Source: {name = " + this.getName()
                + ", type = " + this.getType()
                + ", id = " + this.getId() + "}";
    }
}

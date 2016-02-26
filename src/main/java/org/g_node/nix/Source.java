package org.g_node.nix;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.annotation.*;
import org.bytedeco.javacpp.annotation.Properties;
import org.g_node.nix.base.EntityWithMetadata;
import org.g_node.nix.internal.*;

import java.util.*;
import java.util.function.Predicate;

/**
 * <h1>Source</h1>
 * A class that describes the provenance of other entities of the NIX data model.
 * <p>
 * The Source is conceptually a rather simple entity. It is used to note the provenance of
 * the data and offers the opportunity to bind additional metadata.
 * One special feature of the Source is the possibility to contain other sources as children
 * thus building up a tree of sources.
 * This can, for example, be used to specify that a source electrode array contains
 * multiple electrodes as its child sources.
 */

@Properties(value = {
        @Platform(include = {"<nix/Source.hpp>"}),
        @Platform(value = "linux", link = BuildLibs.NIX_1, preload = BuildLibs.HDF5_7),
        @Platform(value = "macosx", link = BuildLibs.NIX, preload = BuildLibs.HDF5),
        @Platform(value = "windows",
                link = BuildLibs.NIX,
                preload = {BuildLibs.HDF5, BuildLibs.MSVCP120, BuildLibs.MSVCR120, BuildLibs.SZIP, BuildLibs.ZLIB})})
@Namespace("nix")
public class Source extends EntityWithMetadata {

    static {
        Loader.load();
    }

    /**
     * Constructor that creates an uninitialized Source.
     * <p>
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
     * Get the creation date of the source.
     *
     * @return The creation date of the source.
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
     * Setter for the type of the source.
     *
     * @param type The type of the source.
     */
    public native
    @Name("type")
    void setType(@StdString String type);

    /**
     * Getter for the type of the source.
     *
     * @return The type of the source.
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
     * Setter for the definition of the source. If <tt>null</tt> is passed definition is removed.
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
    OptionalUtils.OptionalString definition();

    /**
     * Getter for the definition of the source.
     *
     * @return The definition of the source.
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
     * Retrieves a specific source by index.
     *
     * @param index The index of the source.
     * @return The source at the specified index.
     */
    public Source getSource(long index) {
        Source source = fetchSource(index);
        if (source.isNone()) {
            source = null;
        }
        return source;
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

    /**
     * Get all direct child sources associated with this source.
     * <p>
     * The parameter filter can be used to filter sources by various
     * criteria.
     *
     * @param filter A filter function.
     * @return A list containing the matching child sources.
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

    private static class SourceCont {
        private final Source entity;
        private final int depth;

        public SourceCont(Source entity, int depth) {
            this.entity = entity;
            this.depth = depth;
        }
    }

    /**
     * Get all descendant sources of the source recursively.
     * <p>
     * This method traverses the sub-tree of all child sources of the source. The traversal
     * is accomplished via breadth first and can be limited in depth. On each node or
     * source a filter is applied. If the filter returns true the respective source
     * will be added to the result list.
     *
     * @param filter   A filter function.
     * @param maxDepth The maximum depth of traversal.
     * @return A list containing the matching descendant sources.
     */
    public List<Source> findSources(Predicate<Source> filter, int maxDepth) {
        List<Source> results = new ArrayList<>();
        Queue<SourceCont> todo = new LinkedList<>();
        int level = 0;

        todo.add(new SourceCont(this, level));

        while (todo.size() > 0) {
            SourceCont current = todo.remove();

            if (filter.test(current.entity)) {
                results.add(current.entity);
            }
            if (current.depth < maxDepth) {
                List<Source> children = current.entity.getSources();
                int next_depth = current.depth + 1;

                for (Source child : children) {
                    todo.add(new SourceCont(child, next_depth));
                }
            }
        }
        return results;
    }

    /**
     * Get all descendant sources of the source recursively.
     * <p>
     * This method traverses the sub-tree of all child sources of the source. The traversal
     * is accomplished via breadth first and can be limited in depth. On each node or
     * source a filter is applied. If the filter returns true the respective source
     * will be added to the result list.
     * By default nodes at all depths are considered.
     *
     * @param filter A filter function.
     * @return A list containing the matching descendant sources.
     */
    public List<Source> findSources(Predicate<Source> filter) {
        return findSources(filter, Integer.MAX_VALUE);
    }

    /**
     * Get all descendant sources of the source recursively.
     * <p>
     * This method traverses the sub-tree of all child sources of the source. The traversal
     * is accomplished via breadth first and can be limited in depth. On each node or
     * source a filter is applied. If the filter returns true the respective source
     * will be added to the result list.
     * By default a filter is used that accepts all sources.
     *
     * @param maxDepth The maximum depth of traversal.
     * @return A list containing the matching descendant sources.
     */
    public List<Source> findSources(int maxDepth) {
        return findSources((Source s) -> true, maxDepth);
    }

    /**
     * Get all descendant sources of the source recursively.
     * <p>
     * This method traverses the sub-tree of all child sources of the source. The traversal
     * is accomplished via breadth first and can be limited in depth. On each node or
     * source a filter is applied. If the filter returns true the respective source
     * will be added to the result list.
     * By default a filter is used that accepts all sources at all depths.
     *
     * @return A list containing the matching descendant sources.
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
     */
    public Source createSource(String name, String type) {
        Source source = makeSource(name, type);
        if (source.isNone()) {
            source = null;
        }
        return source;
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

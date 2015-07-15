package org.gnode.nix.base;

import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.Properties;
import org.gnode.nix.Source;

import java.util.List;

/**
 * <h1>EntityWithSources</h1>
 * An abstract class for entities that can be associated with sources.
 */

@Properties(value = {
        @Platform(value = "linux"),
        @Platform(value = "windows")})
public abstract class EntityWithSources extends EntityWithMetadata {

    /**
     * Get the number of sources associated with this entity.
     *
     * @return The number sources.
     */
    abstract public long getSourceCount();

    /**
     * Checks if a specific source is associated with this entity.
     *
     * @param id The source id to check.
     * @return True if the source is associated with this entity, false otherwise.
     */
    abstract public boolean hasSource(String id);

    /**
     * Checks if a specific source is associated with this entity.
     *
     * @param source The source to check.
     * @return True if the source is associated with this entity, false otherwise.
     */
    abstract public boolean hasSource(Source source);

    /**
     * Returns an associated source identified by its id.
     *
     * @param id The id of the associated source.
     */
    abstract public Source getSource(String id);

    /**
     * Retrieves an associated source identified by its index.
     *
     * @param index The index of the associated source.
     * @return The source with the given id. If it doesn't exist an exception
     * will be thrown.
     */
    abstract public Source getSource(long index);

    /**
     * Get all sources associated with this entity.
     *
     * @return All associated sources that match the given filter as a vector
     */
    abstract public List<Source> getSources();

    /**
     * Set all sources associations for this entity.
     * <p/>
     * All previously existing associations will be overwritten.
     *
     * @param sources A vector with all sources.
     */
    abstract public void setSources(List<Source> sources);

    /**
     * Associate a new source with the entity.
     * <p/>
     * If a source with the given id already is associated with the
     * entity, the call will have no effect.
     *
     * @param id The id of the source.
     */
    abstract public void addSource(String id);

    /**
     * Associate a new source with the entity.
     * <p/>
     * Calling this method will have no effect if the source is already associated to this entity.
     *
     * @param source The source to add.
     */
    abstract public void addSource(Source source);

    /**
     * Remove a source from the list of associated sources.
     * <p/>
     * This method just removes the association between the entity and the source.
     * The source itself will not be deleted from the file.
     *
     * @param id The id of the source to remove.
     * @return True if the source was removed, false otherwise.
     */
    abstract public boolean removeSource(String id);

    /**
     * Remove a source from the list of associated sources.
     * <p/>
     * This method just removes the association between the entity and the source.
     * The source itself will not be deleted from the file.
     *
     * @param source The source to remove.
     * @return True if the source was removed, false otherwise.
     */
    abstract public boolean removeSource(Source source);

}

package org.g_node.nix.base;

import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.Properties;
import org.g_node.nix.Section;

/**
 * <h1>EntityWithMetadata</h1>
 * An abstract class for entities that can be associated with additional metadata.
 */

@Properties(value = {
        @Platform(value = "linux"),
        @Platform(value = "windows")})
public abstract class EntityWithMetadata extends NamedEntity {

    /**
     * Get metadata associated with this entity.
     *
     * @return The associated section, if no such section exists {#link null} is returned.
     */
    abstract public Section getMetadata();

    /**
     * Associate the entity with some metadata.
     * <p>
     * Calling this method will replace previously stored information.
     *
     * @param metadata The {@link Section} that should be associated
     *                 with this entity.
     */
    abstract public void setMetadata(Section metadata);

    /**
     * Associate the entity with some metadata.
     * <p>
     * Calling this method will replace previously stored information.
     *
     * @param id The id of the {@link Section} that should be associated
     *           with this entity.
     */
    abstract public void setMetadata(String id);

    /**
     * Removes metadata associated with the entity.
     */
    abstract public void removeMetadata();
}

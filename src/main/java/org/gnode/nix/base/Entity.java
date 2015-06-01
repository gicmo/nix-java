package org.gnode.nix.base;

import org.bytedeco.javacpp.annotation.Platform;

import java.util.Date;

@Platform(value = "linux")
public abstract class Entity extends ImplContainer {

    /**
     * Getter for the id of the entity.
     * <p/>
     * The id is generated at creation time of an entity. It contains
     * a randomly generated sequence of characters with low collision
     * probability.
     *
     * @return The id of the entity.
     */
    abstract public String getId();

    /**
     * Gets the time of the last update.
     *
     * @return Time of the last update.
     */
    abstract public Date getUpdatedAt();

    /**
     * Gets the creation time.
     *
     * @return The creation time of the entity.
     */
    abstract public Date getCreatedAt();

    /**
     * Sets the time of the last update to the current time if
     * the field is not set.
     */
    abstract public void setUpdatedAt();

    /**
     * Sets the time of the last update to the current time.
     */
    abstract public void forceUpdatedAt();

    /**
     * Sets the creation time to the current time if the creation
     * time is not set.
     */
    abstract public void setCreatedAt();

    /**
     * Sets the creation time to the provided value even if the
     * field is already set.
     *
     * @param date The creation time to set.
     */
    abstract public void forceCreatedAt(Date date);
}

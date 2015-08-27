package org.g_node.nix.base;

import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.Properties;
import org.g_node.nix.Block;

/**
 * <h1>NamedEntity</h1>
 * An abstract class for entities with a {@link NamedEntity#getName()}, {@link NamedEntity#getType()}
 * and {@link NamedEntity#getDefinition()}.
 */

@Properties(value = {
        @Platform(value = "linux"),
        @Platform(value = "windows")})
public abstract class NamedEntity<T extends NamedEntity> extends Entity implements Comparable<T> {

    /**
     * Setter for the type of the entity.
     *
     * @param type The type to set.
     */
    abstract public void setType(String type);

    /**
     * Getter for the type of the entity.
     * <p>
     * The property type is used in order to allow the specification
     * of additional semantic meaning for an entity and therefore can introduce
     * domain-specificity into the quite generic data model.
     *
     * @return The current type.
     */
    abstract public String getType();

    /**
     * Getter for the name of the entity.
     * <p>
     * The name of an entity serves as a human readable identifier. It is not obliged
     * to be unique. However it is strongly recommended to use unique name inside one specific
     * {@link Block}.
     *
     * @return string The name of the entity.
     */
    abstract public String getName();

    /**
     * Setter for the definition of the entity.
     *
     * @param definition The definition of the entity.
     */
    abstract public void setDefinition(String definition);

    /**
     * Getter for the definition of the entity.
     * <p>
     * The definition is an optional property that allows the user to add
     * a freely assignable textual definition to the entity.
     *
     * @return The definition of the entity.
     */
    abstract public String getDefinition();

    @Override
    public int compareTo(T obj) {
        if (this == obj) {
            return 0;
        }
        return this.getName().compareTo(obj.getName());
    }
}

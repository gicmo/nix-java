package org.gnode.nix;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.annotation.*;
import org.bytedeco.javacpp.annotation.Properties;
import org.gnode.nix.base.NamedEntity;
import org.gnode.nix.internal.*;

import java.util.*;
import java.util.function.Predicate;

@Properties(value = {
        @Platform(include = {"<nix/Section.hpp>"}, link = "nix"),
        @Platform(value = "linux"),
        @Platform(value = "windows")})
@Namespace("nix")
public class Section extends NamedEntity {

    static {
        Loader.load();
    }

    //--------------------------------------------------
    // Constructors
    //--------------------------------------------------

    /**
     * Constructor that creates an uninitialized Section.
     * <p/>
     * Calling any method on an uninitialized section will throw a {@link RuntimeException}.
     */
    public Section() {
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
     * Get id of the section
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
     * Get the creation date of the section.
     *
     * @return The creation date of the section.
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
     * Setter for the type of the section
     *
     * @param type The type of the section
     */
    public native
    @Name("type")
    void setType(@StdString String type);

    /**
     * Getter for the type of the section
     *
     * @return The type of the section
     */
    public native
    @Name("type")
    @StdString
    String getType();

    /**
     * Getter for the name of the section.
     *
     * @return The name of the section.
     */
    public native
    @Name("name")
    @StdString
    String getName();

    private native void definition(@Const @ByVal None t);

    private native void definition(@StdString String definition);

    /**
     * Setter for the definition of the section.
     *
     * @param definition definition of section. If {#link null} is passed definition is removed.
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
     * Getter for the definition of the section.
     *
     * @return The definition of the section. {#link null} if not present.
     */
    public String getDefinition() {
        OptionalUtils.OptionalString defintion = definition();
        if (defintion.isPresent()) {
            return defintion.getString();
        }
        return null;
    }

    //--------------------------------------------------
    // Attribute getter and setter
    //--------------------------------------------------

    private native void repository(@Const @ByVal None t);

    private native void repository(@StdString String repository);

    /**
     * Set the repository in which a section of this type is defined.
     * <p/>
     * Usually this information is provided in the form of an URL
     *
     * @param repository URL to the repository. If {#link null} is passed repository is removed.
     */

    public void setRepository(String repository) {
        if (repository != null) {
            repository(repository);
        } else {
            repository(new None());
        }
    }

    private native
    @ByVal
    OptionalUtils.OptionalString repository();

    /**
     * Gets the repository URL.
     *
     * @return The URL to the repository. {#link null} if not present.
     */
    public String getRepository() {
        OptionalUtils.OptionalString repository = repository();
        if (repository.isPresent()) {
            return repository.getString();
        }
        return null;
    }

    /**
     * Establish a link to another section.
     * <p/>
     * The linking section inherits the properties defined in the linked section.
     * Properties of the same name are overridden.
     *
     * @param id The id of the section that should be linked.
     */
    public native
    @Name("link")
    void setLink(@StdString String id);

    /**
     * Establish a link to another section.
     * <p/>
     * The linking section inherits the properties defined in the linked section.
     * Properties of the same name are overridden.
     *
     * @param link The section to link with.
     */
    public native
    @Name("link")
    void setLink(@Const @ByRef Section link);

    private native
    @ByVal
    Section link();

    /**
     * Get the linked section.
     *
     * @return The linked section. If no section was linked a null
     * Section will be returned.
     */
    public Section getLink() {
        Section section = link();
        if (section.isInitialized()) {
            return section;
        }
        return null;
    }

    private native void link(@Const @ByVal None t);

    /**
     * Deleter for the linked section.
     * <p/>
     * This just removes the link between both sections, but does not remove
     * the linked section from the file.
     */
    public void removeLink() {
        link(new None());
    }

    private native void mapping(@Const @ByVal None t);

    private native void mapping(@StdString String mapping);

    /**
     * Sets the mapping information for this section.
     * <p/>
     * The mapping is provided as a path or URL to another section.
     *
     * @param mapping The mapping information to this section.  If {#link null} is passed mapping is removed.
     */
    public void setMapping(String mapping) {
        if (mapping != null) {
            mapping(mapping);
        } else {
            mapping(new None());
        }
    }

    private native
    @ByVal
    OptionalUtils.OptionalString mapping();

    /**
     * Gets the mapping information.
     *
     * @return The mapping information. {#link null} if not present.
     */
    public String getMapping() {
        OptionalUtils.OptionalString mapping = mapping();
        if (mapping.isPresent()) {
            return mapping.getString();
        }
        return null;
    }


    //--------------------------------------------------
    // Methods for parent access
    //--------------------------------------------------

    private native
    @ByVal
    Section parent();

    /**
     * Returns the parent section.
     * <p/>
     * Each section which is not a root section has a parent.
     *
     * @return The parent section. If the section has no parent, a null section will be returned.
     */
    public Section getParent() {
        Section section = parent();
        if (section.isInitialized()) {
            return section;
        }
        return null;
    }

    //--------------------------------------------------
    // Methods for child section access
    //--------------------------------------------------

    /**
     * Get the number of child section of the section.
     *
     * @return The number of child sections.
     */
    public native
    @Name("sectionCount")
    long getSectionCount();

    /**
     * Checks whether a section has a certain child section.
     *
     * @param nameOrId Name or id of requested section.
     * @return True if the section is a child, false otherwise.
     */
    public native
    @Cast("bool")
    boolean hasSection(@StdString String nameOrId);

    /**
     * Checks whether a section has a certain child section.
     *
     * @param section The section to check.
     * @return True if the section is a child, false otherwise.
     */
    public native
    @Cast("bool")
    boolean hasSection(@Const @ByRef Section section);

    private native
    @Name("getSection")
    @ByVal
    Section fetchSection(@StdString String nameOrId);

    /**
     * Get a specific child section by its name or id.
     *
     * @param nameOrId The name or the ID of the child section.
     * @return The child section.
     */
    public Section getSection(String nameOrId) {
        Section section = fetchSection(nameOrId);
        if (section.isInitialized()) {
            return section;
        }
        return null;
    }

    private native
    @Name("getSection")
    @ByVal
    Section fetchSection(@Cast("size_t") long index);

    /**
     * Get a child section by its index.
     *
     * @param index The index of the child.
     * @return The specified child section.
     */
    public Section getSection(long index) {
        Section section = fetchSection(index);
        if (section.isInitialized()) {
            return section;
        }
        return null;
    }

    private native
    @ByVal
    VectorUtils.SectionVector sections();

    /**
     * Get all direct child sections of the section.
     *
     * @return list of child sections
     */
    public List<Section> getSections() {
        return sections().getSections();
    }

    /**
     * Get all direct child sections of the section.
     * <p>
     * The parameter filter can be used to filter sections by various
     * criteria.
     *
     * @param filter A filter function.
     * @return A list containing the matching child sections.
     */
    public List<Section> getSections(Predicate<Section> filter) {
        List<Section> result = new ArrayList<>();
        for (Section section : getSections()) {
            if (filter.test(section)) {
                result.add(section);
            }
        }
        return result;
    }

    private static class SectionCont {
        final Section entity;
        final int depth;

        public SectionCont(Section entity, int depth) {
            this.entity = entity;
            this.depth = depth;
        }
    }

    /**
     * Get all descendant sections of the section recursively.
     * <p>
     * This method traverses the sub-tree of all child sections of the section. The traversal
     * is accomplished via breadth first and can be limited in depth. On each node or
     * section a filter is applied. If the filter returns true the respective section
     * will be added to the result list.
     *
     * @param filter   A filter function.
     * @param maxDepth The maximum depth of traversal.
     * @return A list containing the matching descendant sections.
     */
    public List<Section> findSections(Predicate<Section> filter, int maxDepth) {
        List<Section> results = new ArrayList<>();
        Queue<SectionCont> todo = new LinkedList<>();
        int level = 0;

        todo.add(new SectionCont(this, level));

        while (todo.size() > 0) {
            SectionCont current = todo.remove();

            if (filter.test(current.entity)) {
                results.add(current.entity);
            }

            if (current.depth < maxDepth) {
                List<Section> children = current.entity.getSections();
                int next_depth = current.depth + 1;

                for (Section child : children) {
                    todo.add(new SectionCont(child, next_depth));
                }
            }
        }

        return results;
    }

    /**
     * Get all descendant sections of the section recursively.
     * <p>
     * This method traverses the sub-tree of all child sections of the section. The traversal
     * is accomplished via breadth first. On each node or section a filter is applied.
     * If the filter returns true the respective section will be added to the result list.
     * By default nodes at all depths are considered.
     *
     * @param filter A filter function.
     * @return A list containing the matching descendant sections.
     */
    public List<Section> findSections(Predicate<Section> filter) {
        return findSections(filter, Integer.MAX_VALUE);
    }

    /**
     * Get all descendant sections of the section recursively.
     * <p>
     * This method traverses the sub-tree of all child sections of the section. The traversal
     * is accomplished via breadth first. On each node or section a filter is applied.
     * If the filter returns true the respective section will be added to the result list.
     * By default a filter is used that accepts all sections.
     *
     * @param maxDepth The maximum depth of traversal.
     * @return A list containing the matching descendant sections.
     */
    public List<Section> findSections(int maxDepth) {
        return findSections((Section s) -> true, maxDepth);
    }

    /**
     * Get all descendant sections of the section recursively.
     * <p>
     * This method traverses the sub-tree of all child sections of the section. The traversal
     * is accomplished via breadth first. On each node or section a filter is applied.
     * If the filter returns true the respective section will be added to the result list.
     * By default a filter is used that accepts all sections at all depths.
     *
     * @return A list containing the matching descendant sections.
     */
    public List<Section> findSections() {
        return findSections((Section s) -> true, Integer.MAX_VALUE);
    }

    /**
     * Find all related sections of the section.
     *
     * @param filter A filter function.
     * @return A list containing all filtered related sections.
     */
    public List<Section> findRelated(Predicate<Section> filter) {
        List<Section> results = findDownstream(filter);

        String myId = getId();

        //This checking of results can be removed if we decide not to include this in findSection
        results.removeIf(s -> s.getId().equals(myId));
        int resultsSize = results.size();

        if (resultsSize == 0) {
            results = findUpstream(filter);
        }

        //This checking of results can be removed if we decide not to include this in findSection
        results.removeIf(s -> s.getId().equals(myId));
        resultsSize = results.size();

        if (resultsSize == 0) {
            results = findSideways(filter, getId());
        }
        return results;
    }

    /**
     * Find all related sections of the section.
     *
     * @return A list containing all filtered related sections.
     */
    public List<Section> findRelated() {
        return findRelated((Section s) -> true);
    }

    private native
    @Name("createSection")
    @ByVal
    Section makeSection(@StdString String name, @StdString String type);

    /**
     * Adds a new child section.
     *
     * @param name The name of the new section
     * @param type The type of the section
     * @return The new child section.
     */
    public Section createSection(String name, String type) {
        Section section = makeSection(name, type);
        if (section.isInitialized()) {
            return section;
        }
        return null;
    }

    /**
     * Deletes a section from the section.
     *
     * @param nameOrId Name or id of the child section to delete.
     * @return True if the section was deleted, false otherwise.
     */
    public native
    @Cast("bool")
    boolean deleteSection(@StdString String nameOrId);

    /**
     * Deletes a subsection from this Section.
     *
     * @param section The section to delete.
     * @return True if the section was deleted, false otherwise.
     */
    public native
    @Cast("bool")
    boolean deleteSection(@Const @ByRef Section section);

    //--------------------------------------------------
    // Methods for property access
    //--------------------------------------------------

    /**
     * Gets the number of properties of this section.
     *
     * @return The number of Properties
     */
    public native
    @Name("propertyCount")
    long getPropertyCount();

    /**
     * Checks if a Property with this name/id exists in this Section.
     *
     * @param nameOrId Name or id of the property.
     * @return True if the property exists, false otherwise.
     */
    public native
    @Cast("bool")
    boolean hasProperty(@StdString String nameOrId);

    /**
     * Checks if a Property exists in this Section.
     *
     * @param property The Property to check.
     * @return True if the property exists, false otherwise.
     */
    public native
    @Cast("bool")
    boolean hasProperty(@Const @ByRef Property property);

    private native
    @Name("getProperty")
    @ByVal
    Property fetchProperty(@StdString String nameOrId);

    /**
     * Gets the Property identified by its name or id.
     *
     * @param nameOrId Name or id of the property.
     * @return The specified property.
     */
    public Property getProperty(String nameOrId) {
        Property property = fetchProperty(nameOrId);
        if (property.isInitialized()) {
            return property;
        }
        return null;
    }

    private native
    @Name("getProperty")
    @ByVal
    Property fetchProperty(@Cast("size_t") long index);

    /**
     * Gets the property defined by its index.
     *
     * @param index The index of the property.
     * @return The property.
     */
    public Property getProperty(long index) {
        Property property = fetchProperty(index);
        if (property.isInitialized()) {
            return property;
        }
        return null;
    }

    private native
    @ByVal
    VectorUtils.PropertyVector properties();

    /**
     * Get all properties of the section.
     *
     * @return list of properties.
     */
    public List<Property> getProperties() {
        return properties().getProperties();
    }

    private native
    @ByVal
    VectorUtils.PropertyVector inheritedProperties();

    /**
     * Returns all Properties inherited from a linked section.
     * This list may include Properties that are locally overridden.
     *
     * @return All inherited properties as a list.
     */
    public List<Property> getInheritedProperties() {
        return inheritedProperties().getProperties();
    }

    private native
    @Name("createProperty")
    @ByVal
    Property makeProperty(@StdString String name, @Cast("nix::DataType") int dtype);

    /**
     * Add a new Property that does not have any Values to this Section.
     *
     * @param name  The name of the property.
     * @param dtype The DataType of the property.
     * @return The newly created property
     */
    public Property createProperty(String name, int dtype) {
        Property property = makeProperty(name, dtype);
        if (property.isInitialized()) {
            return property;
        }
        return null;
    }

    private native
    @Name("createProperty")
    @ByVal
    Property makeProperty(@StdString String name, @Const @ByRef Value value);

    /**
     * Add a new Property to the Section.
     *
     * @param name  The name of the property.
     * @param value The Value to be stored.
     * @return The newly created property.
     */
    public Property createProperty(String name, Value value) {
        Property property = makeProperty(name, value);
        if (property.isInitialized()) {
            return property;
        }
        return null;
    }

    private native
    @Name("createProperty")
    @ByVal
    Property makeProperty(@StdString String name, @Const @ByRef VectorUtils.ValueVector values);

    /**
     * Add a new Property with values to the Section.
     *
     * @param name   The name of the property.
     * @param values The values of the created property.
     * @return The newly created property.
     */
    public Property createProperty(String name, List<Value> values) {
        Property property = makeProperty(name, new VectorUtils.ValueVector(values));
        if (property.isInitialized()) {
            return property;
        }
        return null;
    }

    /**
     * Delete the Property identified by its name or id.
     *
     * @param nameOrId Name or id of the property.
     * @return True if the property was deleted, false otherwise.
     */
    public native
    @Cast("bool")
    boolean deleteProperty(@StdString String nameOrId);

    /**
     * Deletes the Property from this section.
     *
     * @param property The Property to delete.
     * @return True if the property was deleted, false otherwise.
     */
    public native
    @Cast("bool")
    boolean deleteProperty(@Const @ByRef Property property);

    //------------------------------------------------------
    // Other functions
    //------------------------------------------------------

    private int treeDepth() {
        List<Section> children = getSections();
        int depth = 0;
        if (children.size() > 0) {
            for (Section child : children) {
                depth = Math.max(depth, child.treeDepth());
            }
            depth += 1;
        }
        return depth;
    }

    private List<Section> findDownstream(Predicate<Section> filter) {
        List<Section> results = new ArrayList<>();
        int max_depth = treeDepth();
        int actual_depth = 1;
        while (results.size() == 0 && actual_depth <= max_depth) {
            results = findSections(filter, actual_depth);
            actual_depth += 1;
        }
        return results;
    }

    private List<Section> findUpstream(Predicate<Section> filter) {
        List<Section> results = new ArrayList<>();
        Section p = parent();

        if (p != null) {
            results = p.findSections(filter, 1);
            if (results.size() > 0) {
                return results;
            }
            return p.findUpstream(filter);
        }
        return results;
    }

    private List<Section> findSideways(Predicate<Section> filter, String callerId) {
        List<Section> results = new ArrayList<>();
        Section p = parent();
        if (p != null) {
            results = p.findSections(filter, 1);
            if (results.size() > 0) {
                results.removeIf(s -> s.getId().equals(callerId));
                return results;
            }
            return p.findSideways(filter, callerId);
        }
        return results;
    }

    //--------------------------------------------------
    // Overrides
    //--------------------------------------------------

    //--------------------------------------------------
    // Overrides
    //--------------------------------------------------

    @Override
    public String toString() {
        return "Section: {name = " + this.getName()
                + ", type = " + this.getType()
                + ", id = " + this.getId() + "}";
    }
}
package org.gnode.nix;

import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.annotation.*;
import org.gnode.nix.base.ImplContainer;
import org.gnode.nix.internal.Utils;
import org.gnode.nix.internal.VectorUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Platform(value = "linux",
        include = {"<nix/File.hpp>"},
        link = {"nix"})
@Namespace("nix")
public class File extends ImplContainer implements Comparable<File> {

    static {
        Loader.load();
    }

    //--------------------------------------------------
    // Constructors
    //--------------------------------------------------

    /**
     * Constructor that creates an uninitialized File.
     * <p/>
     * Calling any method on an uninitialized file will throw a {@link java.lang.RuntimeException}.
     */
    public File() {
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
     * Opens a file.
     *
     * @param name The name/path of the file.
     * @param mode The open mode. Check {@link FileMode} for options.
     * @param impl The back-end implementation the should be used to open the file. Check {@link Implementation} for options.
     * @return The opened file.
     * @see FileMode
     * @see Implementation
     */
    public static native
    @ByVal
    File open(@StdString String name, @Cast("nix::FileMode") int mode,
              @Cast("nix::Implementation") int impl);

    /**
     * Opens a file with back-end implementation in hdf5.
     *
     * @param name The name/path of the file.
     * @param mode The open mode. Check {@link FileMode} for options.
     * @return The opened file.
     * @see FileMode
     */
    public static File open(@StdString String name, int mode) {
        return open(name, mode, Implementation.Hdf5);
    }

    /**
     * Opens a file in ReadWrite mode and with back-end implementation in hdf5.
     *
     * @param name The name/path of the file.
     * @return The opened file.
     */
    public static native
    @ByVal
    File open(@StdString String name);

    //--------------------------------------------------
    // Methods concerning Block
    //--------------------------------------------------

    /**
     * Get the number of blocks in in the file.
     *
     * @return The number of blocks.
     * @see Block
     */
    public native
    @Name("blockCount")
    long getBlockCount();

    /**
     * Check if a block exists in the file.
     *
     * @param nameOrId Name or ID of the block.
     * @return True if the block exists, false otherwise.
     * @see Block
     */
    public native
    @Cast("bool")
    boolean hasBlock(@StdString String nameOrId);

    /**
     * Check if a block exists in the file.
     *
     * @param block The block to check.
     * @return True if the block exists, false otherwise.
     * @see Block
     */
    public native
    @Cast("bool")
    boolean hasBlock(@Const @ByRef Block block);

    private native
    @Name("getBlock")
    @ByVal
    Block block(@StdString String nameOrId);

    /**
     * Read an existing block from the file.
     *
     * @param nameOrId Name or ID of the block.
     * @return The block with the given name or id. {@link null} returned if not present.
     * @see Block
     */
    public
    @ByVal
    Block getBlock(@StdString String nameOrId) {
        Block block = block(nameOrId);
        if (block.isInitialized()) {
            return block;
        }
        return null;
    }

    private native
    @Name("getBlock")
    @ByVal
    Block block(@Cast("size_t") long index);

    /**
     * Read an existing with block from the file, addressed by index.
     *
     * @param index The index of the block to read.
     * @return The block at the given index. {@link null} returned if not present.
     * @see Block
     */
    public
    @ByVal
    Block getBlock(@Cast("size_t") long index) {
        Block block = block(index);
        if (block.isInitialized()) {
            return block;
        }
        return null;
    }

    /**
     * Create an new block, that is immediately persisted in the file.
     *
     * @param name The name of the block.
     * @param type The type of the block.
     * @return The created block.
     * @see Block
     */
    public native
    @ByVal
    Block createBlock(@StdString String name, @StdString String type);

    /**
     * Deletes a block from the file.
     *
     * @param nameOrId Name or id of the block to delete.
     * @return True if the block has been removed, false otherwise.
     * @see Block
     */
    public native
    @Cast("bool")
    boolean deleteBlock(@StdString String nameOrId);

    /**
     * Deletes a block from the file.
     *
     * @param block The block to delete.
     * @return True if the block has been removed, false otherwise.
     * @see Block
     */
    public native
    @Cast("bool")
    boolean deleteBlock(@Const @ByRef Block block);

    private native
    @ByVal
    VectorUtils.BlockVector blocks();

    /**
     * Get all blocks within this file.
     *
     * @return A list with Block entities.
     * @see Block
     */
    public List<Block> getBlocks() {
        return blocks().getBlocks();
    }


    //--------------------------------------------------
    // Methods concerning sections
    //--------------------------------------------------

    /**
     * Check if a specific root section exists in the file.
     *
     * @param nameOrId Name or ID of the section.
     * @return True if the section exists, false otherwise.
     */
    public native
    @Cast("bool")
    boolean hasSection(@StdString String nameOrId);

    /**
     * Check if a specific root section exists in the file.
     *
     * @param section The section to check.
     * @return True if the section exists, false otherwise.
     */
    public native
    @Cast("bool")
    boolean hasSection(@Const @ByRef Section section);

    private native
    @Name("getSection")
    @ByVal
    Section fetchSection(@StdString String nameOrId);

    /**
     * Get a root section with the given name/id.
     *
     * @param nameOrId Name or id of the section.
     * @return The section with the specified name/id.
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
     * Get root section with a given index/position.
     *
     * @param index The index of the section.
     * @return The section with the specified index.
     */
    public Section getSection(long index) {
        Section section = fetchSection(index);
        if (section.isInitialized()) {
            return section;
        }
        return null;
    }

    /**
     * Returns the number of root sections stored in the File.
     *
     * @return The number of sections.
     */
    public native
    @Name("sectionCount")
    long getSectionCount();

    private native
    @ByVal
    VectorUtils.SectionVector sections();

    /**
     * Get all root sections within this file.
     * <p/>
     * The parameter filter can be used to filter sections by various
     * criteria. By default a filter is used that accepts all sections.
     *
     * @return A vector of filtered Section entities.
     */
    public List<Section> getSections() {
        return sections().getSections();
    }

    private native
    @Name("createSection")
    @ByVal
    Section makeSection(@StdString String name, @StdString String type);

    /**
     * Creates a new Section with a given name and type. Both must not be empty.
     *
     * @param name The name of the section.
     * @param type The type of the section.
     * @return The created Section.
     */
    public Section createSection(String name, String type) {
        Section section = makeSection(name, type);
        if (section.isInitialized()) {
            return section;
        }
        return null;
    }

    /**
     * Deletes the Section that is specified with the id.
     *
     * @param nameOrId Name or id of the section to delete.
     * @return True if the section was deleted, false otherwise.
     */
    public native
    @Cast("bool")
    boolean deleteSection(@StdString String nameOrId);

    /**
     * Deletes the Section.
     *
     * @param section The section to delete.
     * @return True if the section was deleted, false otherwise.
     */
    public native
    @Cast("bool")
    boolean deleteSection(@Const @ByRef Section section);


    //------------------------------------------------------
    // Methods for file attribute access.
    //------------------------------------------------------

    private native
    @StdVector
    IntPointer version();

    /**
     * Read the NIX format version from the file.
     * <p/>
     * The version consist of three integers standing for the major, minor and patch version of the nix format.
     *
     * @return The format version of the NIX file.
     */
    public ArrayList<Integer> getVersion() {
        return VectorUtils.convertPointerToList(version());
    }

    /**
     * Read the format hint from the file.
     *
     * @return format of file
     */
    public native
    @Name("format")
    @StdString
    String getFormat();

    /**
     * Return the location / uri.
     *
     * @return The uri string.
     */
    public native
    @Name("location")
    @StdString
    String getLocation();

    private native
    @Cast("time_t")
    long createdAt();

    /**
     * Get the creation date of the file.
     *
     * @return The creation date of the file.
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


    //------------------------------------------------------
    // Other functions
    //------------------------------------------------------

    /**
     * Close the file.
     */
    public native void close();

    /**
     * Check if the file is currently open.
     *
     * @return True if the file is open, false otherwise.
     */
    public native
    @Cast("bool")
    boolean isOpen();

    @Override
    public int compareTo(File file) {
        if (this == file) {
            return 0;
        }
        return this.getLocation().compareTo(file.getLocation());
    }
}
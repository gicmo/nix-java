package org.g_node.nix.valid;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.annotation.*;
import org.g_node.nix.internal.BuildLibs;

/**
 * <h1>Message</h1>
 * Message class with entity id and msg string
 * <p>
 * Class with message text and id string to save a message and the
 * entity id together, used by {@link Result}.
 */

@Properties(value = {
        @Platform(include = {"<nix/valid/helper.hpp>"}),
        @Platform(value = "linux", link = BuildLibs.NIX_1, preload = BuildLibs.HDF5_7),
        @Platform(value = "macosx", link = BuildLibs.NIX, preload = BuildLibs.HDF5),
        @Platform(value = "windows",
                link = BuildLibs.NIX,
                preload = {BuildLibs.HDF5, BuildLibs.MSVCP120, BuildLibs.MSVCR120, BuildLibs.SZIP, BuildLibs.ZLIB})})
@Namespace("nix::valid")
public class Message extends Pointer {
    static {
        Loader.load();
    }

    /**
     * Create new message.
     *
     * @param newId  id
     * @param newMsg text
     */
    public Message(String newId, String newMsg) {
        allocate(newId, newMsg);
    }

    private native void allocate(@StdString String newId, @StdString String newMsg);

    /**
     * Get id of message.
     *
     * @return id of message.
     */
    public native
    @Name("id")
    @MemberGetter
    @StdString
    String getId();

    /**
     * Set id of message.
     *
     * @param id id of message.
     */
    public native
    @Name("id")
    @MemberSetter
    void setId(@StdString String id);

    /**
     * Get text of message.
     *
     * @return text
     */
    public native
    @Name("msg")
    @MemberGetter
    @StdString
    String getMsg();

    /**
     * Set text for message.
     *
     * @param msg text
     */
    public native
    @Name("msg")
    @MemberSetter
    void setMsg(@StdString String msg);

    //--------------------------------------------------
    // Overrides
    //--------------------------------------------------

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Message)) {
            return false;
        }

        Message message = (Message) obj;

        return this.getId().equals(message.getId())
                && this.getMsg().equals(message.getMsg());
    }

    @Override
    public String toString() {
        return "Message: {id = " + this.getId()
                + ", msg = " + this.getMsg() + "}";
    }
}

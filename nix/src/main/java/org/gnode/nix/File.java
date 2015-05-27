package org.gnode.nix;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.annotation.ByRef;
import org.bytedeco.javacpp.annotation.Const;
import org.bytedeco.javacpp.annotation.Namespace;
import org.bytedeco.javacpp.annotation.Platform;

@Platform(value = "linux",
        include = {"<nix/File.hpp>"},
        link = {"nix"},
        linkpath = {"/usr/local/lib/"},
        includepath = {"/usr/local/include/"})
@Namespace("nix")
public class File extends Pointer {

    static {
        Loader.load();
    }

    /**
     * Constructor that creates an uninitialized File.
     * <p/>
     * Calling any method on an uninitialized file will throw a {@link java.lang.RuntimeException}.
     */
    public File() {
        allocate();
    }

    private native void allocate();

    /**
     * Copy constructor.
     *
     * @param other The file to copy.
     */
    public File(@Const @ByRef File other) {
        allocate(other);
    }

    private native void allocate(@Const @ByRef File other);

    /**
     * Close the file.
     */
    public native void close();
}

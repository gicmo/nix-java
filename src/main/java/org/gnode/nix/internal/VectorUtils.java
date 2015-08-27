package org.gnode.nix.internal;

import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.annotation.*;
import org.gnode.nix.*;
import org.gnode.nix.valid.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>VectorUtils</h1>
 * Low level wrapper to std vectors.
 */

@Properties(value = {
        @Platform(include = {
                "<vector>",
                "<nix/Block.hpp>",
                "<nix/DataArray.hpp>",
                "<nix/Dimensions.hpp>",
                "<nix/Feature.hpp>",
                "<nix/MultiTag.hpp>",
                "<nix/Property.hpp>",
                "<nix/Section.hpp>",
                "<nix/Source.hpp>",
                "<nix/Tag.hpp>",
                "<nix/Value.hpp>",
                "<nix/valid/helper.hpp>"}),
        @Platform(value = "linux", link = BuildLibs.NIX_1, preload = BuildLibs.HDF5_7),
        @Platform(value = "windows",
                link = BuildLibs.NIX,
                preload = {BuildLibs.HDF5, BuildLibs.MSVCP120, BuildLibs.MSVCR120, BuildLibs.SZIP, BuildLibs.ZLIB})})
public class VectorUtils {

    static {
        Loader.load();
    }

    //--------------------------------------------------
    // Block vector
    //--------------------------------------------------

    /**
     * <h1>BlockVector</h1>
     * Low level <tt>std::vector&lt;nix::Block&gt;</tt> wrapper
     */
    @Name("std::vector<nix::Block>")
    public static class BlockVector extends Pointer {
        static {
            Loader.load();
        }

        /**
         * Set blocks.
         *
         * @param lst list of blocks.
         * @see Block
         */
        public BlockVector(List<Block> lst) {
            allocate(lst.size());

            for (int i = 0; i < lst.size(); i++) {
                put(i, lst.get(i));
            }
        }

        private native void allocate(@Cast("size_t") long n);

        private native long size();

        @Index
        @ByRef
        private native Block get(@Cast("size_t") long i);

        private native BlockVector put(@Cast("size_t") long i, Block block);

        /**
         * Get blocks.
         *
         * @return list of blocks.
         * @see Block
         */
        public List<Block> getBlocks() {
            ArrayList<Block> blocks = new ArrayList<Block>();
            for (int i = 0; i < size(); i++) {
                blocks.add(get(i));
            }
            return blocks;
        }
    }

    //--------------------------------------------------
    // DataArray vector
    //--------------------------------------------------

    /**
     * <h1>DataArrayVector</h1>
     * Low level <tt>std::vector&lt;nix::DataArray&gt;</tt> wrapper
     */
    @Name("std::vector<nix::DataArray>")
    public static class DataArrayVector extends Pointer {
        static {
            Loader.load();
        }

        /**
         * Set data arrays.
         *
         * @param lst list of data arrays.
         * @see DataArray
         */
        public DataArrayVector(List<DataArray> lst) {
            allocate(lst.size());

            for (int i = 0; i < lst.size(); i++) {
                put(i, lst.get(i));
            }
        }

        private native void allocate(@Cast("size_t") long n);

        private native long size();

        @Index
        @ByRef
        private native DataArray get(@Cast("size_t") long i);

        private native DataArrayVector put(@Cast("size_t") long i, DataArray dataArray);

        /**
         * Get data arrays.
         *
         * @return list of data arrays.
         * @see DataArray
         */
        public List<DataArray> getDataArrays() {
            ArrayList<DataArray> dataArrays = new ArrayList<DataArray>();
            for (int i = 0; i < size(); i++) {
                dataArrays.add(get(i));
            }
            return dataArrays;
        }
    }

    //--------------------------------------------------
    // Dimension vector
    //--------------------------------------------------

    /**
     * <h1>DimensionVector</h1>
     * Low level <tt>std::vector&lt;nix::Dimension&gt;</tt> wrapper
     */
    @Name("std::vector<nix::Dimension>")
    public static class DimensionVector extends Pointer {
        static {
            Loader.load();
        }

        /**
         * Set dimensions.
         *
         * @param lst list of dimensions.
         * @see Dimension
         */
        public DimensionVector(List<Dimension> lst) {
            allocate(lst.size());

            for (int i = 0; i < lst.size(); i++) {
                put(i, lst.get(i));
            }
        }

        private native void allocate(@Cast("size_t") long n);

        private native long size();

        @Index
        @ByRef
        private native Dimension get(@Cast("size_t") long i);

        private native DimensionVector put(@Cast("size_t") long i, Dimension dimension);

        /**
         * Get dimensions.
         *
         * @return list of dimensions.
         * @see Dimension
         */
        public List<Dimension> getDimensions() {
            ArrayList<Dimension> dimensions = new ArrayList<Dimension>();
            for (int i = 0; i < size(); i++) {
                dimensions.add(get(i));
            }
            return dimensions;
        }
    }

    //--------------------------------------------------
    // Feature vector
    //--------------------------------------------------

    /**
     * <h1>FeatureVector</h1>
     * Low level <tt>std::vector&lt;nix::Feature&gt;</tt> wrapper
     */
    @Name("std::vector<nix::Feature>")
    public static class FeatureVector extends Pointer {
        static {
            Loader.load();
        }

        /**
         * Set features.
         *
         * @param lst list of features.
         * @see Feature
         */
        public FeatureVector(List<Feature> lst) {
            allocate(lst.size());

            for (int i = 0; i < lst.size(); i++) {
                put(i, lst.get(i));
            }
        }

        private native void allocate(@Cast("size_t") long n);

        private native long size();

        @Index
        @ByRef
        private native Feature get(@Cast("size_t") long i);

        private native FeatureVector put(@Cast("size_t") long i, Feature feature);

        /**
         * Get features.
         *
         * @return list of features.
         * @see Feature
         */
        public List<Feature> getFeatures() {
            ArrayList<Feature> features = new ArrayList<Feature>();
            for (int i = 0; i < size(); i++) {
                features.add(get(i));
            }
            return features;
        }
    }

    //--------------------------------------------------
    // MultiTag vector
    //--------------------------------------------------

    /**
     * <h1>MultiTagVector</h1>
     * Low level <tt>std::vector&lt;nix::MultiTag&gt;</tt> wrapper
     */
    @Name("std::vector<nix::MultiTag>")
    public static class MultiTagVector extends Pointer {
        static {
            Loader.load();
        }

        /**
         * Set multi tags.
         *
         * @param lst list of multi tags.
         * @see MultiTag
         */
        public MultiTagVector(List<MultiTag> lst) {
            allocate(lst.size());

            for (int i = 0; i < lst.size(); i++) {
                put(i, lst.get(i));
            }
        }

        private native void allocate(@Cast("size_t") long n);

        private native long size();

        @Index
        @ByRef
        private native MultiTag get(@Cast("size_t") long i);

        private native MultiTagVector put(@Cast("size_t") long i, MultiTag multiTag);

        /**
         * Get multi tags.
         *
         * @return list of multi tags.
         * @see MultiTag
         */
        public List<MultiTag> getMultiTags() {
            ArrayList<MultiTag> multiTags = new ArrayList<MultiTag>();
            for (int i = 0; i < size(); i++) {
                multiTags.add(get(i));
            }
            return multiTags;
        }
    }

    //--------------------------------------------------
    // Property vector
    //--------------------------------------------------

    /**
     * <h1>PropertyVector</h1>
     * Low level <tt>std::vector&lt;nix::Property&gt;</tt> wrapper
     */
    @Name("std::vector<nix::Property>")
    public static class PropertyVector extends Pointer {
        static {
            Loader.load();
        }

        /**
         * Set properties.
         *
         * @param lst list of properties.
         * @see Property
         */
        public PropertyVector(List<Property> lst) {
            allocate(lst.size());

            for (int i = 0; i < lst.size(); i++) {
                put(i, lst.get(i));
            }
        }

        private native void allocate(@Cast("size_t") long n);

        private native long size();

        @Index
        @ByRef
        private native Property get(@Cast("size_t") long i);

        private native PropertyVector put(@Cast("size_t") long i, Property property);

        /**
         * Get properties.
         *
         * @return list of properties.
         * @see Property
         */
        public List<Property> getProperties() {
            ArrayList<Property> properties = new ArrayList<Property>();
            for (int i = 0; i < size(); i++) {
                properties.add(get(i));
            }
            return properties;
        }
    }

    //--------------------------------------------------
    // Section vector
    //--------------------------------------------------

    /**
     * <h1>SectionVector</h1>
     * Low level <tt>std::vector&lt;nix::Section&gt;</tt> wrapper
     */
    @Name("std::vector<nix::Section>")
    public static class SectionVector extends Pointer {
        static {
            Loader.load();
        }

        /**
         * Set sections.
         *
         * @param lst list of sections.
         * @see Section
         */
        public SectionVector(List<Section> lst) {
            allocate(lst.size());

            for (int i = 0; i < lst.size(); i++) {
                put(i, lst.get(i));
            }
        }

        private native void allocate(@Cast("size_t") long n);

        private native long size();

        @Index
        @ByRef
        private native Section get(@Cast("size_t") long i);

        private native SectionVector put(@Cast("size_t") long i, Section section);

        /**
         * Get sections.
         *
         * @return list of sections.
         * @see Section
         */
        public List<Section> getSections() {
            ArrayList<Section> sections = new ArrayList<Section>();
            for (int i = 0; i < size(); i++) {
                sections.add(get(i));
            }
            return sections;
        }
    }

    //--------------------------------------------------
    // Source vector
    //--------------------------------------------------

    /**
     * <h1>SourceVector</h1>
     * Low level <tt>std::vector&lt;nix::Source&gt;</tt> wrapper
     */
    @Name("std::vector<nix::Source>")
    public static class SourceVector extends Pointer {
        static {
            Loader.load();
        }

        /**
         * Set sources.
         *
         * @param lst list of sources.
         * @see Source
         */
        public SourceVector(List<Source> lst) {
            allocate(lst.size());

            for (int i = 0; i < lst.size(); i++) {
                put(i, lst.get(i));
            }
        }

        private native void allocate(@Cast("size_t") long n);

        private native long size();

        @Index
        @ByRef
        private native Source get(@Cast("size_t") long i);

        private native SourceVector put(@Cast("size_t") long i, Source source);

        /**
         * Get source.
         *
         * @return list of sources.
         * @see Source
         */
        public List<Source> getSources() {
            ArrayList<Source> sources = new ArrayList<Source>();
            for (int i = 0; i < size(); i++) {
                sources.add(get(i));
            }
            return sources;
        }
    }

    //--------------------------------------------------
    // Tag vector
    //--------------------------------------------------

    /**
     * <h1>TagVector</h1>
     * Low level <tt>std::vector&lt;nix::Tag&gt;</tt> wrapper
     */
    @Name("std::vector<nix::Tag>")
    public static class TagVector extends Pointer {
        static {
            Loader.load();
        }

        /**
         * Set tags.
         *
         * @param lst list of tags.
         * @see Tag
         */
        public TagVector(List<Tag> lst) {
            allocate(lst.size());

            for (int i = 0; i < lst.size(); i++) {
                put(i, lst.get(i));
            }
        }

        private native void allocate(@Cast("size_t") long n);

        private native long size();

        @Index
        @ByRef
        private native Tag get(@Cast("size_t") long i);

        private native TagVector put(@Cast("size_t") long i, Tag tag);

        /**
         * Get tags.
         *
         * @return list of tags.
         * @see Tag
         */
        public List<Tag> getTags() {
            ArrayList<Tag> tags = new ArrayList<Tag>();
            for (int i = 0; i < size(); i++) {
                tags.add(get(i));
            }
            return tags;
        }
    }

    //--------------------------------------------------
    // Value vector
    //--------------------------------------------------

    /**
     * <h1>ValueVector</h1>
     * Low level <tt>std::vector&lt;nix::Value&gt;</tt> wrapper
     */
    @Name("std::vector<nix::Value>")
    public static class ValueVector extends Pointer {
        static {
            Loader.load();
        }

        /**
         * Set values.
         *
         * @param lst list of values.
         * @see Value
         */
        public ValueVector(List<Value> lst) {
            allocate(lst.size());

            for (int i = 0; i < lst.size(); i++) {
                put(i, lst.get(i));
            }
        }

        private native void allocate(@Cast("size_t") long n);

        private native long size();

        @Index
        @ByRef
        private native Value get(@Cast("size_t") long i);

        private native ValueVector put(@Cast("size_t") long i, Value value);

        /**
         * Get values.
         *
         * @return list of values.
         * @see Value
         */
        public List<Value> getValues() {
            ArrayList<Value> values = new ArrayList<Value>();
            for (int i = 0; i < size(); i++) {
                values.add(get(i));
            }
            return values;
        }
    }

    //--------------------------------------------------
    // Message vector
    //--------------------------------------------------

    /**
     * <h1>MessageVector</h1>
     * Low level <tt>std::vector&lt;nix::valid::Message&gt;</tt> wrapper
     */
    @Name("std::vector<nix::valid::Message>")
    public static class MessageVector extends Pointer {
        static {
            Loader.load();
        }

        /**
         * Set messages.
         *
         * @param lst list of messages.
         * @see Message
         */
        public MessageVector(List<Message> lst) {
            allocate(lst.size());

            for (int i = 0; i < lst.size(); i++) {
                put(i, lst.get(i));
            }
        }

        private native void allocate(@Cast("size_t") long n);

        private native long size();

        @Index
        @ByRef
        private native Message get(@Cast("size_t") long i);

        private native MessageVector put(@Cast("size_t") long i, Message message);

        /**
         * Get messages.
         *
         * @return list of messages.
         * @see Message
         */
        public List<Message> getMessages() {
            ArrayList<Message> messages = new ArrayList<Message>();
            for (int i = 0; i < size(); i++) {
                messages.add(get(i));
            }
            return messages;
        }
    }

    //--------------------------------------------------
    // String vector
    //--------------------------------------------------

    /**
     * <h1>StringVector</h1>
     * Low level <tt>std::vector&lt;std::string&gt;</tt> wrapper
     */
    @Name("std::vector<std::string>")
    public static class StringVector extends Pointer {
        static {
            Loader.load();
        }

        /**
         * Set list of strings.
         *
         * @param lst list of strings.
         * @see String
         */
        public StringVector(List<String> lst) {
            allocate(lst.size());

            for (int i = 0; i < lst.size(); i++) {
                put(i, lst.get(i));
            }
        }

        private native void allocate(@Cast("size_t") long n);

        private native long size();

        @Index
        @ByRef
        private native String get(@Cast("size_t") long i);

        private native StringVector put(@Cast("size_t") long i, String value);

        /**
         * Get Strings.
         *
         * @return list of strings.
         * @see String
         */
        public List<String> getStrings() {
            ArrayList<String> strings = new ArrayList<String>();
            for (int i = 0; i < size(); i++) {
                strings.add(get(i));
            }
            return strings;
        }
    }

    //--------------------------------------------------
    // IntPointer utilities
    //--------------------------------------------------

    /**
     * Converts {@link IntPointer} to integer array
     *
     * @param ip {@link IntPointer} pointer to be converted
     * @return array of ints
     */
    public static int[] convertPointerToArray(IntPointer ip) {
        int[] arr = null;
        if (ip != null) {
            arr = new int[ip.capacity()];
            for (int i = 0; i < ip.capacity(); i++) {
                arr[i] = ip.get(i);
            }
        }
        return arr;
    }

    //--------------------------------------------------
    // DoublePointer utilities
    //--------------------------------------------------

    /**
     * Converts {@link DoublePointer} to double array.
     *
     * @param dp {@link DoublePointer} pointer to be converted
     * @return array of doubles
     */
    public static double[] convertPointerToArray(DoublePointer dp) {
        double[] arr = null;
        if (dp != null) {
            arr = new double[dp.capacity()];
            for (int i = 0; i < dp.capacity(); i++) {
                arr[i] = dp.get(i);
            }
        }
        return arr;
    }
}
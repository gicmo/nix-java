package org.gnode.nix.internal;

import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.annotation.*;
import org.gnode.nix.*;

import java.util.ArrayList;
import java.util.List;

@Platform(value = "linux",
        include = {
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
                "<nix/Value.hpp>"},
        link = {"nix"})
public class VectorUtils {

    static {
        Loader.load();
    }

    //--------------------------------------------------
    // Block vector
    //--------------------------------------------------

    @Name("std::vector<nix::Block>")
    public static class BlockVector extends Pointer {
        static {
            Loader.load();
        }

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

    @Name("std::vector<nix::DataArray>")
    public static class DataArrayVector extends Pointer {
        static {
            Loader.load();
        }

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

    @Name("std::vector<nix::Dimension>")
    public static class DimensionVector extends Pointer {
        static {
            Loader.load();
        }

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

    @Name("std::vector<nix::Feature>")
    public static class FeatureVector extends Pointer {
        static {
            Loader.load();
        }

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

    @Name("std::vector<nix::MultiTag>")
    public static class MultiTagVector extends Pointer {
        static {
            Loader.load();
        }

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

    @Name("std::vector<nix::Property>")
    public static class PropertyVector extends Pointer {
        static {
            Loader.load();
        }

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

    @Name("std::vector<nix::Section>")
    public static class SectionVector extends Pointer {
        static {
            Loader.load();
        }

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

    @Name("std::vector<nix::Source>")
    public static class SourceVector extends Pointer {
        static {
            Loader.load();
        }

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

    @Name("std::vector<nix::Tag>")
    public static class TagVector extends Pointer {
        static {
            Loader.load();
        }

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

    @Name("std::vector<nix::Value>")
    public static class ValueVector extends Pointer {
        static {
            Loader.load();
        }

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

        public List<Value> getValues() {
            ArrayList<Value> values = new ArrayList<Value>();
            for (int i = 0; i < size(); i++) {
                values.add(get(i));
            }
            return values;
        }
    }

    //--------------------------------------------------
    // String vector
    //--------------------------------------------------

    @Name("std::vector<std::string>")
    public static class StringVector extends Pointer {
        static {
            Loader.load();
        }

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
     * Converts {@link IntPointer} to {@link Integer} {@link List}
     *
     * @param ip {@link IntPointer} pointer to be converted
     * @return list of ints
     */
    public static List<Integer> convertPointerToList(IntPointer ip) {
        ArrayList<Integer> intList = new ArrayList<Integer>();
        if (ip != null) {
            for (int i = 0; i < ip.capacity(); i++) {
                intList.add(ip.get(i));
            }
        }
        return intList;
    }

    //--------------------------------------------------
    // DoublePointer utilities
    //--------------------------------------------------

    /**
     * Converts {@link DoublePointer} to {@link Double} {@link List}
     *
     * @param dp {@link DoublePointer} pointer to be converted
     * @return list of doubles
     */
    public static List<Double> convertPointerToList(DoublePointer dp) {
        ArrayList<Double> doubleList = new ArrayList<Double>();
        if (dp != null) {
            for (int i = 0; i < dp.capacity(); i++) {
                doubleList.add(dp.get(i));
            }
        }
        return doubleList;
    }
}

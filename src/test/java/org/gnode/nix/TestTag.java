package org.gnode.nix;

import org.gnode.nix.valid.Result;
import org.gnode.nix.valid.Validator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class TestTag {

    private File file;
    private Section section;
    private Block block;
    private Tag tag, tag_other, tag_null;
    private List<DataArray> refs = new ArrayList<DataArray>();

    private Date statup_time;

    @Before
    public void setUp() {
        // precision of time_t is in seconds hence (millis / 1000) * 1000
        statup_time = new Date((System.currentTimeMillis() / 1000) * 1000);

        file = File.open("test_Tag_" + UUID.randomUUID().toString() + ".h5", FileMode.Overwrite);
        block = file.createBlock("block", "dataset");

        List<String> array_names = Arrays.asList("data_array_a", "data_array_b", "data_array_c",
                "data_array_d", "data_array_e");
        refs.clear();
        for (String name : array_names) {
            refs.add(block.createDataArray(name, "reference",
                    DataType.Double, new NDSize(new int[]{0})));
        }

        tag = block.createTag("tag_one", "test_tag", new double[]{0.0, 2.0, 3.4});
        tag_other = block.createTag("tag_two", "test_tag", new double[]{0.0, 2.0, 3.4});
        tag_null = null;

        section = file.createSection("foo_section", "metadata");
    }

    @After
    public void tearDown() {
        String location = file.getLocation();

        file.deleteBlock(block.getId());
        file.deleteSection(section.getId());
        file.close();

        // delete file
        java.io.File f = new java.io.File(location);
        f.delete();
    }

    @Test
    public void testValidate() {
        Result result = Validator.validate(tag);
        assertTrue(result.getErrors().size() == 0);
        assertTrue(result.getWarnings().size() == 0);
    }

    @Test
    public void testId() {
        assertEquals(tag.getId().length(), 36);
    }

    @Test
    public void testName() {
        assertEquals(tag.getName(), "tag_one");
    }

    @Test
    public void testType() {
        assertEquals(tag.getType(), "test_tag");
    }

    @Test
    public void testDefinition() {
        String def = "some_str";
        tag.setDefinition(def);
        assertEquals(tag.getDefinition(), def);
        tag.setDefinition(null);
        assertNull(tag.getDefinition());
    }

    @Test
    public void testCreateRemove() {
        List<String> ids = new ArrayList<String>();
        long count = block.getTagCount();
        String[] names = new String[]{"tag_a", "tag_b", "tag_c", "tag_d", "tag_e"};

        for (int i = 0; i < 5; i++) {
            String type = "Event";
            Tag st1 = block.createTag(names[i], type, new double[]{0.0, 2.0, 3.4});
            st1.setReferences(refs);
            Tag st2 = block.getTag(st1.getId());
            ids.add(st1.getId());

            assertEquals(st1.getId().compareTo(st2.getId()), 0);
        }

        assertEquals(block.getTagCount(), count + 5);

        for (DataArray da : refs) {
            block.deleteDataArray(da.getId());
        }
        for (String id : ids) {
            block.deleteTag(id);
        }

        assertEquals(block.getTagCount(), count);
    }

    @Test
    public void testExtent() {
        Tag st = block.createTag("TestTag1", "Tag", new double[]{0.0, 2.0, 3.4});
        st.setReferences(refs);

        double[] extent = {1.0, 2.0, 3.0};
        st.setExtent(extent);

        List<Double> retrieved = st.getExtent();
        assertEquals(retrieved.size(), extent.length);
        for (int i = 0; i < retrieved.size(); i++) {
            assertTrue(retrieved.get(i) == extent[i]);
        }

        st.setExtent(null);
        assertEquals(st.getExtent().size(), 0);
        for (DataArray da : refs) {
            block.deleteDataArray(da.getId());
        }
        block.deleteTag(st.getId());
    }

    @Test
    public void testPosition() {
        Tag st = block.createTag("TestTag1", "Tag", new double[]{0.0, 2.0, 3.4});
        st.setReferences(refs);

        double[] position = {1.0, 2.0, 3.0};
        double[] new_position = {2.0};

        st.setPosition(position);

        List<Double> retrieved = st.getPosition();
        assertEquals(retrieved.size(), position.length);
        for (int i = 0; i < retrieved.size(); i++) {
            assertTrue(retrieved.get(i) == position[i]);
        }

        st.setPosition(new_position);
        retrieved = st.getPosition();
        assertEquals(retrieved.size(), new_position.length);
        for (int i = 0; i < retrieved.size(); i++) {
            assertTrue(retrieved.get(i) == new_position[i]);
        }

        for (DataArray da : refs) {
            block.deleteDataArray(da.getId());
        }
        block.deleteTag(st.getId());
    }

    @Test
    public void testMetadataAccess() {
        assertNull(tag.getMetadata());

        tag.setMetadata(section);
        assertNotNull(tag.getMetadata());

        assertNotNull(tag.getMetadata().getId(), section.getId());

        // test none-unsetter
        tag.removeMetadata();
        assertNull(tag.getMetadata());
        // test deleter removing link too
        tag.setMetadata(section);
        file.deleteSection(section.getId());
        assertNull(tag.getMetadata());
        // re-create section
        section = file.createSection("foo_section", "metadata");
    }

    @Test
    public void testSourceAccess() {
        List<String> names = Arrays.asList("source_a", "source_b", "source_c", "source_d", "source_e");
        assertEquals(tag.getSourceCount(), 0);
        assertEquals(tag.getSources().size(), 0);

        List<String> ids = new ArrayList<String>();
        for (String name : names) {
            Source child_source = block.createSource(name, "channel");
            tag.addSource(child_source);
            assertEquals(child_source.getName(), name);
            ids.add(child_source.getId());
        }

        assertEquals(tag.getSourceCount(), names.size());
        assertEquals(tag.getSources().size(), names.size());

        String name = names.get(0);
        Source source = tag.getSource(name);
        assertEquals(source.getName(), name);

        for (String id : ids) {
            Source child_source = tag.getSource(id);
            assertTrue(tag.hasSource(id));
            assertEquals(child_source.getId(), id);

            tag.removeSource(id);
            block.deleteSource(id);
        }

        assertEquals(tag.getSourceCount(), 0);
    }

    @Test
    public void testUnits() {
        Tag st = block.createTag("TestTag1", "Tag", new double[]{0.0, 2.0, 3.4});
        st.setReferences(refs);

        List<String> valid_units = Arrays.asList("mV", "cm", "m^2");
        List<String> invalid_units = Arrays.asList("mV", "haha", "qm^2");
        List<String> insane_units = Arrays.asList("muV ", " muS");

        try {
            st.setUnits(valid_units);
        } catch (Exception e) {
            fail();
        }

        assertEquals(st.getUnits().size(), valid_units.size());
        List<String> retrieved_units = st.getUnits();
        for (int i = 0; i < retrieved_units.size(); i++) {
            assertEquals(retrieved_units.get(i), valid_units.get(i));
        }

        st.setUnits(null);
        assertEquals(st.getUnits().size(), 0);

        try {
            st.setUnits(invalid_units);
            fail();
        } catch (RuntimeException re) {
        }

        assertEquals(st.getUnits().size(), 0);
        for (DataArray da : refs) {
            block.deleteDataArray(da.getId());
        }

        st.setUnits(insane_units);
        retrieved_units = st.getUnits();
        assertTrue(retrieved_units.size() == 2);
        assertEquals(retrieved_units.get(0), "uV");
        assertEquals(retrieved_units.get(1), "uS");

        block.deleteTag(st.getId());
    }

    @Test
    public void testReferences() {
        assertEquals(tag.getReferenceCount(), 0);
        for (int i = 0; i < refs.size(); ++i) {
            assertTrue(!tag.hasReference(refs.get(i)));
            try {
                tag.addReference(refs.get(i));
            } catch (Exception e) {
                fail();
            }
            assertTrue(tag.hasReference(refs.get(i)));
        }
        assertTrue(tag.getReferenceCount() == refs.size());
        for (int i = 0; i < refs.size(); ++i) {
            try {
                tag.removeReference(refs.get(i));
            } catch (Exception e) {
                fail();
            }
        }
        assertEquals(tag.getReferenceCount(), 0);
        DataArray a = null;
        try {
            tag.hasReference(a);
            fail();
        } catch (RuntimeException re) {
        }

        try {
            tag.addReference(a);
            fail();
        } catch (RuntimeException re) {
        }

        try {
            tag.removeReference(a);
            fail();
        } catch (RuntimeException re) {
        }
    }

    @Test
    public void testFeatures() {
        DataArray a = null;
        Feature f = null;
        assertEquals(tag.getFeatureCount(), 0);
        try {
            tag.hasFeature(f);
            fail();
        } catch (RuntimeException re) {
        }

        try {
            tag.deleteFeature(f);
            fail();
        } catch (RuntimeException re) {
        }

        try {
            tag.createFeature(a, LinkType.Indexed);
            fail();
        } catch (RuntimeException re) {
        }

        try {
            f = tag.createFeature(refs.get(0), LinkType.Indexed);
        } catch (Exception e) {
            fail();
        }

        assertTrue(tag.getFeatureCount() == 1);
        try {
            tag.deleteFeature(f);
        } catch (Exception e) {
            fail();
        }
        assertTrue(tag.getFeatureCount() == 0);
    }

    @Test
    public void testDataAccess() {
        double samplingInterval = 1.0;
        double[] ticks = {1.2, 2.3, 3.4, 4.5, 6.7};
        String unit = "ms";
        SampledDimension sampledDim;
        RangeDimension rangeDim;
        SetDimension setDim;
        double[] position = {0.0, 2.0, 3.4};
        double[] extent = {0.0, 6.0, 2.3};
        List<String> units = Arrays.asList("none", "ms", "ms");

        DataArray data_array = block.createDataArray("dimensionTest",
                "test",
                DataType.Double,
                new NDSize(new int[]{0, 0, 0}));
        List<DataArray> reference = new ArrayList<DataArray>();
        reference.add(data_array);

        int[] data = new int[2 * 10 * 5];
        int value;
        for (int i = 0; i != 2; ++i) {
            value = 0;
            for (int j = 0; j != 10; ++j) {
                for (int k = 0; k != 5; ++k) {
                    data[i * 10 * 5 + j * 5 + k] = value++;
                }
            }
        }
        data_array.setDataExtent(new NDSize(new int[]{2, 10, 5}));
        data_array.setData(data, data_array.getDataExtent(), new NDSize());

        setDim = data_array.appendSetDimension();
        List<String> labels = Arrays.asList("label_a", "label_b");
        setDim.setLabels(labels);

        sampledDim = data_array.appendSampledDimension(samplingInterval);
        sampledDim.setUnit(unit);

        rangeDim = data_array.appendRangeDimension(ticks);
        rangeDim.setUnit(unit);

        Tag position_tag = block.createTag("position tag", "event", position);
        position_tag.setReferences(reference);
        position_tag.setUnits(units);

        Tag segment_tag = block.createTag("region tag", "segment", position);
        segment_tag.setReferences(reference);
        segment_tag.setExtent(extent);
        segment_tag.setUnits(units);

        DataView retrieved_data = position_tag.retrieveData(0);
        NDSize data_size = retrieved_data.getDataExtent();
        assertEquals(data_size.getSize(), 3);
        int[] data_size_arr = data_size.getData();
        assertTrue(data_size_arr[0] == 1 && data_size_arr[1] == 1 && data_size_arr[2] == 1);

        retrieved_data = segment_tag.retrieveData(0);
        data_size = retrieved_data.getDataExtent();
        assertEquals(data_size.getSize(), 3);
        data_size_arr = data_size.getData();
        assertTrue(data_size_arr[0] == 1 && data_size_arr[1] == 6 && data_size_arr[2] == 2);

        block.deleteTag(position_tag);
        block.deleteTag(segment_tag);
    }

    @Test
    public void testCreatedAt() {
        assertTrue(tag.getCreatedAt().compareTo(statup_time) >= 0);

        long time = System.currentTimeMillis() - 10000000L * 1000;
        // precision of time_t is in seconds hence (millis / 1000) * 1000
        time = time / 1000 * 1000;

        Date past_time = new Date(time);
        tag.forceCreatedAt(past_time);
        assertTrue(tag.getCreatedAt().equals(past_time));
    }

    @Test
    public void testUpdatedAt() {
        assertTrue(tag.getUpdatedAt().compareTo(statup_time) >= 0);
    }

}
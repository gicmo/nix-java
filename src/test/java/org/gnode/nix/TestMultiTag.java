package org.gnode.nix;

import org.gnode.nix.valid.Result;
import org.gnode.nix.valid.Validator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class TestMultiTag {

    private File file;
    private Block block;
    private DataArray positions, extents;
    private MultiTag tag, tag_other, tag_null;
    private Section section;

    private Date statup_time;

    @Before
    public void setUp() {
        // precision of time_t is in seconds hence (millis / 1000) * 1000
        statup_time = new Date((System.currentTimeMillis() / 1000) * 1000);

        file = File.open("test_multiTag.h5", FileMode.Overwrite);
        block = file.createBlock("block", "dataset");

        positions = block.createDataArray("positions_DataArray", "dataArray",
                DataType.Double, new NDSize(new int[]{0, 0}));
        extents = block.createDataArray("extents_DataArray", "dataArray",
                DataType.Double, new NDSize(new int[]{0, 0}));

        double[] A = new double[5 * 5];
        for (int i = 0; i < 5; i++) {
            A[i * 5 + i] = 100.0 * i;
        }
        positions.setDataExtent(new NDSize(new int[]{5, 5}));
        positions.setData(A, new NDSize(new int[]{5, 5}), new NDSize());

        double[] B = new double[5 * 5];
        for (int i = 0; i < 5; i++) {
            B[i * 5 + i] = 100.0 * i;
        }
        extents.setDataExtent(new NDSize(new int[]{5, 5}));
        extents.setData(B, new NDSize(new int[]{5, 5}), new NDSize());

        tag = block.createMultiTag("tag_one", "test_tag", positions);
        tag_other = block.createMultiTag("tag_two", "test_tag", positions);
        tag_null = null;

        section = file.createSection("foo_section", "metadata");
    }

    @After
    public void tearDown() {
        file.deleteBlock(block.getId());
        file.deleteSection(section.getId());
        file.close();
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
        long count = block.getMultiTagCount();
        String[] names = {"tag_a", "tag_b", "tag_c", "tag_d", "tag_e"};
        for (int i = 0; i < 5; i++) {
            String type = "Event";
            MultiTag dt1 = block.createMultiTag(names[i], type, positions);
            MultiTag dt2 = block.getMultiTag(dt1.getId());
            ids.add(dt1.getId());

            assertTrue(dt1.getId().compareTo(dt2.getId()) == 0);
        }

        assertTrue(block.getMultiTagCount() == (count + 5));

        for (int i = 0; i < ids.size(); i++) {
            block.deleteMultiTag(ids.get(i));
        }

        assertTrue(block.getMultiTagCount() == count);

        DataArray a = null;
        MultiTag mtag = null;

        try {
            mtag = block.createMultiTag("test", "test", a);
            fail();
        } catch (RuntimeException re) {
        }

        mtag = block.createMultiTag("test", "test", positions);
        mtag.setExtents(positions);

        try {
            mtag.setPositions(a);
            fail();
        } catch (RuntimeException re) {
        }

        assertEquals(mtag.getExtents().getId(), positions.getId());

        try {
            mtag.removeExtents();
        } catch (Exception e) {
            fail();
        }
        assertNull(mtag.getExtents());
    }

    @Test
    public void testUnits() {
        MultiTag dt = block.createMultiTag("TestMultiTag1", "Tag", positions);

        List<String> valid_units = Arrays.asList("mV", "cm", "m^2");
        List<String> invalid_units = Arrays.asList("mV", "haha", "qm^2");
        List<String> insane_units = Arrays.asList("muV ", " muS");

        try {
            dt.setUnits(valid_units);
        } catch (Exception e) {
            fail();
        }

        assertTrue(dt.getUnits().size() == valid_units.size());
        List<String> retrieved_units = dt.getUnits();
        for (int i = 0; i < retrieved_units.size(); i++) {
            assertEquals(retrieved_units.get(i), valid_units.get(i));
        }

        dt.setUnits(null);
        assertTrue(dt.getUnits().size() == 0);

        try {
            dt.setUnits(invalid_units);
            fail();
        } catch (RuntimeException re) {
        }

        assertTrue(dt.getUnits().size() == 0);

        dt.setUnits(insane_units);
        retrieved_units = dt.getUnits();
        assertTrue(retrieved_units.size() == 2);
        assertEquals(retrieved_units.get(0), "uV");
        assertEquals(retrieved_units.get(1), "uS");

        block.deleteMultiTag(dt.getId());
    }

    @Test
    public void testReferences() {
        DataArray da_1 = block.createDataArray("TestReference 1",
                "Reference",
                DataType.Double,
                new NDSize(new int[]{0}));
        DataArray da_2 = block.createDataArray("TestReference 2", "Reference",
                DataType.Double,
                new NDSize(new int[]{0}));
        DataArray a = null;
        MultiTag dt = block.createMultiTag("TestMultiTag1", "Tag", positions);

        try {
            dt.getReference(42);
            fail();
        } catch (RuntimeException re) {
        }

        try {
            dt.hasReference(a);
            fail();
        } catch (RuntimeException re) {
        }

        assertTrue(dt.getReferenceCount() == 0);

        dt.addReference(da_1);
        dt.addReference(da_2);

        try {
            dt.addReference(a);
            fail();
        } catch (RuntimeException re) {
        }

        assertTrue(dt.getReferenceCount() == 2);
        assertTrue(dt.hasReference(da_1));
        assertTrue(dt.hasReference(da_2));

        assertTrue(dt.hasReference(da_1.getId()));
        assertTrue(dt.hasReference(da_1.getName()));

        DataArray ref1 = dt.getReference(da_1.getId());

        assertEquals(ref1.getId(), da_1.getId());
        DataArray ref2 = dt.getReference(da_1.getName());

        assertEquals(ref2.getId(), da_1.getId());

        List<DataArray> arrays = dt.getReferences();
        assertTrue(arrays.size() == 2);

        assertTrue(dt.hasReference(da_1.getId()));
        assertTrue(dt.hasReference(da_2.getId()));

        dt.removeReference(da_1.getId());
        assertTrue(dt.getReferenceCount() == 1);
        dt.removeReference("NONEXISTENT");
        assertTrue(dt.getReferenceCount() == 1);
        dt.removeReference(da_2.getName());
        assertTrue(dt.getReferenceCount() == 0);
        dt.addReference(da_1);
        assertTrue(dt.getReferenceCount() == 1);

        try {
            dt.removeReference(da_1);
        } catch (Exception e) {
            fail();
        }

        assertTrue(dt.getReferenceCount() == 0);

        // delete data arrays
        List<String> ids = Arrays.asList(da_1.getId(), da_2.getId());
        block.deleteDataArray(da_1.getId());
        block.deleteDataArray(da_2.getId());
        // check if references are gone too!
        assertTrue(dt.getReferenceCount() == 0);
        assertTrue(!dt.hasReference(ids.get(0)));
        assertTrue(!dt.hasReference(ids.get(1)));
        block.deleteMultiTag(dt.getId());
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
            f = tag.createFeature(positions, LinkType.Indexed);
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
    public void testExtents() {

        try {
            tag.setExtents("wrong_data_array_id");
            fail();
        } catch (RuntimeException re) {
        }

        tag.setPositions(positions);
        tag.setExtents(extents);
        assertNotNull(tag.getExtents());
        tag.removeExtents();
        assertNull(tag.getExtents());
    }


    @Test
    public void testPositions() {
        try {
            tag.setPositions("wrong_data_array_id");
            fail();
        } catch (RuntimeException re) {
        }


        tag.setPositions(positions);
        assertEquals(tag.getPositions().getId(), positions.getId());
        block.deleteDataArray(positions.getId());
        // make sure link is gone with data array
        try {
            tag.getPositions();
            fail();
        } catch (RuntimeException re) {

        }

        // re-create positions
        positions = block.createDataArray("positions_DataArray", "dataArray",
                DataType.Double, new NDSize(new int[]{0, 0}));

    }

    @Test
    public void testPositionExtents() {
        tag.setExtents(extents);
        assertEquals(tag.getExtents().getId(), extents.getId());
        block.deleteDataArray(extents.getId());
        // make sure that link is gone with data array
        assertNull(tag.getExtents());
        // re-create extents
        extents = block.createDataArray("extents_DataArray", "dataArray",
                DataType.Double, new NDSize(new int[]{0, 0}));

        double[] B = new double[5 * 5];
        for (int i = 0; i < 5; ++i) {
            B[i * 5 + i] = 100.0 * i;
        }
        extents.setDataExtent(new NDSize(new int[]{5, 5}));
        extents.setData(B, extents.getDataExtent(), new NDSize());

        double[] A = new double[10 * 10];
        for (int i = 0; i < 10; ++i) {
            A[i * 10 + i] = 100.0 * i;
        }
        positions.setDataExtent(new NDSize(new int[]{10, 10}));
        positions.setData(A, positions.getDataExtent(), new NDSize());

        tag.setPositions(positions);
        try {
            tag.setExtents(extents);
            fail();
        } catch (RuntimeException re) {
        }

        tag.removeExtents();
        assertNull(tag.getExtents());
    }

    @Test
    public void testDataAccess() {
        DataArray data_array = block.createDataArray("dimensionTest",
                "test",
                DataType.Double,
                new NDSize(new int[]{0, 0, 0}));
        double samplingInterval = 1.0;
        double[] ticks = {1.2, 2.3, 3.4, 4.5, 6.7};
        String unit = "ms";

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

        SetDimension setDim = data_array.appendSetDimension();
        List<String> labels = Arrays.asList("label_a", "label_b");
        setDim.setLabels(labels);

        SampledDimension sampledDim = data_array.appendSampledDimension(samplingInterval);
        sampledDim.setUnit(unit);

        RangeDimension rangeDim = data_array.appendRangeDimension(ticks);
        rangeDim.setUnit(unit);

        double[] event_positions = {0.0, 3.0, 3.4, 0.0, 8.0, 2.3};
        double[] event_extents = {0.0, 6.0, 2.3, 0.0, 3.0, 2.0};

        List<String> event_labels = Arrays.asList("event 1", "event 2");
        List<String> dim_labels = Arrays.asList("dim 0", "dim 1", "dim 2");

        DataArray event_array = block.createDataArray("positions", "test",
                DataType.Double, new NDSize(new int[]{2, 3}));
        event_array.setData(event_positions, event_array.getDataExtent(), new NDSize());

        SetDimension event_set_dim;
        event_set_dim = event_array.appendSetDimension();
        event_set_dim.setLabels(event_labels);
        event_set_dim = event_array.appendSetDimension();
        event_set_dim.setLabels(dim_labels);

        DataArray extent_array = block.createDataArray("extents", "test",
                DataType.Double, new NDSize(new int[]{2, 3}));
        extent_array.setData(event_extents, extent_array.getDataExtent(), new NDSize());

        SetDimension extent_set_dim;
        extent_set_dim = extent_array.appendSetDimension();
        extent_set_dim.setLabels(event_labels);
        extent_set_dim = extent_array.appendSetDimension();
        extent_set_dim.setLabels(dim_labels);

        MultiTag multi_tag = block.createMultiTag("multi_tag", "events", event_array);
        multi_tag.setExtents(extent_array);
        multi_tag.addReference(data_array);

        DataView ret_data = multi_tag.retrieveData(0, 0);
        NDSize data_size = ret_data.getDataExtent();
        assertEquals(data_size.getSize(), 3);
        int[] data_size_arr = data_size.getData();
        assertTrue(data_size_arr[0] == 1 && data_size_arr[1] == 6 && data_size_arr[2] == 2);

        try {
            multi_tag.retrieveData(1, 0);
            fail();
        } catch (RuntimeException re) {
        }

        block.deleteMultiTag(multi_tag);
        block.deleteDataArray(data_array);
        block.deleteDataArray(event_array);
        block.deleteDataArray(event_array);
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
        assertEquals(tag.getSources().size(), 0);
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
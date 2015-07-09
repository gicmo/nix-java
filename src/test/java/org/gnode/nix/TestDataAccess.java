package org.gnode.nix;


import org.gnode.nix.util.DataAccess;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

public class TestDataAccess {

    private File file;
    private DataArray data_array;
    private Tag position_tag, segment_tag;
    private MultiTag multi_tag;
    private Block block;
    private SampledDimension sampledDim;
    private RangeDimension rangeDim;
    private SetDimension setDim;

    @Before
    public void setUp() {
        file = File.open("test_DataAccess_" + UUID.randomUUID().toString() + ".h5", FileMode.Overwrite);
        block = file.createBlock("dimensionTest", "test");
        data_array = block.createDataArray("dimensionTest",
                "test",
                DataType.Double,
                new NDSize(new int[]{2, 10, 5}));
        double samplingInterval = 1.0;
        double[] ticks = {1.2, 2.3, 3.4, 4.5, 6.7};
        String unit = "ms";

        double[] data = new double[2 * 10 * 5];
        for (int i = 0; i != 2; ++i) {
            int value = 0;
            for (int j = 0; j != 10; ++j) {
                for (int k = 0; k != 5; ++k) {
                    data[i * 10 * 5 + j * 5 + k] = value++;
                }
            }
        }
        data_array.setData(data, new NDSize(new int[]{2, 10, 5}), new NDSize());

        setDim = data_array.appendSetDimension();
        List<String> labels = Arrays.asList("label_a", "label_b");
        setDim.setLabels(labels);

        sampledDim = data_array.appendSampledDimension(samplingInterval);
        sampledDim.setUnit(unit);

        rangeDim = data_array.appendRangeDimension(ticks);
        rangeDim.setUnit(unit);

        List<DataArray> refs = new ArrayList<DataArray>();
        refs.add(data_array);
        double[] position = {0.0, 2.0, 3.4};
        double[] extent = {0.0, 6.0, 2.3};
        List<String> units = Arrays.asList("none", "ms", "ms");

        position_tag = block.createTag("position tag", "event", position);
        position_tag.setReferences(refs);
        position_tag.setUnits(units);

        segment_tag = block.createTag("region tag", "segment", position);
        segment_tag.setReferences(refs);
        segment_tag.setExtent(extent);
        segment_tag.setUnits(units);

        // setup multitag
        double[] event_positions = {0.0, 3.0, 3.4, 0.0, 8.0, 2.3};
        double[] event_extents = {0.0, 6.0, 2.3, 0.0, 3.0, 2.0};

        List<String> event_labels = Arrays.asList("event 1", "event 2");
        List<String> dim_labels = Arrays.asList("dim 0", "dim 1", "dim 2");

        DataArray event_array = block.createDataArray("positions", "test",
                DataType.Double, new NDSize(new int[]{2, 3}));
        event_array.setData(event_positions, new NDSize(new int[]{2, 3}), new NDSize());

        SetDimension event_set_dim;
        event_set_dim = event_array.appendSetDimension();
        event_set_dim.setLabels(event_labels);
        event_set_dim = event_array.appendSetDimension();
        event_set_dim.setLabels(dim_labels);

        DataArray extent_array = block.createDataArray("extents", "test",
                DataType.Double, new NDSize(new int[]{2, 3}));
        extent_array.setData(event_extents, new NDSize(new int[]{2, 3}), new NDSize());

        SetDimension extent_set_dim;
        extent_set_dim = extent_array.appendSetDimension();
        extent_set_dim.setLabels(event_labels);
        extent_set_dim = extent_array.appendSetDimension();
        extent_set_dim.setLabels(dim_labels);

        multi_tag = block.createMultiTag("multi_tag", "events", event_array);
        multi_tag.setExtents(extent_array);
        multi_tag.addReference(data_array);
    }

    @After
    public void tearDown() {
        String location = file.getLocation();

        file.close();

        // delete file
        java.io.File f = new java.io.File(location);
        f.delete();
    }

    @Test
    public void testPositionToIndexRangeDimension() {
        String unit = "ms";
        String invalid_unit = "kV";
        String scaled_unit = "s";

        try {
            DataAccess.positionToIndex(5.0, invalid_unit, rangeDim);
            fail();
        } catch (RuntimeException re) {
        }

        assertTrue(DataAccess.positionToIndex(1.0, unit, rangeDim) == 0);
        assertTrue(DataAccess.positionToIndex(8.0, unit, rangeDim) == 4);
        assertTrue(DataAccess.positionToIndex(0.001, scaled_unit, rangeDim) == 0);
        assertTrue(DataAccess.positionToIndex(0.008, scaled_unit, rangeDim) == 4);
        assertTrue(DataAccess.positionToIndex(3.4, unit, rangeDim) == 2);
        assertTrue(DataAccess.positionToIndex(3.6, unit, rangeDim) == 2);
        assertTrue(DataAccess.positionToIndex(4.0, unit, rangeDim) == 3);
        assertTrue(DataAccess.positionToIndex(0.0036, scaled_unit, rangeDim) == 2);
    }

    @Test
    public void testPositionToIndexSampledDimension() {
        String unit = "ms";
        String invalid_unit = "kV";
        String scaled_unit = "s";

        try {
            DataAccess.positionToIndex(-1.0, unit, sampledDim);
            fail();
        } catch (RuntimeException re) {
        }

        try {
            DataAccess.positionToIndex(0.005, invalid_unit, sampledDim);
            fail();
        } catch (RuntimeException re) {
        }

        assertTrue(DataAccess.positionToIndex(5.0, unit, sampledDim) == 5);
        assertTrue(DataAccess.positionToIndex(0.005, scaled_unit, sampledDim) == 5);
    }

    @Test
    public void testPositionToIndexSetDimension() {
        String unit = "ms";

        try {
            DataAccess.positionToIndex(5.8, "none", setDim);
            fail();
        } catch (RuntimeException re) {
        }

        try {
            DataAccess.positionToIndex(0.5, unit, setDim);
            fail();
        } catch (RuntimeException re) {
        }

        try {
            DataAccess.positionToIndex(0.5, "none", setDim);
        } catch (Exception e) {
            fail();
        }

        assertTrue(DataAccess.positionToIndex(0.5, "none", setDim) == 1);
        assertTrue(DataAccess.positionToIndex(0.45, "none", setDim) == 0);
    }

    @Test
    public void testOffsetAndCount() {
        NDSize offsets = new NDSize(), counts = new NDSize();
        DataAccess.getOffsetAndCount(position_tag, data_array, offsets, counts);

        assertTrue(offsets.getSize() == 3);
        assertTrue(counts.getSize() == 3);
        int[] offsets_data = offsets.getData();
        int[] counts_data = counts.getData();
        assertTrue(offsets_data[0] == 0 && offsets_data[1] == 2 && offsets_data[2] == 2);
        assertTrue(counts_data[0] == 1 && counts_data[1] == 1 && counts_data[2] == 1);

        position_tag.setUnits(new ArrayList<String>());
        DataAccess.getOffsetAndCount(position_tag, data_array, offsets, counts);

        assertTrue(position_tag.getUnits().size() == 0);
        assertTrue(offsets.getSize() == 3);
        assertTrue(counts.getSize() == 3);
        offsets_data = offsets.getData();
        counts_data = counts.getData();
        assertTrue(offsets_data[0] == 0 && offsets_data[1] == 2 && offsets_data[2] == 2);
        assertTrue(counts_data[0] == 1 && counts_data[1] == 1 && counts_data[2] == 1);

        DataAccess.getOffsetAndCount(segment_tag, data_array, offsets, counts);
        assertTrue(offsets.getSize() == 3);
        assertTrue(counts.getSize() == 3);
        offsets_data = offsets.getData();
        counts_data = counts.getData();
        assertTrue(offsets_data[0] == 0 && offsets_data[1] == 2 && offsets_data[2] == 2);
        assertTrue(counts_data[0] == 1 && counts_data[1] == 6 && counts_data[2] == 2);

        segment_tag.setUnits(new ArrayList<String>());
        DataAccess.getOffsetAndCount(segment_tag, data_array, offsets, counts);
        assertTrue(offsets.getSize() == 3);
        assertTrue(counts.getSize() == 3);
        offsets_data = offsets.getData();
        counts_data = counts.getData();
        assertTrue(offsets_data[0] == 0 && offsets_data[1] == 2 && offsets_data[2] == 2);
        assertTrue(counts_data[0] == 1 && counts_data[1] == 6 && counts_data[2] == 2);

        try {
            DataAccess.getOffsetAndCount(multi_tag, data_array, -1, offsets, counts);
            fail();
        } catch (RuntimeException re) {
        }

        try {
            DataAccess.getOffsetAndCount(multi_tag, data_array, 3, offsets, counts);
            fail();
        } catch (RuntimeException re) {
        }

        DataAccess.getOffsetAndCount(multi_tag, data_array, 0, offsets, counts);
        assertTrue(offsets.getSize() == 3);
        assertTrue(counts.getSize() == 3);
        offsets_data = offsets.getData();
        counts_data = counts.getData();
        assertTrue(offsets_data[0] == 0 && offsets_data[1] == 3 && offsets_data[2] == 2);
        assertTrue(counts_data[0] == 1 && counts_data[1] == 6 && counts_data[2] == 2);

        DataAccess.getOffsetAndCount(multi_tag, data_array, 1, offsets, counts);
        assertTrue(offsets.getSize() == 3);
        assertTrue(counts.getSize() == 3);
        offsets_data = offsets.getData();
        counts_data = counts.getData();
        assertTrue(offsets_data[0] == 0 && offsets_data[1] == 8 && offsets_data[2] == 1);
        assertTrue(counts_data[0] == 1 && counts_data[1] == 3 && counts_data[2] == 2);
    }

    @Test
    public void testPositionInData() {
        NDSize offsets = new NDSize(), counts = new NDSize();
        DataAccess.getOffsetAndCount(multi_tag, data_array, 0, offsets, counts);
        assertTrue(DataAccess.positionInData(data_array, offsets));
        assertTrue(DataAccess.positionAndExtentInData(data_array, offsets, counts));

        DataAccess.getOffsetAndCount(multi_tag, data_array, 1, offsets, counts);
        assertTrue(DataAccess.positionInData(data_array, offsets));
        assertTrue(!DataAccess.positionAndExtentInData(data_array, offsets, counts));
    }

    @Test
    public void testRetrieveData() {
        try {
            DataAccess.retrieveData(multi_tag, 0, -1);
            fail();
        } catch (RuntimeException re) {
        }

        try {
            DataAccess.retrieveData(multi_tag, 0, 1);
            fail();
        } catch (RuntimeException re) {
        }

        try {
            DataAccess.retrieveData(multi_tag, -1, 0);
            fail();
        } catch (RuntimeException re) {
        }

        try {
            DataAccess.retrieveData(multi_tag, 10, 0);
            fail();
        } catch (RuntimeException re) {
        }

        DataView data_view = DataAccess.retrieveData(multi_tag, 0, 0);
        NDSize data_size = data_view.getDataExtent();
        assertTrue(data_size.getSize() == 3);
        int[] data_size_arr = data_size.getData();
        assertTrue(data_size_arr[0] == 1 && data_size_arr[1] == 6 && data_size_arr[2] == 2);

        try {
            DataAccess.retrieveData(multi_tag, 1, 0);
            fail();
        } catch (RuntimeException re) {
        }

        data_view = DataAccess.retrieveData(position_tag, 0);
        data_size = data_view.getDataExtent();
        data_size_arr = data_size.getData();
        assertTrue(data_size.getSize() == 3);
        assertTrue(data_size_arr[0] == 1 && data_size_arr[1] == 1 && data_size_arr[2] == 1);

        data_view = DataAccess.retrieveData(segment_tag, 0);
        data_size = data_view.getDataExtent();
        data_size_arr = data_size.getData();
        assertTrue(data_size.getSize() == 3);
        assertTrue(data_size_arr[0] == 1 && data_size_arr[1] == 6 && data_size_arr[2] == 2);
    }

    @Test
    public void testTagFeatureData() {
        DataArray number_feat = block.createDataArray("number feature", "test", DataType.Double, new NDSize(new int[]{1}));
        double[] number = {10.0};
        number_feat.setData(number, new NDSize(new int[]{1}), new NDSize());

        DataArray ramp_feat = block.createDataArray("ramp feature", "test", DataType.Double, new NDSize(new int[]{10}));
        ramp_feat.setLabel("voltage");
        ramp_feat.setUnit("mV");
        SampledDimension dim = ramp_feat.appendSampledDimension(1.0);
        dim.setUnit("ms");
        double[] ramp_data = {0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0};
        ramp_feat.setData(ramp_data, new NDSize(new int[]{10}), new NDSize());

        Tag pos_tag = block.createTag("feature test", "test", new double[]{5.0});
        pos_tag.setUnits(Arrays.asList("ms"));

        Feature f1 = pos_tag.createFeature(number_feat, LinkType.Untagged);
        Feature f2 = pos_tag.createFeature(ramp_feat, LinkType.Tagged);
        Feature f3 = pos_tag.createFeature(ramp_feat, LinkType.Untagged);

        DataView data1 = DataAccess.retrieveFeatureData(pos_tag, 0);
        DataView data2 = DataAccess.retrieveFeatureData(pos_tag, 1);
        DataView data3 = DataAccess.retrieveFeatureData(pos_tag, 2);

        assertTrue(pos_tag.getFeatureCount() == 3);
        assertTrue(data1.getDataExtent().getElementsProduct() == 1);
        assertTrue(data2.getDataExtent().getElementsProduct() == 1);
        assertTrue(data3.getDataExtent().getElementsProduct() == ramp_data.length);

        pos_tag.setExtent(new double[]{2.0});
        data1 = DataAccess.retrieveFeatureData(pos_tag, 0);
        data2 = DataAccess.retrieveFeatureData(pos_tag, 1);
        data3 = DataAccess.retrieveFeatureData(pos_tag, 2);

        assertTrue(data1.getDataExtent().getElementsProduct() == 1);
        assertTrue(data2.getDataExtent().getElementsProduct() == 2);
        assertTrue(data3.getDataExtent().getElementsProduct() == ramp_data.length);

        pos_tag.deleteFeature(f1.getId());
        pos_tag.deleteFeature(f2.getId());
        pos_tag.deleteFeature(f3.getId());
        block.deleteDataArray(number_feat.getId());
        block.deleteDataArray(ramp_feat.getId());
        block.deleteTag(pos_tag);
    }

    @Test
    public void testMultiTagFeatureData() {
        DataArray index_data = block.createDataArray("indexed feature data", "test", DataType.Double, new NDSize(new int[]{10, 10}));
        SampledDimension dim1 = index_data.appendSampledDimension(1.0);
        dim1.setUnit("ms");
        SampledDimension dim2 = index_data.appendSampledDimension(1.0);
        dim2.setUnit("ms");

        double[] data1 = new double[10 * 10];
        double total = 0.0;
        for (int i = 0; i != 10; ++i) {
            int value = 100 * i;
            for (int j = 0; j != 10; ++j) {
                data1[i * 10 + j] = value++;
                total += data1[i * 10 + j];
            }
        }
        index_data.setData(data1, new NDSize(new int[]{10, 10}), new NDSize());

        DataArray tagged_data = block.createDataArray("tagged feature data", "test", DataType.Double, new NDSize(new int[]{2, 20, 10}));
        dim1 = tagged_data.appendSampledDimension(1.0);
        dim1.setUnit("ms");
        dim2 = tagged_data.appendSampledDimension(1.0);
        dim2.setUnit("ms");
        SampledDimension dim3;
        dim3 = tagged_data.appendSampledDimension(1.0);
        dim3.setUnit("ms");

        double[] data2 = new double[2 * 20 * 10];
        for (int i = 0; i != 2; ++i) {
            int value = 100 * i;
            for (int j = 0; j != 20; ++j) {
                for (int k = 0; k != 10; ++k) {
                    data2[i * 20 * 10 + j * 10 + k] = value++;
                }
            }
        }
        tagged_data.setData(data2, new NDSize(new int[]{2, 20, 10}), new NDSize());

        Feature index_feature = multi_tag.createFeature(index_data, LinkType.Indexed);
        Feature tagged_feature = multi_tag.createFeature(tagged_data, LinkType.Tagged);
        Feature untagged_feature = multi_tag.createFeature(index_data, LinkType.Untagged);

        // preparations done, actually test
        assertTrue(multi_tag.getFeatureCount() == 3);
        // indexed feature
        DataView data_view = DataAccess.retrieveFeatureData(multi_tag, 0, 0);
        NDSize data_size = data_view.getDataExtent();

        assertTrue(data_size.getSize() == 2);
        assertTrue(data_size.getElementsProduct() == 10);
        double sum = 0.;
        double temp;

        NDSize offset = new NDSize(data_view.getDataExtent().getSize(), 0);

        // tagged feature
        data_view = DataAccess.retrieveFeatureData(multi_tag, 0, 1);
        data_size = data_view.getDataExtent();
        assertTrue(data_size.getSize() == 3);

        data_view = DataAccess.retrieveFeatureData(multi_tag, 1, 1);
        data_size = data_view.getDataExtent();
        assertTrue(data_size.getSize() == 3);

        try {
            DataAccess.retrieveFeatureData(multi_tag, 2, 1);
            fail();
        } catch (RuntimeException re) {
        }

        try {
            DataAccess.retrieveFeatureData(multi_tag, 2, 3);
            fail();
        } catch (RuntimeException re) {
        }

        // clean up
        multi_tag.deleteFeature(index_feature.getId());
        multi_tag.deleteFeature(tagged_feature.getId());
        multi_tag.deleteFeature(untagged_feature.getId());
        block.deleteDataArray(tagged_data.getId());
        block.deleteDataArray(index_data.getId());
    }

    @Test
    public void testMultiTagUnitSupport() {
        List<String> valid_units = Arrays.asList("none", "ms", "s");
        List<String> invalid_units = Arrays.asList("mV", "Ohm", "muV");

        MultiTag testTag = block.createMultiTag("test", "testTag", multi_tag.getPositions());
        testTag.setUnits(valid_units);
        testTag.addReference(data_array);

        try {
            DataAccess.retrieveData(testTag, 0, 0);
        } catch (Exception e) {
            fail();
        }

        testTag.setUnits(null);

        try {
            DataAccess.retrieveData(testTag, 0, 0);
        } catch (Exception e) {
            fail();
        }

        testTag.setUnits(invalid_units);

        try {
            DataAccess.retrieveData(testTag, 0, 0);
            fail();
        } catch (RuntimeException re) {
        }
    }

    @Test
    public void testDataView() {

        NDSize zcount = new NDSize(new int[]{2, 5, 2});
        NDSize zoffset = new NDSize(new int[]{0, 5, 2});

        DataView io = new DataView(data_array, zcount, zoffset);

        assertEquals(zcount, io.getDataExtent());
        assertEquals(data_array.getDataType(), io.getDataType());
    }
}

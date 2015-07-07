package org.gnode.nix;

import org.gnode.nix.valid.Result;
import org.gnode.nix.valid.Validator;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class TestDimension {

    private File file;
    private Block block;
    private DataArray data_array;

    @Before
    public void setUp() {
        file = File.open("test_dimension.h5", FileMode.Overwrite);

        block = file.createBlock("dimensionTest", "test");
        data_array = block.createDataArray("dimensionTest", "Test",
                DataType.Double, new NDSize(new int[]{0}));
    }

    @After
    public void tearDown() {
        file.deleteBlock(block.getId());
        file.close();
    }

    @Test
    public void testValidate() {
        SetDimension d = data_array.appendSetDimension();

        Result result = Validator.validate(d);
        assertTrue(result.getErrors().size() == 0);
        assertTrue(result.getWarnings().size() == 0);
    }

    @Test
    public void testSetValidate() {
        SetDimension d = data_array.appendSetDimension();

        Result result = Validator.validate(d);
        assertTrue(result.getErrors().size() == 0);
        assertTrue(result.getWarnings().size() == 0);
    }

    @Test
    public void testRangeValidate() {
        double[] ticks = new double[5];
        for (int i = 0; i < 5; i++) {
            ticks[i] = Math.PI;
        }

        RangeDimension d = data_array.appendRangeDimension(ticks);

        Result result = Validator.validate(d);
        assertTrue(result.getErrors().size() == 0);
        assertTrue(result.getWarnings().size() == 0);
    }

    @Test
    public void testSampleValidate() {

        double samplingInterval = Math.PI;

        SampledDimension d = data_array.appendSampledDimension(samplingInterval);

        Result result = Validator.validate(d);
        assertTrue(result.getErrors().size() == 0);
        assertTrue(result.getWarnings().size() == 0);
    }

    @Test
    public void testIndex() {
        SetDimension sd = data_array.appendSetDimension();
        assertTrue(data_array.getDimensionCount() == 1 && sd.getIndex() == 1);
        data_array.deleteDimension(sd.getIndex());
        assertEquals(data_array.getDimensionCount(), 0);
    }

    @Test
    public void testSampledDimLabel() {
        String label = "aLabel";
        String other_label = "anotherLabel";
        double samplingInterval = Math.PI;

        SampledDimension sd = data_array.appendSampledDimension(samplingInterval);
        assertEquals(sd.getDimensionType(), DimensionType.Sample);

        sd.setLabel(label);
        assertEquals(sd.getLabel(), label);
        sd.setLabel(other_label);
        assertEquals(sd.getLabel(), other_label);

        try {
            sd.setLabel(null);
        } catch (Exception e) {
            fail();
        }

        assertNull(sd.getLabel());

        data_array.deleteDimension(sd.getIndex());
    }

    @Test
    public void testSampledDimUnit() {
        String invalidUnit = "invalidunit";
        String validUnit = "mV^2";
        double samplingInterval = Math.PI;

        SampledDimension sd = data_array.appendSampledDimension(samplingInterval);
        assertEquals(sd.getDimensionType(), DimensionType.Sample);

        try {
            sd.setUnit(invalidUnit);
            fail();
        } catch (RuntimeException re) {
        }

        try {
            sd.setUnit(validUnit);
        } catch (Exception e) {
            fail();
        }

        assertEquals(sd.getUnit(), validUnit);

        try {
            sd.setUnit(null);
        } catch (Exception e) {
            fail();
        }

        assertNull(sd.getUnit());
        data_array.deleteDimension(sd.getIndex());
    }

    @Test
    public void testSampledDimSamplingInterval() {
        double impossible_sampling_interval = -1.0;
        double invalid_sampling_interval = 0.0;
        double samplingInterval = Math.PI;

        SampledDimension sd = data_array.appendSampledDimension(samplingInterval);
        assertEquals(sd.getDimensionType(), DimensionType.Sample);

        assertTrue(sd.getSamplingInterval() == Math.PI);

        try {
            sd.setSamplingInterval(impossible_sampling_interval);
            fail();
        } catch (RuntimeException re) {
        }
        try {
            sd.setSamplingInterval(invalid_sampling_interval);
            fail();
        } catch (RuntimeException e) {
        }

        try {
            sd.setSamplingInterval(samplingInterval);
        } catch (Exception e) {
            fail();
        }

        assertTrue(sd.getSamplingInterval() == samplingInterval);

        data_array.deleteDimension(sd.getIndex());
    }

    @Test
    public void testSampledDimOffset() {
        double offset = 1.0;
        double samplingInterval = Math.PI;

        SampledDimension sd = data_array.appendSampledDimension(samplingInterval);
        assertEquals(sd.getDimensionType(), DimensionType.Sample);

        try {
            sd.setOffset(offset);
        } catch (Exception e) {
            fail();
        }

        assertTrue(sd.getOffset() == offset);

        try {
            sd.setOffset(0.0);
        } catch (Exception e) {
            fail();
        }

        assertTrue(sd.getOffset() == 0.0);

        data_array.deleteDimension(sd.getIndex());
    }

    @Test
    public void testSampledDimIndexOf() {
        double offset = 1.0;
        double samplingInterval = Math.PI;

        SampledDimension sd = data_array.appendSampledDimension(samplingInterval);
        assertEquals(sd.getDimensionType(), DimensionType.Sample);

        try {
            sd.getIndexOf(-3.14);
            fail();
        } catch (RuntimeException re) {
        }

        try {
            sd.getIndexOf(-1.14);
        } catch (Exception e) {
            fail();
        }

        assertTrue(sd.getIndexOf(3.14) == 1);
        assertTrue(sd.getIndexOf(6.28) == 2);
        assertTrue(sd.getIndexOf(4.28) == 1);
        assertTrue(sd.getIndexOf(7.28) == 2);

        sd.setOffset(offset);
        assertTrue(sd.getOffset() == offset);
        assertTrue(sd.getIndexOf(-1 * samplingInterval / 2 + offset + 0.001) == 0);

        try {
            sd.getIndexOf(-3.14);
            fail();
        } catch (RuntimeException re) {
        }

        assertTrue(sd.getIndexOf(2.14) == 0);
        assertTrue(sd.getIndexOf(6.28) == 2);
        assertTrue(sd.getIndexOf(4.28) == 1);
        assertTrue(sd.getIndexOf(7.28) == 2);

        data_array.deleteDimension(sd.getIndex());
    }

    @Test
    public void testSampledDimPositionAt() {
        double offset = 1.0;
        double samplingInterval = Math.PI;

        SampledDimension sd = data_array.appendSampledDimension(samplingInterval);
        assertEquals(sd.getDimensionType(), DimensionType.Sample);
        sd.setOffset(offset);
        assertTrue(sd.getPositionAt(0) == offset);
        assertTrue(200 * samplingInterval + offset ==
                        sd.getPositionAt(200)
        );

        assertTrue(sd.getOffset() == offset);

        assertTrue(200 * samplingInterval + offset ==
                        sd.getPositionAt(200)
        );

        data_array.deleteDimension(sd.getIndex());
    }

    @Test
    public void testSampledDimAxis() {
        double offset = 1.0;
        double samplingInterval = Math.PI;

        SampledDimension sd = data_array.appendSampledDimension(samplingInterval);
        assertEquals(sd.getDimensionType(), DimensionType.Sample);
        sd.setOffset(offset);

        List<Double> axis = sd.getAxis(100);
        assertTrue(axis.size() == 100);
        assertTrue(axis.get(0) == offset);
        assertTrue(99 * samplingInterval + offset ==
                axis.get(axis.size() - 1));

        axis = sd.getAxis(100, 10);
        assertTrue(10 * samplingInterval + offset ==
                axis.get(0));
        assertTrue(109 * samplingInterval + offset ==
                axis.get(axis.size() - 1));

        data_array.deleteDimension(sd.getIndex());
    }

    @Test
    public void testSetDimLabels() {
        List<String> labels = Arrays.asList("label_a", "label_b", "label_c", "label_d", "label_e");
        List<String> new_labels = Arrays.asList("new label_a", "new label_b", "new label_c");

        SetDimension sd = data_array.appendSetDimension();
        assertEquals(sd.getDimensionType(), DimensionType.Set);

        assertNotNull(sd);

        sd.setLabels(labels);
        List<String> retrieved_labels = sd.getLabels();
        assertTrue(retrieved_labels.size() == labels.size());
        for (int i = 0; i < labels.size(); i++) {
            assertEquals(labels.get(i), retrieved_labels.get(i));
        }

        sd.setLabels(new_labels);
        retrieved_labels = sd.getLabels();
        assertTrue(retrieved_labels.size() == new_labels.size());
        for (int i = 0; i < new_labels.size(); i++) {
            assertEquals(new_labels.get(i), retrieved_labels.get(i));
        }

        sd.setLabels(null);
        retrieved_labels = sd.getLabels();
        assertEquals(0, retrieved_labels.size());

        data_array.deleteDimension(sd.getIndex());
    }

    @Test
    public void testRangeDimLabel() {
        String label = "aLabel";
        String other_label = "anotherLabel";
        double[] ticks = new double[5];
        for (int i = 0; i < 5; i++) {
            ticks[i] = Math.PI;
        }

        RangeDimension rd = data_array.appendRangeDimension(ticks);
        assertEquals(rd.getDimensionType(), DimensionType.Range);

        rd.setLabel(label);
        assertEquals(rd.getLabel(), label);
        rd.setLabel(other_label);
        assertEquals(rd.getLabel(), other_label);
        rd.setLabel(null);
        assertNull(rd.getLabel());

        data_array.deleteDimension(rd.getIndex());
    }

    @Test
    public void testRangeDimUnit() {
        String invalidUnit = "invalidunit";
        String validUnit = "ms";

        double[] ticks = new double[5];
        for (int i = 0; i < 5; i++) {
            ticks[i] = Math.PI;
        }
        RangeDimension rd = data_array.appendRangeDimension(ticks);
        assertEquals(rd.getDimensionType(), DimensionType.Range);

        try {
            rd.setUnit(invalidUnit);
            fail();
        } catch (RuntimeException re) {
        }

        try {
            rd.setUnit(validUnit);
        } catch (Exception e) {
            fail();
        }

        assertEquals(rd.getUnit(), validUnit);

        try {
            rd.setUnit(null);
        } catch (Exception e) {
            fail();
        }

        assertNull(rd.getUnit());

        data_array.deleteDimension(rd.getIndex());
    }

    @Test
    public void testRangeTicks() {
        double[] ticks = {1.0, 2.0, 3.4, 42.0};
        double[] new_ticks = {-100.0, -10.0, 0.0, 10.0, 100.0};
        double[] unordered_ticks = {-20.0, -100.0, 10.0, -10.0, 0.0};
        double[] double_ticks = {-20.0, -10.0, 10.0, -10.0, -20.0};
        RangeDimension rd = data_array.appendRangeDimension(ticks);
        assertEquals(rd.getDimensionType(), DimensionType.Range);

        assertTrue(rd.getTicks().size() == ticks.length);
        List<Double> retrieved_ticks = rd.getTicks();
        assertTrue(retrieved_ticks.size() == ticks.length);
        for (int i = 0; i < ticks.length; i++) {
            assertTrue(ticks[i] == retrieved_ticks.get(i));
        }
        try {
            rd.setTicks(unordered_ticks);
            fail();
        } catch (RuntimeException re) {
        }
        try {
            rd.setTicks(double_ticks);
            fail();
        } catch (RuntimeException re) {
        }

        rd.setTicks(new_ticks);
        retrieved_ticks = rd.getTicks();
        assertTrue(retrieved_ticks.size() == new_ticks.length);
        for (int i = 0; i < new_ticks.length; i++) {
            assertTrue(new_ticks[i] == retrieved_ticks.get(i));
        }

        data_array.deleteDimension(rd.getIndex());
    }

    @Test
    public void testRangeDimIndexOf() {
        double[] ticks = {-100.0, -10.0, 0.0, 10.0, 100.0};
        RangeDimension rd = data_array.appendRangeDimension(ticks);
        assertEquals(rd.getDimensionType(), DimensionType.Range);

        assertTrue(rd.getIndexOf(-100.) == 0);
        assertTrue(rd.getIndexOf(-50.) == 1);
        assertTrue(rd.getIndexOf(-70.) == 0);
        assertTrue(rd.getIndexOf(5.0) == 2);
        assertTrue(rd.getIndexOf(257.28) == 4);
        assertTrue(rd.getIndexOf(-257.28) == 0);

        data_array.deleteDimension(rd.getIndex());
    }

    @Test
    public void testRangeDimTickAt() {
        double[] ticks = {-100.0, -10.0, 0.0, 10.0, 100.0};
        RangeDimension rd = data_array.appendRangeDimension(ticks);
        assertEquals(rd.getDimensionType(), DimensionType.Range);

        assertTrue(rd.getTickAt(0) == -100.);
        assertTrue(rd.getTickAt(4) == 100.);

        try {
            rd.getTickAt(10);
            fail();
        } catch (RuntimeException re) {
        }

        assertTrue(rd.getTickAt(0) == -100.);
        assertTrue(rd.getTickAt(4) == 100.);

        try {
            rd.getTickAt(10);
            fail();
        } catch (RuntimeException re) {
        }

        data_array.deleteDimension(rd.getIndex());
    }

    @Test
    public void testRangeDimAxis() {
        double[] ticks = {-100.0, -10.0, 0.0, 10.0, 100.0};
        RangeDimension rd = data_array.appendRangeDimension(ticks);
        assertEquals(rd.getDimensionType(), DimensionType.Range);

        List<Double> axis = rd.getAxis(2);
        assertTrue(axis.size() == 2);
        assertTrue(axis.get(0) == -100.0);
        assertTrue(axis.get(1) == -10.0);

        axis = rd.getAxis(2, 2);
        assertTrue(axis.size() == 2);
        assertTrue(axis.get(0) == 0.0);
        assertTrue(axis.get(1) == 10.0);

        try {
            rd.getAxis(10);
            fail();
        } catch (RuntimeException re) {
        }

        try {
            rd.getAxis(2, 10);
            fail();
        } catch (RuntimeException re) {
        }

        data_array.deleteDimension(rd.getIndex());
    }
}
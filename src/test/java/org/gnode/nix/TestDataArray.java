package org.gnode.nix;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class TestDataArray {

    private File file;
    private Block block;
    private DataArray array1, array2;

    private Date statup_time;

    @Before
    public void setUp() {
        // precision of time_t is in seconds hence (millis / 1000) * 1000
        statup_time = new Date((System.currentTimeMillis() / 1000) * 1000);

        file = File.open("test_DataArray.h5", FileMode.Overwrite);

        block = file.createBlock("block_one", "dataset");
        array1 = block.createDataArray("array_one",
                "testdata",
                DataType.Double,
                new NDSize(new int[]{0, 0, 0}));
        array2 = block.createDataArray("random",
                "double",
                DataType.Double,
                new NDSize(new int[]{20, 20}));
    }

    @After
    public void tearDown() {
        file.close();
    }

    @Test
    public void testId() {
        assertEquals(array1.getId().length(), 36);
    }

    @Test
    public void testName() {
        assertEquals(array1.getName(), "array_one");
    }

    @Test
    public void testType() {
        assertEquals(array1.getType(), "testdata");
    }

    @Test
    public void testDefinition() {
        assertNull(array1.getDefinition());
    }

    @Test
    public void testLabel() {
        String testStr = "somestring";

        array1.setLabel(testStr);
        assertEquals(array1.getLabel(), testStr);
        array1.setLabel(null);
        assertNull(array1.getLabel());
    }

    @Test
    public void testUnit() {
        String testStr = "somestring";
        String validUnit = "mV^2";

        try {
            array1.setUnit(testStr);
            fail();
        } catch (RuntimeException re) {
        }

        try {
            array1.setUnit(validUnit);
        } catch (Exception e) {
            fail();
        }

        assertEquals(array1.getUnit(), validUnit);


        try {
            array1.setUnit(null);
        } catch (Exception e) {
            fail();
        }

        assertNull(array1.getUnit());
    }

    @Test
    public void testDimension() {
        List<Dimension> dims = new ArrayList<Dimension>();
        double[] ticks = new double[5];
        double samplingInterval = Math.PI;

        for (int i = 0; i < 5; i++) {
            ticks[i] = Math.PI;
        }

        try {
            array2.appendRangeDimension(new double[]{});
            fail();
        } catch (RuntimeException re) {
        }

        try {
            array2.createRangeDimension(1, new double[]{});
            fail();
        } catch (RuntimeException re) {
        }

        dims.add(array2.createSampledDimension(1, samplingInterval));
        dims.add(array2.createSetDimension(2));
        dims.add(array2.createRangeDimension(3, ticks));
        dims.add(array2.appendSampledDimension(samplingInterval));
        dims.add(array2.appendSetDimension());
        dims.set(3, array2.createRangeDimension(4, ticks));

        // have some explicit dimension types
        RangeDimension dim_range = array1.appendRangeDimension(ticks);
        SampledDimension dim_sampled = array1.appendSampledDimension(samplingInterval);
        SetDimension dim_set = array1.appendSetDimension();

        assertTrue(array2.getDimension(dims.get(0).getIndex()).getDimensionType() == DimensionType.Sample);
        assertTrue(array2.getDimension(dims.get(1).getIndex()).getDimensionType() == DimensionType.Set);
        assertTrue(array2.getDimension(dims.get(2).getIndex()).getDimensionType() == DimensionType.Range);
        assertTrue(array2.getDimension(dims.get(3).getIndex()).getDimensionType() == DimensionType.Range);
        assertTrue(array2.getDimension(dims.get(4).getIndex()).getDimensionType() == DimensionType.Set);

        assertTrue(array2.getDimensionCount() == 5);

        // since deleteDimension renumbers indices to be continuous we test that too
        array2.deleteDimension(5);
        array2.deleteDimension(4);
        array2.deleteDimension(1);
        array2.deleteDimension(1);
        array2.deleteDimension(1);

        dims = array2.getDimensions();
        assertTrue(array2.getDimensionCount() == 0);
        assertTrue(dims.size() == 0);
    }

    @Test
    public void testCreatedAt() {
        assertTrue(array1.getCreatedAt().compareTo(statup_time) >= 0);
        assertTrue(array2.getCreatedAt().compareTo(statup_time) >= 0);

        long time = System.currentTimeMillis() - 10000000L * 1000;
        // precision of time_t is in seconds hence (millis / 1000) * 1000
        time = time / 1000 * 1000;

        Date past_time = new Date(time);
        array1.forceCreatedAt(past_time);
        array2.forceCreatedAt(past_time);
        assertTrue(array1.getCreatedAt().equals(past_time));
        assertTrue(array2.getCreatedAt().equals(past_time));
    }

    @Test
    public void testUpdatedAt() {
        assertTrue(array1.getUpdatedAt().compareTo(statup_time) >= 0);
        assertTrue(array2.getUpdatedAt().compareTo(statup_time) >= 0);
    }
}
package org.gnode.nix;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class TestBlock {

    private File file;
    private Block block, block_other, block_null;

    private Date statup_time;

    @Before
    public void setUp() {
        // precision of time_t is in seconds hence (millis / 1000) * 1000
        statup_time = new Date((System.currentTimeMillis() / 1000) * 1000);

        file = File.open("test_block.h5", FileMode.Overwrite);

        block = file.createBlock("block_one", "dataset");
        block_other = file.createBlock("block_two", "dataset");
        block_null = null;
    }

    @After
    public void tearDown() {
        file.close();
    }

    @Test
    public void testId() {
        assertEquals(block.getId().length(), 36);
    }

    @Test
    public void testName() {
        assertEquals(block.getName(), "block_one");
    }

    @Test
    public void testType() {
        assertEquals(block.getType(), "dataset");
    }

    @Test
    public void testCreatedAt() {
        assertTrue(block.getCreatedAt().compareTo(statup_time) >= 0);

        long time = System.currentTimeMillis() - 10000000L * 1000;
        // precision of time_t is in seconds hence (millis / 1000) * 1000
        time = time / 1000 * 1000;

        Date past_time = new Date(time);
        block.forceCreatedAt(past_time);
        assertTrue(block.getCreatedAt().equals(past_time));
    }

    @Test
    public void testUpdatedAt() {
        assertTrue(block.getUpdatedAt().compareTo(statup_time) >= 0);
    }


    @Test
    public void testDataArrayAccess() {
        List<String> names = Arrays.asList("data_array_a", "data_array_b", "data_array_c",
                "data_array_d", "data_array_e");
        DataArray data_array = null, a = null;

        assertEquals(block.getDataArrayCount(), 0);
        assertEquals(block.dataArrays().size(), 0);
        assertFalse(block.hasDataArray("invalid_id"));

        ArrayList<String> ids = new ArrayList<String>();
        for (String name : names) {
            data_array = block.createDataArray(name, "channel",
                    DataType.Double, new NDSize(1));
            assertEquals(data_array.getName(), name);
            assertEquals(data_array.getType(), "channel");

            ids.add(data_array.getId());
        }

        try {
            block.createDataArray(names.get(0), "channel", DataType.Double, new NDSize(1));
            fail();
        } catch (RuntimeException re) {
        }

        assertTrue(block.hasDataArray(data_array));

        try {
            block.hasDataArray(a);
            fail();
        } catch (RuntimeException re) {
        }

        assertEquals(block.getDataArrayCount(), names.size());
        assertEquals(block.dataArrays().size(), names.size());

        for (String name : names) {
            DataArray da_name = block.getDataArray(name);
            assertNotNull(da_name);

            DataArray da_id = block.getDataArray(da_name.getId());
            assertNotNull(da_id);
            assertEquals(da_name.getName(), da_id.getName());
        }

        for (String id : ids) {
            data_array = block.getDataArray(id);
            assertTrue(block.hasDataArray(id));
            assertEquals(data_array.getId(), id);

            block.deleteDataArray(id);
        }

        try {
            block.deleteDataArray(a);
            fail();
        } catch (RuntimeException re) {
        }

        assertEquals(block.getDataArrayCount(), 0);
        assertEquals(block.dataArrays().size(), 0);
        assertFalse(block.hasDataArray("invalid_id"));
    }

}
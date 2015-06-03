package org.gnode.nix;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

}
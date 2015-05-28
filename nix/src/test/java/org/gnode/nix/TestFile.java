package org.gnode.nix;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestFile {

    private File file_open;
    private File file_other;
    private File file_null;

    private long statup_time;

    @Before
    public void setUp() {
        statup_time = System.currentTimeMillis() / 1000;
        file_open = File.open("test_file.h5", FileMode.Overwrite);
        file_other = File.open("test_file_other.h5", FileMode.Overwrite);
        file_null = null;
    }

    @After
    public void tearDown() {
        file_open.close();
        file_other.close();
    }

    @Test
    public void testFormat() {
        assertEquals(file_open.getFormat(), "nix");
    }

    @Test
    public void testLocation() {
        assertEquals(file_open.getLocation(), "test_file.h5");
        assertEquals(file_other.getLocation(), "test_file_other.h5");
    }

    @Test
    public void testVersion() {
        ArrayList<Integer> version = file_open.getVersion();
        assertEquals(version.get(0), Integer.valueOf(1));
        assertEquals(version.get(1), Integer.valueOf(0));
        assertEquals(version.get(2), Integer.valueOf(0));
    }

    @Test
    public void testCreatedAt() {
        assertTrue(file_open.createdAt() >= statup_time);
        long past_time = (System.currentTimeMillis() / 1000) - 10000000;
        file_open.forceCreatedAt(past_time);
        assertEquals(file_open.createdAt(), past_time);
    }

    @Test
    public void testUpdatedAt() {
        assertTrue(file_open.updatedAt() >= statup_time);
    }
}

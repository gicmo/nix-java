package org.g_node.nix;

import net.jcip.annotations.NotThreadSafe;
import org.g_node.nix.valid.Result;
import org.g_node.nix.valid.Validator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

@NotThreadSafe
public class TestFile {

    private File file_open;
    private File file_other;
    private File file_null;

    private Date statup_time;

    @Before
    public void setUp() {
        // precision of time_t is in seconds hence (millis / 1000) * 1000
        statup_time = new Date((System.currentTimeMillis() / 1000) * 1000);

        file_open = File.open("test_File_" + UUID.randomUUID().toString() + ".h5", FileMode.Overwrite);
        file_null = null;
    }

    @After
    public void tearDown() {
        String location1 = file_open.getLocation();

        file_open.close();

        // delete file
        java.io.File f1 = new java.io.File(location1);
        f1.delete();

    }

    @Test
    public void testValidate() {
        Result result = Validator.validate(file_open);
        assertTrue(result.getErrors().size() == 0);
        assertTrue(result.getErrors().size() == 0);
    }

    @Test
    public void testFormat() {
        assertEquals(file_open.getFormat(), "nix");
    }

    @Test
    public void testLocation() {
        file_other = File.open("test_file_other.h5", FileMode.Overwrite);
        assertEquals(file_other.getLocation(), "test_file_other.h5");
        java.io.File f2 = new java.io.File(file_other.getLocation());
        f2.delete();
    }

    @Test
    public void testVersion() {
        int[] version = file_open.getVersion();
        assertEquals(version[0], 1);
        assertEquals(version[1], 1);
        //changes of the last version component ([2])
        //are non-breaking changes, so we ignore these
    }

    @Test
    public void testCreatedAt() {
        assertTrue(file_open.getCreatedAt().compareTo(statup_time) >= 0);

        long time = System.currentTimeMillis() - 10000000L * 1000;
        // precision of time_t is in seconds hence (millis / 1000) * 1000
        time = time / 1000 * 1000;

        Date past_time = new Date(time);
        file_open.forceCreatedAt(past_time);
        assertTrue(file_open.getCreatedAt().equals(past_time));
    }

    @Test
    public void testUpdatedAt() {
        assertTrue(file_open.getUpdatedAt().compareTo(statup_time) >= 0);
    }

    @Test
    public void testBlockAccess() {
        List<String> names = Arrays.asList("block_a", "block_b", "block_c", "block_d", "block_e");
        Block b = null;
        assertEquals(file_open.getBlockCount(), 0);
        assertEquals(file_open.getBlocks().size(), 0);
        assertFalse(file_open.hasBlock("invalid_id"));
        try {
            file_open.hasBlock(b);
            fail();
        } catch (NullPointerException npe) {
        }

        ArrayList<String> ids = new ArrayList<String>();
        for (String name : names) {
            Block bl = file_open.createBlock(name, "dataset");
            assertEquals(bl.getName(), name);
            assertTrue(file_open.hasBlock(bl));
            assertTrue(file_open.hasBlock(name));

            ids.add(bl.getId());
        }

        try {
            file_open.createBlock(names.get(0), "dataset");
            fail();
        } catch (RuntimeException re) {
        }

        assertEquals(file_open.getBlockCount(), names.size());
        assertEquals(file_open.getBlocks().size(), names.size());

        for (String name : names) {
            Block bl_name = file_open.getBlock(name);
            assertNotNull(bl_name);

            Block bl_id = file_open.getBlock(bl_name.getId());
            assertNotNull(bl_id);
            assertEquals(bl_name.getName(), bl_id.getName());
        }

        for (String id : ids) {
            Block bl = file_open.getBlock(id);
            assertTrue(file_open.hasBlock(id));
            assertEquals(bl.getId(), id);

            file_open.deleteBlock(id);
        }

        try {
            file_open.deleteBlock(b);
            fail();
        } catch (NullPointerException npe) {
        }

        b = file_open.createBlock("test", "test");
        assertTrue(file_open.deleteBlock(b));
        assertEquals(file_open.getBlockCount(), 0);
        assertEquals(file_open.getBlocks().size(), 0);
    }

    @Test
    public void testSectionAccess() {

        List<String> names = Arrays.asList("section_a", "section_b", "section_c", "section_d", "section_e");
        Section s = null;
        assertEquals(file_open.getSectionCount(), 0);
        assertEquals(file_open.getSections().size(), 0);
        assertFalse(file_open.hasSection("invalid_id"));
        try {
            file_open.hasSection(s);
            fail();
        } catch (NullPointerException npe) {
        }

        ArrayList<String> ids = new ArrayList<String>();
        for (String name : names) {
            Section sec = file_open.createSection(name, "dataset");
            assertTrue(file_open.hasSection(sec));
            assertTrue(file_open.hasSection(name));
            assertEquals(sec.getName(), name);

            ids.add(sec.getId());
        }

        try {
            file_open.createSection(names.get(0), "root section");
            fail();
        } catch (RuntimeException re) {
        }

        assertEquals(file_open.getSectionCount(), names.size());
        assertEquals(file_open.getSections().size(), names.size());

        for (String id : ids) {
            Section sec = file_open.getSection(id);
            assertTrue(file_open.hasSection(id));
            file_open.deleteSection(id);
        }

        try {
            file_open.deleteSection(s);
            fail();
        } catch (NullPointerException npe) {
        }

        s = file_open.createSection("test", "test");
        assertTrue(file_open.hasSection(s));
        assertTrue(file_open.deleteSection(s));
        assertEquals(file_open.getSectionCount(), 0);
        assertEquals(file_open.getSections().size(), 0);
        assertFalse(file_open.hasSection("invalid_id"));
    }
}
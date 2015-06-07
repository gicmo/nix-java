package org.gnode.nix;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class TestSource {

    private File file;
    private Block block;
    private Section section;
    private Source source, source_other, source_null;

    private Date statup_time;

    @Before
    public void setUp() {
        // precision of time_t is in seconds hence (millis / 1000) * 1000
        statup_time = new Date((System.currentTimeMillis() / 1000) * 1000);

        file = File.open("test_source.h5", FileMode.Overwrite);
        block = file.createBlock("block", "dataset");
        section = file.createSection("foo_section", "metadata");

        source = block.createSource("source_one", "channel");
        source_other = block.createSource("source_two", "channel");
        source_null = null;
    }

    @After
    public void tearDown() {
        file.close();
    }

    @Test
    public void testId() {
        assertEquals(source.getId().length(), 36);
    }

    @Test
    public void testName() {
        assertEquals(source.getName(), "source_one");
    }

    @Test
    public void testType() {
        assertEquals(source.getType(), "channel");
    }

    @Test
    public void testDefinition() {
        String def = "def";
        source.setDefinition(def);
        assertEquals(source.getDefinition(), def);

        source.setDefinition(null);
        assertNull(source.getDefinition());
    }

    @Test
    public void testtestMetadataAccess() {
        assertNull(source.getMetadata());

        source.setMetadata(section);
        assertNotNull(source.getMetadata());

        source.removeMetadata();
        assertNull(source.getMetadata());
        // test deleter removing link too
        source.setMetadata(section);
        file.deleteSection(section.getId());
        assertNull(source.getMetadata());
        // re-create section
        section = file.createSection("foo_section", "metadata");
    }

    @Test
    public void testSourceAccess() {
        List<String> names = Arrays.asList("source_a", "source_b", "source_c", "source_d", "source_e");

        assertEquals(source.getSourceCount(), 0);
        assertEquals(source.getSources().size(), 0);
        assertFalse(source.hasSource("invalid_id"));

        Source s = null;
        try {
            source.hasSource(s);
            fail();
        } catch (RuntimeException re) {

        }

        ArrayList<String> ids = new ArrayList<String>();
        for (String name : names) {
            Source child_source = source.createSource(name, "channel");
            assertEquals(child_source.getName(), name);
            assertTrue(source.hasSource(child_source));
            assertTrue(source.hasSource(name));

            ids.add(child_source.getId());
        }

        try {
            source.createSource(names.get(0), "channel");
            fail();
        } catch (RuntimeException re) {
        }

        assertEquals(source.getSourceCount(), names.size());
        assertEquals(source.getSources().size(), names.size());

        for (String id : ids) {
            Source child_source = source.getSource(id);
            assertTrue(source.hasSource(id));
            assertEquals(child_source.getId(), id);

            source.deleteSource(id);
        }
        Source s1, s2 = null;
        s1 = source.createSource("name", "type");

        try {
            source.deleteSource(s2);
            fail();
        } catch (RuntimeException re) {
        }

        try {
            source.deleteSource(s1);
        } catch (Exception e) {
            fail();
        }

        assertEquals(source.getSourceCount(), 0);
        assertEquals(source.getSources().size(), 0);
        assertFalse(source.hasSource("invalid_id"));
    }

    @Test
    public void testCreatedAt() {
        assertTrue(source.getCreatedAt().compareTo(statup_time) >= 0);

        long time = System.currentTimeMillis() - 10000000L * 1000;
        // precision of time_t is in seconds hence (millis / 1000) * 1000
        time = time / 1000 * 1000;

        Date past_time = new Date(time);
        source.forceCreatedAt(past_time);
        assertTrue(source.getCreatedAt().equals(past_time));
    }

    @Test
    public void testUpdatedAt() {
        assertTrue(source.getUpdatedAt().compareTo(statup_time) >= 0);
    }
}

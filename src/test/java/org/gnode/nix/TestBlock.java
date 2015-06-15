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
    private Section section;
    private Block block, block_other, block_null;

    private Date statup_time;

    @Before
    public void setUp() {
        // precision of time_t is in seconds hence (millis / 1000) * 1000
        statup_time = new Date((System.currentTimeMillis() / 1000) * 1000);

        file = File.open("test_block.h5", FileMode.Overwrite);

        section = file.createSection("foo_section", "metadata");

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
    public void testMetadataAccess() {
        assertNull(block.getMetadata());

        block.setMetadata(section);
        assertNotNull(block.getMetadata());

        // test none-unsetter
        block.removeMetadata();
        assertNull(block.getMetadata());

        // test deleter removing link too
        block.setMetadata(section);
        file.deleteSection(section.getId());
        assertNull(block.getMetadata());

        // re-create section
        section = file.createSection("foo_section", "metadata");
    }

    @Test
    public void testSourceAccess() {
        List<String> names = Arrays.asList("source_a", "source_b", "source_c", "source_d", "source_e");
        Source s = null;

        try {
            block.hasSource(s);
            fail();
        } catch (RuntimeException re) {
        }

        assertEquals(block.getSourceCount(), 0);
        assertEquals(block.getSources().size(), 0);
        assertFalse(block.hasSource("invalid_id"));

        ArrayList<String> ids = new ArrayList<String>();
        for (String name : names) {
            Source src = block.createSource(name, "channel");
            assertEquals(src.getName(), name);
            assertTrue(block.hasSource(name));
            assertTrue(block.hasSource(src));

            ids.add(src.getId());
        }

        try {
            block.createSource(names.get(0), "channel");
            fail();
        } catch (RuntimeException re) {
        }

        assertEquals(block.getSourceCount(), names.size());
        assertEquals(block.getSources().size(), names.size());


        for (String id : ids) {
            Source src = block.getSource(id);
            assertTrue(block.hasSource(id));
            assertEquals(src.getId(), id);
            block.deleteSource(id);
        }

        s = block.createSource("test", "test");
        assertTrue(block.getSourceCount() == 1);

        try {
            block.deleteSource(s);
        } catch (Exception e) {
            fail();
        }

        assertEquals(block.getSourceCount(), 0);
        assertEquals(block.getSources().size(), 0);
        assertFalse(block.hasSource("invalid_id"));
    }

    @Test
    public void testDataArrayAccess() {
        List<String> names = Arrays.asList("data_array_a", "data_array_b", "data_array_c",
                "data_array_d", "data_array_e");
        DataArray data_array = null, a = null;

        assertEquals(block.getDataArrayCount(), 0);
        assertEquals(block.getDataArrays().size(), 0);
        assertFalse(block.hasDataArray("invalid_id"));

        ArrayList<String> ids = new ArrayList<String>();
        for (String name : names) {
            data_array = block.createDataArray(name, "channel",
                    DataType.Double, new NDSize(new int[]{0}));
            assertEquals(data_array.getName(), name);
            assertEquals(data_array.getType(), "channel");

            ids.add(data_array.getId());
        }

        try {
            block.createDataArray(names.get(0), "channel", DataType.Double, new NDSize(new int[]{0}));
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
        assertEquals(block.getDataArrays().size(), names.size());

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
        assertEquals(block.getDataArrays().size(), 0);
        assertFalse(block.hasDataArray("invalid_id"));
    }

    @Test
    public void testTagAccess() {
        List<String> names = Arrays.asList("tag_a", "tag_b", "tag_c", "tag_d", "tag_e");
        List<String> array_names = Arrays.asList("data_array_a", "data_array_b", "data_array_c",
                "data_array_d", "data_array_e");
        List<DataArray> refs = new ArrayList<DataArray>();
        Tag tag, t = null;
        for (String name : array_names) {
            refs.add(block.createDataArray(name,
                    "reference",
                    DataType.Double,
                    new NDSize(new int[]{0})));
        }

        assertEquals(block.getTagCount(), 0);
        assertEquals(block.getTags().size(), 0);
        assertFalse(block.hasTag("invalid_id"));

        List<String> ids = new ArrayList<String>();
        for (String name : names) {
            tag = block.createTag(name, "segment", new double[]{0.0, 2.0, 3.4});
            tag.setReferences(refs);
            assertEquals(tag.getName(), name);
            assertTrue(block.hasTag(tag));
            ids.add(tag.getId());
        }

        try {
            block.createTag(names.get(0), "segment", new double[]{0.0, 2.0, 3.4});
            fail();
        } catch (RuntimeException re) {
        }

        assertEquals(block.getTagCount(), names.size());
        assertEquals(block.getTags().size(), names.size());

        for (String id : ids) {
            tag = block.getTag(id);
            assertTrue(block.hasTag(id));
            assertEquals(tag.getId(), id);

            block.deleteTag(id);
        }

        tag = block.createTag("test", "test", new double[]{0.0});
        assertTrue(block.hasTag(tag));

        try {
            block.deleteTag(tag);
        } catch (Exception e) {
            fail();
        }

        try {
            block.deleteTag(t);
            fail();
        } catch (RuntimeException re) {
        }

        assertEquals(block.getTagCount(), 0);
        assertEquals(block.getTags().size(), 0);
        assertFalse(block.hasTag("invalid_id"));
    }

    @Test
    public void testMultiTagAccess() {
        List<String> names = Arrays.asList("tag_a", "tag_b", "tag_c", "tag_d", "tag_e");
        MultiTag mtag, m = null;
        // create a valid positions data array below
        DataArray positions = block.createDataArray("array_one",
                "testdata",
                DataType.Double,
                new NDSize(new int[]{3, 4, 2}));

        assertEquals(block.getMultiTagCount(), 0);
        assertEquals(block.getMultiTags().size(), 0);
        assertNull(block.getMultiTag("invalid_id"));
        assertFalse(block.hasMultiTag("invalid_id"));

        try {
            block.hasMultiTag(m);
            fail();
        } catch (RuntimeException re) {
        }

        List<String> ids = new ArrayList<String>();
        for (String name : names) {
            mtag = block.createMultiTag(name, "segment", positions);
            assertEquals(mtag.getName(), name);
            assertTrue(block.hasMultiTag(mtag));
            ids.add(mtag.getId());
        }

        try {
            block.createMultiTag(names.get(0), "segment", positions);
            fail();
        } catch (RuntimeException re) {
        }

        assertEquals(block.getMultiTagCount(), names.size());
        assertEquals(block.getMultiTags().size(), names.size());

        for (String id : ids) {
            mtag = block.getMultiTag(id);
            assertTrue(block.hasMultiTag(id));
            assertEquals(mtag.getId(), id);

            block.deleteMultiTag(id);
        }

        mtag = block.createMultiTag("test", "test", positions);
        assertTrue(block.hasMultiTag(mtag));

        try {
            block.deleteMultiTag(m);
            fail();
        } catch (RuntimeException re) {
        }

        try {
            block.deleteMultiTag(mtag);
        } catch (Exception e) {
            fail();
        }

        assertEquals(block.getMultiTagCount(), 0);
        assertEquals(block.getMultiTags().size(), 0);
        assertNull(block.getMultiTag("invalid_id"));
    }
}
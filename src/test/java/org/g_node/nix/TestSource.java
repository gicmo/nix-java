package org.g_node.nix;

import net.jcip.annotations.NotThreadSafe;
import org.g_node.nix.valid.Result;
import org.g_node.nix.valid.Validator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.function.Predicate;

import static org.junit.Assert.*;

@NotThreadSafe
public class TestSource {

    private File file;
    private Block block;
    private Section section;
    private Source source, source_other, source_null;
    private DataArray darray;
    private MultiTag mtag;

    private Date statup_time;

    @Before
    public void setUp() {
        // precision of time_t is in seconds hence (millis / 1000) * 1000
        statup_time = new Date((System.currentTimeMillis() / 1000) * 1000);

        file = File.open("test_Source_" + UUID.randomUUID().toString() + ".h5", FileMode.Overwrite);
        block = file.createBlock("block", "dataset");
        section = file.createSection("foo_section", "metadata");

        source = block.createSource("source_one", "channel");
        source_other = block.createSource("source_two", "channel");
        source_null = null;

        // create a DataArray & a MultiTag
        darray = block.createDataArray("DataArray", "dataArray",
                DataType.Double, new NDSize(new int[]{0, 0}));
        double[] A = new double[5 * 5];
        for (int i = 0; i < 5; i++) {
            A[i * 5 + i] = 100 * i;
        }
        darray.setDataExtent(new NDSize(new int[]{5, 5}));
        darray.setData(A, darray.getDataExtent(), new NDSize());
        mtag = block.createMultiTag("tag_one", "test_tag", darray);
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
    public void testValidate() {
        Result result = Validator.validate(source);
        assertTrue(result.getErrors().size() == 0);
        assertTrue(result.getWarnings().size() == 0);
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
    public void testFindSource() {
        /* We create the following tree:
         *
         * source---l1n1---l2n1---l3n1
         *    |      |      |
         *    |      ------l2n2
         *    |      |
         *    |      |-----l2n3---l3n2
         *    |             |
         *    |             ------l3n3
         *    ------l1n2
         *    |
         *    ------l1n3---l2n4
         *           |
         *           ------l2n5---l3n4
         *           |
         *           ------l2n6---l3n5
         *                  |      |
         * mtag-------------|      |
         *                         |
         * darray-------------------
         */
        Source l1n1 = source.createSource("l1n1", "typ1");
        Source l1n2 = source.createSource("l1n2", "typ2");
        Source l1n3 = source.createSource("l1n3", "typ3");
        Source l2n1 = l1n1.createSource("l2n1", "typ1");
        Source l2n2 = l1n1.createSource("l2n2", "typ2");
        Source l2n3 = l1n1.createSource("l2n3", "typ2");
        Source l2n4 = l1n3.createSource("l2n4", "typ2");
        Source l2n5 = l1n3.createSource("l2n5", "typ2");
        Source l2n6 = l1n3.createSource("l2n6", "typ3");
        Source l3n1 = l2n1.createSource("l3n1", "typ1");
        Source l3n2 = l2n3.createSource("l3n2", "typ2");
        Source l3n3 = l2n3.createSource("l3n3", "typ2");
        Source l3n4 = l2n5.createSource("l3n4", "typ2");
        Source l3n5 = l2n5.createSource("l3n5", "typ2");
        mtag.addSource(l2n6.getId());
        darray.addSource(l3n5.getId());

        // test if sources are in place
        assertTrue(mtag.hasSource(l2n6));
        assertTrue(darray.hasSource(l3n5));
        assertTrue(mtag.getSources().size() == 1);
        assertTrue(darray.getSources().size() == 1);

        // test depth limit
        assertTrue(source.findSources().size() == 15);
        assertTrue(source.findSources((Source s) -> true, 2).size() == 10);
        assertTrue(source.findSources((Source s) -> true, 1).size() == 4);
        assertTrue(source.findSources((Source s) -> true, 0).size() == 1);

        // test filter
        Predicate<Source> filter_typ1 = (Source s) -> s.getType().equals("typ1");
        Predicate<Source> filter_typ2 = (Source s) -> s.getType().equals("typ2");
        assertTrue(source.findSources(filter_typ1).size() == 3);
        assertTrue(source.findSources(filter_typ2).size() == 9);

        // test deleter
        /* chop the tree down to:
         *
         * source---l1n2
         *    |
         *    ------l1n3---l2n4
         *           |
         *           ------l2n5---l3n4
         *           |
         *           ------l2n6---l3n5
         *                  |      |
         * mtag-------------|      |
         *                         |
         * darray-------------------
         */
        source.deleteSource(l1n1.getId());
        assertTrue(source.findSources().size() == 8);

        /* chop the tree down to:
         *
         * source---l1n3---l2n4
         *           |
         *           ------l2n5---l3n4
         *           |
         *           ------l2n6---l3n5
         *                  |      |
         * mtag-------------|      |
         *                         |
         * darray-------------------
         */
        source.deleteSource(l1n2.getId());
        assertTrue(source.findSources().size() == 7);

        /* chop the tree down to:
         *
         * source
         * mtag
         * darray
         */
        source.deleteSource(l1n3.getId());
        assertTrue(source.findSources().size() == 1);
        assertFalse(mtag.hasSource(l2n6));
        assertFalse(darray.hasSource(l3n5));
        assertTrue(mtag.getSources().size() == 0);
        assertTrue(darray.getSources().size() == 0);
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

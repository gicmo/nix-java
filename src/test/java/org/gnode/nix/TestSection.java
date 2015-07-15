package org.gnode.nix;

import net.jcip.annotations.NotThreadSafe;
import org.gnode.nix.valid.Result;
import org.gnode.nix.valid.Validator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.function.Predicate;

import static org.junit.Assert.*;

@NotThreadSafe
public class TestSection {

    private File file;
    private Section section, section_other, section_null;

    private Date statup_time;

    @Before
    public void setUp() {
        // precision of time_t is in seconds hence (millis / 1000) * 1000
        statup_time = new Date((System.currentTimeMillis() / 1000) * 1000);

        file = File.open("test_Section_" + UUID.randomUUID().toString() + ".h5", FileMode.Overwrite);

        section = file.createSection("section", "metadata");
        section_other = file.createSection("other_section", "metadata");
        section_null = null;
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
        Result result = Validator.validate(section);
        assertTrue(result.getErrors().size() == 0);
        assertTrue(result.getWarnings().size() == 0);
    }

    @Test
    public void testId() {
        assertEquals(section.getId().length(), 36);
    }

    @Test
    public void testName() {
        assertEquals(section.getName(), "section");
    }

    @Test
    public void testType() {
        assertEquals(section.getType(), "metadata");
    }

    @Test
    public void testDefinition() {
        String def = "def";
        section.setDefinition(def);
        assertEquals(section.getDefinition(), def);

        section.setDefinition(null);
        assertNull(section.getDefinition());
    }

    @Test
    public void testParent() {
        assertNull(section.getParent());

        Section child = section.createSection("child", "section");
        assertNotNull(child.getParent());
        assertEquals(child.getParent().getId(), section.getId());

        assertNull(child.getParent().getParent());
    }

    @Test
    public void testRepository() {
        assertNull(section.getRepository());
        String rep = "http://foo.bar/";
        section.setRepository(rep);
        assertEquals(section.getRepository(), rep);
        section.setRepository(null);
        assertNull(section.getRepository());
    }

    @Test
    public void testLink() {
        assertNull(section.getLink());

        section.setLink(section_other);
        assertNotNull(section.getLink());
        assertEquals(section.getLink().getId(), section_other.getId());

        // test none-unsetter
        section.removeLink();
        ;
        assertNull(section.getLink());
        // test deleter removing link too
        section.setLink(section);
        file.deleteSection(section.getId());
        assertNull(section.getLink());
        // re-create section
        section = file.createSection("foo_section", "metadata");
    }

    @Test
    public void testMapping() {
        assertNull(section.getMapping());
        String map = "http://foo.bar/";
        section.setMapping(map);
        assertEquals(section.getMapping(), map);
        section.setMapping(null);
        assertNull(section.getMapping());
    }

    @Test
    public void testSectionAccess() {
        List<String> names = Arrays.asList("section_a", "section_b", "section_c", "section_d", "section_e");

        assertEquals(section.getSectionCount(), 0);
        assertEquals(section.getSections().size(), 0);
        assertFalse(section.hasSection("invalid_id"));

        ArrayList<String> ids = new ArrayList<String>();
        for (String name : names) {
            Section child_section = section.createSection(name, "metadata");
            assertEquals(child_section.getName(), name);
            assertTrue(section.hasSection(name));

            ids.add(child_section.getId());
        }

        try {
            section.createSection(names.get(0), "metadata");
            fail();
        } catch (RuntimeException re) {
        }

        assertEquals(section.getSectionCount(), names.size());
        assertEquals(section.getSections().size(), names.size());

        for (String id : ids) {
            Section child_section = section.getSection(id);
            assertTrue(section.hasSection(id));
            assertEquals(id, child_section.getId());

            section.deleteSection(id);
        }

        assertEquals(section.getSectionCount(), 0);
        assertEquals(section.getSections().size(), 0);
        assertFalse(section.hasSection("invalid_id"));
    }

    @Test
    public void testFindSection() {
        // prepare
        Section l1n1 = section.createSection("l1n1", "typ1");
        Section l1n2 = section.createSection("l1n2", "typ2");
        Section l1n3 = section.createSection("l1n3", "typ3");

        Section l2n1 = l1n1.createSection("l2n1", "typ1");
        Section l2n2 = l1n1.createSection("l2n2", "typ2");
        Section l2n3 = l1n1.createSection("l2n3", "typ2");
        Section l2n4 = l1n3.createSection("l2n4", "typ2");
        Section l2n5 = l1n3.createSection("l2n5", "typ2");
        Section l2n6 = l1n3.createSection("l2n6", "typ3");

        Section l3n1 = l2n1.createSection("l3n1", "typ1");
        Section l3n2 = l2n3.createSection("l3n2", "typ2");
        Section l3n3 = l2n3.createSection("l3n3", "typ2");
        Section l3n4 = l2n5.createSection("l3n4", "typ2");

        // test depth limit
        assertTrue(section.findSections().size() == 14);
        assertTrue(section.findSections((Section s) -> true, 2).size() == 10);
        assertTrue(section.findSections((Section s) -> true, 1).size() == 4);
        assertTrue(section.findSections((Section s) -> true, 0).size() == 1);

        // test filter
        Predicate<Section> filter_typ1 = (Section s) -> s.getType().equals("typ1");
        Predicate<Section> filter_typ2 = (Section s) -> s.getType().equals("typ2");

        assertTrue(section.findSections(filter_typ1).size() == 3);
        assertTrue(section.findSections(filter_typ2).size() == 8);
    }

    @Test
    public void testFindRelated() {
        /* We create the following tree:
         *
         * section---l1n1---l2n1---l3n1------------
         *            |      |                    |
         *            ------l2n2---l3n2---l4n1---l5n1
         *                   |      |      |
         *                   ------l3n3---l4n2
         * section_other------------|
         */
        Section l1n1 = section.createSection("l1n1", "typ1");

        Section l2n1 = l1n1.createSection("l2n1", "t1");
        Section l2n2 = l1n1.createSection("l2n2", "t2");
        Section l3n1 = l2n1.createSection("l3n1", "t3");
        Section l3n2 = l2n2.createSection("l3n2", "t3");
        Section l3n3 = l2n2.createSection("l3n3", "t4");
        Section l4n1 = l3n2.createSection("l4n1", "typ2");
        Section l4n2 = l3n3.createSection("l4n2", "typ2");
        Section l5n1 = l4n1.createSection("l5n1", "typ2");
        l2n1.setLink(l2n2.getId());
        l3n1.setLink(l5n1.getId());
        l3n2.setLink(l3n3.getId());
        l4n1.setLink(l4n2.getId());
        section_other.setLink(l3n3.getId());

        String t1 = "t1";
        String t3 = "t3";
        String t4 = "t4";
        String typ2 = "typ2";
        String typ1 = "typ1";

        List<Section> related = l1n1.findRelated((Section s) -> s.getType().equals(t1));
        assertTrue(related.size() == 1);
        related = l1n1.findRelated((Section s) -> s.getType().equals(t3));
        assertTrue(related.size() == 2);
        related = l1n1.findRelated((Section s) -> s.getType().equals(t4));
        assertTrue(related.size() == 1);
        related = l1n1.findRelated((Section s) -> s.getType().equals(typ2));
        assertTrue(related.size() == 2);
        related = l4n1.findRelated((Section s) -> s.getType().equals(typ1));
        assertTrue(related.size() == 1);
        related = l4n1.findRelated((Section s) -> s.getType().equals(t1));
        assertTrue(related.size() == 1);
        related = l3n2.findRelated((Section s) -> s.getType().equals(t1));
        assertTrue(related.size() == 1);
        related = l3n2.findRelated((Section s) -> s.getType().equals(t3));
        assertTrue(related.size() == 0);

        /* Chop the tree to:
         *
         * section---l1n1---l2n1---l3n1
         * section_other
         *
         */
        l1n1.deleteSection(l2n2.getId());
        assertTrue(section.findSections().size() == 4);
        // test that all (horizontal) links are gone too:
        assertNull(l2n1.getLink());
        assertNull(l3n1.getLink());
        assertNull(l3n2.getLink());
        assertNull(l4n1.getLink());
        assertNull(section_other.getLink());
        assertFalse(l1n1.hasSection(l2n2));

        /* Extend the tree to:
         *
         * section---l1n1---l2n1---l3n1
         * section_other-----|
         *
         * and then chop it down to:
         *
         * section_other
         *
         */
        section_other.setLink(l2n1.getId());
        file.deleteSection(section.getId());
        assertTrue(section_other.findSections().size() == 1);
        assertNull(section_other.getLink());

        // re-create section
        section = file.createSection("section", "metadata");
    }

    @Test
    public void testPropertyAccess() {
        List<String> names = Arrays.asList("property_a", "property_b", "property_c", "property_d", "property_e");

        assertEquals(section.getPropertyCount(), 0);
        assertEquals(section.getProperties().size(), 0);
        assertFalse(section.hasProperty("invalid_id"));

        Property p = section.createProperty("empty_prop", DataType.Double);
        assertEquals(section.getPropertyCount(), 1);
        assertTrue(section.hasProperty(p));
        assertTrue(section.hasProperty("empty_prop"));
        Property prop = section.getProperty("empty_prop");
        assertEquals(prop.getValueCount(), 0);
        assertEquals(prop.getDataType(), DataType.Double);
        section.deleteProperty(p.getId());
        assertEquals(section.getPropertyCount(), 0);

        Value dummy = new Value(10);
        prop = section.createProperty("single value", dummy);
        assertTrue(section.hasProperty("single value"));
        assertEquals(section.getPropertyCount(), 1);
        section.deleteProperty(prop.getId());
        assertEquals(section.getPropertyCount(), 0);

        ArrayList<String> ids = new ArrayList<String>();
        for (String name : names) {
            prop = section.createProperty(name, dummy);
            assertEquals(prop.getName(), name);
            assertTrue(section.hasProperty(name));

            Property prop_copy = section.getProperty(name);

            assertEquals(prop_copy.getId(), prop.getId());

            ids.add(prop.getId());
        }

        try {
            section.createProperty(names.get(0), dummy);
            fail();
        } catch (RuntimeException re) {
        }

        assertEquals(section.getPropertyCount(), names.size());
        assertEquals(section.getProperties().size(), names.size());
        section_other.createProperty("some_prop", dummy);
        section_other.setLink(section);
        assertEquals(section_other.getPropertyCount(), 1);
        assertEquals(section_other.getInheritedProperties().size(), names.size() + 1);

        for (String id : ids) {
            prop = section.getProperty(id);
            assertTrue(section.hasProperty(id));
            assertEquals(prop.getId(), id);

            section.deleteProperty(id);
        }

        assertEquals(section.getPropertyCount(), 0);
        assertEquals(section.getProperties().size(), 0);
        assertFalse(section.hasProperty("invalid_id"));

        ArrayList<Value> values = new ArrayList<Value>();
        values.add(new Value(10));
        values.add(new Value(100));
        section.createProperty("another test", values);
        assertEquals(section.getPropertyCount(), 1);
        prop = section.getProperty("another test");
        assertEquals(prop.getValueCount(), 2);
    }

    @Test
    public void testCreatedAt() {
        assertTrue(section.getCreatedAt().compareTo(statup_time) >= 0);

        long time = System.currentTimeMillis() - 10000000L * 1000;
        // precision of time_t is in seconds hence (millis / 1000) * 1000
        time = time / 1000 * 1000;

        Date past_time = new Date(time);
        section.forceCreatedAt(past_time);
        assertTrue(section.getCreatedAt().equals(past_time));
    }

    @Test
    public void testUpdatedAt() {
        assertTrue(section.getUpdatedAt().compareTo(statup_time) >= 0);
    }
}

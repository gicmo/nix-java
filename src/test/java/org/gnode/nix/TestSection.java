package org.gnode.nix;

import net.jcip.annotations.NotThreadSafe;
import org.gnode.nix.valid.Result;
import org.gnode.nix.valid.Validator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

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

package org.g_node.nix;

import net.jcip.annotations.NotThreadSafe;
import org.g_node.nix.valid.Result;
import org.g_node.nix.valid.Validator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

@NotThreadSafe
public class TestProperty {

    private File file;
    private Section section;
    private Variant int_dummy, str_dummy;
    private Property property, property_other, property_null;

    private Date statup_time;

    @Before
    public void setUp() {
        // precision of time_t is in seconds hence (millis / 1000) * 1000
        statup_time = new Date((System.currentTimeMillis() / 1000) * 1000);

        file = File.open("test_Property_" + UUID.randomUUID().toString() + ".h5", FileMode.Overwrite);

        section = file.createSection("cool section", "metadata");
        int_dummy = new Variant(10);
        str_dummy = new Variant("test");
        property = section.createProperty("prop", int_dummy);
        property_other = section.createProperty("other", int_dummy);
        property_null = null;
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
        // values are set but unit is missing: 1 warning
        Result result = Validator.validate(property);
        assertTrue(result.getErrors().size() == 0);
        assertTrue(result.getWarnings().size() == 1);
    }

    @Test
    public void testId() {
        assertEquals(property.getId().length(), 36);
    }

    @Test
    public void testName() {
        assertEquals(property.getName(), "prop");
    }

    @Test
    public void testDefinition() {
        String def = "some_str";
        property.setDefinition(def);
        assertEquals(property.getDefinition(), def);
        property.setDefinition(null);
        assertNull(property.getDefinition());
    }

    @Test
    public void testUnit() {
        Section section = file.createSection("testSection", "test");

        Property p1 = section.createProperty("testProperty", int_dummy);
        String nosi_unit = "invalid unit";
        String valid_unit = "mV*cm^-2";
        String second_unit = "mV";

        try {
            p1.setUnit(nosi_unit);
        } catch (RuntimeException re) {
        }

        p1.setUnit(valid_unit);
        assertEquals(p1.getUnit(), valid_unit);

        p1.setUnit(null);
        assertNull(p1.getUnit());

        try {
            p1.setUnit(second_unit);
        } catch (Exception e) {
            fail();
        }
        assertEquals(p1.getUnit(), second_unit);
    }

    @Test
    public void testValues() {
        Section section = file.createSection("Area51", "Boolean");
        Property p1 = section.createProperty("strProperty", str_dummy);

        List<Variant> strValues = new ArrayList<Variant>();
        strValues.add(new Variant("Freude"));
        strValues.add(new Variant("schoener"));
        strValues.add(new Variant("Goetterfunken"));

        p1.setValues(strValues);
        assertEquals(p1.getValueCount(), strValues.size());

        List<Variant> ctrlValues = p1.getValues();
        for (int i = 0; i < ctrlValues.size(); ++i) {
            assertEquals(strValues.get(i).getString(), ctrlValues.get(i).getString());
        }

        strValues.add(new Variant("Tochter"));
        strValues.add(new Variant("aus"));
        strValues.add(new Variant("Elysium"));
        strValues.add(new Variant("Wir betreten feuertrunken"));

        p1.setValues(strValues);
        assertEquals(p1.getValueCount(), strValues.size());

        strValues.remove(6);
        p1.setValues(strValues);
        assertEquals(p1.getValueCount(), strValues.size());

        Property p2 = section.createProperty("toDelete", str_dummy);
        assertEquals(p2.getValueCount(), 1);
        assertEquals(p2.getValues().get(0).getString(), str_dummy.getString());

        strValues.clear();
        p2.setValues(strValues);
        assertEquals(p2.getValueCount(), strValues.size());

        p2.deleteValues();
        assertTrue(p2.getValues().isEmpty());
    }

    @Test
    public void testDataType() {
        Section section = file.createSection("Area51", "Boolean");
        List<Variant> strValues = new ArrayList<Variant>();
        strValues.add(new Variant("Freude"));
        strValues.add(new Variant("schoener"));
        strValues.add(new Variant("Goetterfunken"));

        List<Variant> doubleValues = new ArrayList<Variant>();
        doubleValues.add(new Variant(1.0));
        doubleValues.add(new Variant(2.0));
        doubleValues.add(new Variant(-99.99));

        Property p1 = section.createProperty("strProperty", strValues);
        Property p2 = section.createProperty("doubleProperty", doubleValues);

        assertEquals(p1.getDataType(), DataType.String);
        assertEquals(p2.getDataType(), DataType.Double);

        file.deleteSection(section.getId());
    }

    @Test
    public void testCreatedAt() {
        assertTrue(property.getCreatedAt().compareTo(statup_time) >= 0);

        long time = System.currentTimeMillis() - 10000000L * 1000;
        // precision of time_t is in seconds hence (millis / 1000) * 1000
        time = time / 1000 * 1000;

        Date past_time = new Date(time);
        property.forceCreatedAt(past_time);
        assertTrue(property.getCreatedAt().equals(past_time));
    }

    @Test
    public void testUpdatedAt() {
        assertTrue(property.getUpdatedAt().compareTo(statup_time) >= 0);
    }
}

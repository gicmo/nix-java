package org.gnode.nix;

import org.gnode.nix.valid.Result;
import org.gnode.nix.valid.Validator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

public class TestProperty {

    private File file;
    private Section section;
    private Value int_dummy, str_dummy;
    private Property property, property_other, property_null;

    private Date statup_time;

    @Before
    public void setUp() {
        // precision of time_t is in seconds hence (millis / 1000) * 1000
        statup_time = new Date((System.currentTimeMillis() / 1000) * 1000);

        file = File.open("test_Property_" + UUID.randomUUID().toString() + ".h5", FileMode.Overwrite);

        section = file.createSection("cool section", "metadata");
        int_dummy = new Value(10);
        str_dummy = new Value("test");
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
    public void testMapping() {
        String map = "some_str";
        property.setMapping(map);
        assertEquals(property.getMapping(), map);
        property.setMapping(null);
        assertNull(property.getMapping());
    }

    @Test
    public void testUnit() {
        Section section = file.createSection("testSection", "test");
        Value v = new Value(22.2);

        Property p1 = section.createProperty("testProperty", int_dummy);
        String inv_unit = "invalid unit";
        String valid_unit = "mV*cm^-2";
        String second_unit = "mV";

        try {
            p1.setUnit(inv_unit);
            fail();
        } catch (RuntimeException re) {
        }

        assertNull(p1.getUnit());

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

        List<Value> strValues = new ArrayList<Value>();
        strValues.add(new Value("Freude"));
        strValues.add(new Value("schoener"));
        strValues.add(new Value("Goetterfunken"));

        p1.setValues(strValues);
        assertEquals(p1.getValueCount(), strValues.size());

        List<Value> ctrlValues = p1.getValues();
        for (int i = 0; i < ctrlValues.size(); ++i) {
            assertEquals(strValues.get(i).getString(), ctrlValues.get(i).getString());
        }

        strValues.add(new Value("Tochter"));
        strValues.add(new Value("aus"));
        strValues.add(new Value("Elysium"));
        strValues.add(new Value("Wir betreten feuertrunken"));

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
        List<Value> strValues = new ArrayList<Value>();
        strValues.add(new Value("Freude"));
        strValues.add(new Value("schoener"));
        strValues.add(new Value("Goetterfunken"));

        List<Value> doubleValues = new ArrayList<Value>();
        doubleValues.add(new Value(1.0));
        doubleValues.add(new Value(2.0));
        doubleValues.add(new Value(-99.99));

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

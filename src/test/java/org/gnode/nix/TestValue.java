package org.gnode.nix;

import net.jcip.annotations.NotThreadSafe;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

@NotThreadSafe
public class TestValue {

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {

    }

    @Test
    public void testObject() {

        Value boolVal = new Value(true);
        assertEquals(boolVal.getBoolean(), true);
        try {
            boolVal.getInt();
            fail();
        } catch (RuntimeException re) {
        }

        Value intVal = new Value(42);
        assertEquals(intVal.getInt(), 42);
        try {
            intVal.getBoolean();
            fail();
        } catch (RuntimeException re) {
        }

        Value longVal = new Value(42L);
        assertEquals(longVal.getLong(), 42L);
        try {
            longVal.getBoolean();
            fail();
        } catch (RuntimeException re) {
        }

        Value doubleVal = new Value(2.71828);
        assertTrue(doubleVal.getDouble() == 2.71828);
        try {
            doubleVal.getBoolean();
            fail();
        } catch (RuntimeException re) {
        }

        Value stringVal = new Value("When shall we three meet again");
        assertEquals(stringVal.getString(), "When shall we three meet again");
        try {
            stringVal.getBoolean();
            fail();
        } catch (RuntimeException re) {
        }

        Value v1 = new Value();
        assertEquals(v1.getType(), DataType.Nothing);

        assertFalse(Value.supportsType(DataType.DateTime));
    }

    @Test
    public void testSwap() {

        Value v1 = new Value("Hallo");
        Value v2 = new Value("Welt");

        v1.swap(v2);

        assertEquals(v1.getString(), "Welt");
        assertEquals(v2.getString(), "Hallo");

        //lets swap a int with a Nothing
        Value v3 = new Value(42);
        Value v4 = new Value();

        v4.swap(v3);

        assertEquals(v4.getInt(), 42);
        assertEquals(v3.getType(), DataType.Nothing);

        // and now with the string
        String checkStr = v2.getString();
        v4.swap(v2);
        assertEquals(v2.getInt(), 42);
        assertEquals(v4.getString(), checkStr);
    }
}

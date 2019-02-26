package org.g_node.nix;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import net.jcip.annotations.NotThreadSafe;

@NotThreadSafe
public class TestVariant {

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {

    }

    @Test
    public void testObject() {

        Variant boolVal = new Variant(true);
        assertEquals(boolVal.getBoolean(), true);
        try {
            boolVal.getInt();
            fail();
        } catch (RuntimeException re) {
        }

        Variant intVal = new Variant(42);
        assertEquals(intVal.getInt(), 42);
        try {
            intVal.getBoolean();
            fail();
        } catch (RuntimeException re) {
        }

        Variant longVal = new Variant(42L);
        assertEquals(longVal.getLong(), 42L);
        try {
            longVal.getBoolean();
            fail();
        } catch (RuntimeException re) {
        }

        Variant doubleVal = new Variant(2.71828);
        assertTrue(doubleVal.getDouble() == 2.71828);
        try {
            doubleVal.getBoolean();
            fail();
        } catch (RuntimeException re) {
        }

        Variant stringVal = new Variant("When shall we three meet again");
        assertEquals(stringVal.getString(), "When shall we three meet again");
        try {
            stringVal.getBoolean();
            fail();
        } catch (RuntimeException re) {
        }

        Variant v1 = new Variant();
        assertEquals(v1.getType(), DataType.Nothing);
    }

    @Test
    public void testSwap() {

        Variant v1 = new Variant("Hallo");
        Variant v2 = new Variant("Welt");

        v1.swap(v2);

        assertEquals(v1.getString(), "Welt");
        assertEquals(v2.getString(), "Hallo");

        //lets swap a int with a Nothing
        Variant v3 = new Variant(42);
        Variant v4 = new Variant();

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

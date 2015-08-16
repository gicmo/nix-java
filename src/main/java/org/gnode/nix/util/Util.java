package org.gnode.nix.util;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.annotation.*;
import org.gnode.nix.base.Entity;
import org.gnode.nix.base.NamedEntity;
import org.gnode.nix.internal.VectorUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>Util</h1>
 * This class contains utility functions.
 */

@Properties(value = {
        @Platform(include = {"<nix/util/util.hpp>"}, link = "nix", preload = "hdf5"),
        @Platform(value = "linux"),
        @Platform(value = "windows")})
@Namespace("nix::util")
public class Util {
    static {
        Loader.load();
    }

    private static native void deblankString(@StdString BytePointer str);

    /**
     * Remove blank spaces from the entire string
     *
     * @param str The string to trim.
     */
    public static String deblankString(String str) {
        BytePointer bp = new BytePointer(str);
        deblankString(bp);
        return bp.getString();
    }

    /**
     * Replace forbidden chars in name string
     *
     * @param name The string to sanitize
     * @return The sanitized string
     */
    public static native
    @StdString
    String nameSanitizer(@StdString String name);

    /**
     * Check if the name is legit or needs the {@link Util#nameSanitizer(String)}
     *
     * @param name The string to check
     * @return true if name is legit, false otherwise
     */
    public static native
    @ByVal
    boolean nameCheck(@StdString String name);

    /**
     * Generates an ID-String.
     *
     * @return The generated id string.
     */
    public static native
    @StdString
    String createId();

    /**
     * Extract id from given entity. Does not work for dimensions
     *
     * @return The entity id.
     */
    public static <T extends Entity> String toId(T entity) {
        return entity.getId();
    }

    /**
     * Extract name from given entity. Does not work for dimensions
     *
     * @return The entity name.
     */
    public static <T extends NamedEntity> String toName(T entity) {
        return entity.getName();
    }

    /**
     * Sanitizer function that deblanks units and replaces mu and µ
     * with the "u" replacement.
     *
     * @param unit The old unit.
     * @return The sanitized unit.
     */
    public static native
    @StdString
    String unitSanitizer(@StdString String unit);

    /**
     * Checks if the passed string represents a valid SI unit.  The passed
     * unit may be atomic, e.g. 'V' or a compound unit, e.g. 'J/s'.
     *
     * @param unit A string that is supposed to represent an SI unit.
     * @return True if a valid SI unit, false otherwise.
     */
    public static native boolean isSIUnit(@StdString String unit);

    /**
     * Checks if the passed string represents a valid SI unit.
     *
     * @param unit A string that is supposed to represent an SI unit.
     * @return True if a valid SI unit, false otherwise.
     */
    public static native boolean isAtomicSIUnit(@StdString String unit);

    /**
     * Checks if the passed string is a valid combination of SI units.
     * <p>
     * For example mV^2*Hz^-1. Method accepts only the * notation.
     *
     * @param unit A string that should be tested
     * @return True if a valid compound si unti, false otherwise.
     */
    public static native boolean isCompoundSIUnit(@StdString String unit);

    /**
     * Returns whether or not two units represent scalable versions
     * of the same SI unit.
     *
     * @param unitA A string representing the first unit.
     * @param unitB A string representing the second unit.
     * @return True if the units are scalable version of the same unit.
     **/
    public static native boolean isScalable(@StdString String unitA, @StdString String unitB);

    private static native boolean isScalable(@Const @ByRef VectorUtils.StringVector unitsA, @Const @ByRef VectorUtils.StringVector unitsB);

    /**
     * Returns whether or not in all cases the units at the same
     * index in the two given unit vectors are scalable versions of the same
     * SI unit.
     *
     * @param unitsA A vector of unit strings.
     * @param unitsB A vector of unit strings.
     * @return True if the units are scalable version of the same unit.
     */
    public static boolean isScalable(List<String> unitsA, List<String> unitsB) {
        return isSetAtSamePos(new VectorUtils.StringVector(unitsA), new VectorUtils.StringVector(unitsB));
    }

    private static native boolean isSetAtSamePos(@Const @ByRef VectorUtils.StringVector stringsA, @Const @ByRef VectorUtils.StringVector stringsB);

    /**
     * Returns whether or not in all cases the strings at the same
     * index in the two given string vectors are either both set or both
     * not set (empty).
     *
     * @param stringsA A list of unit strings.
     * @param stringsB A list of unit strings.
     * @return True if the units are scalable version of the same unit.
     */
    public static boolean isSetAtSamePos(List<String> stringsA, List<String> stringsB) {
        return isSetAtSamePos(new VectorUtils.StringVector(stringsA), new VectorUtils.StringVector(stringsB));
    }

    /**
     * Get the scaling between two SI units that are identified by the two strings.
     *
     * @param originUnit      The original unit
     * @param destinationUnit The one into which a scaling should be done
     * @return A double with the appropriate scaling
     */
    public static native double getSIScaling(@StdString String originUnit, @StdString String destinationUnit);

    public static native void splitUnit(@StdString String fullUnit, @ByRef @StdString BytePointer prefix,
                                        @ByRef @StdString BytePointer unit, @ByRef @StdString BytePointer power);

    /**
     * Splits an SI unit into prefix, unit and the power components.
     *
     * @param fullUnit unit
     * @return array with prefix, unit, power
     */
    public static String[] splitUnit(String fullUnit) {
        BytePointer bp1 = new BytePointer(1);
        BytePointer bp2 = new BytePointer(1);
        BytePointer bp3 = new BytePointer(1);
        splitUnit(fullUnit, bp1, bp2, bp3);

        return new String[]{bp1.getString(), bp2.getString(), bp3.getString()};
    }

    private static native void splitCompoundUnit(@StdString String compoundUnit, @ByRef VectorUtils.StringVector atomicUnits);

    /**
     * Splits a SI unit compound into its atomic parts.
     *
     * @param compoundUnit An SI unit that consists of many atomic units
     */
    public static List<String> splitCompoundUnit(@StdString String compoundUnit) {
        VectorUtils.StringVector auList = new VectorUtils.StringVector(new ArrayList<String>());
        splitCompoundUnit(compoundUnit, auList);
        return auList.getStrings();
    }

    /**
     * Converts temperatures given in degrees Celsius of Fahrenheit to Kelvin.
     *
     * @param unit  The original unit {"F", "°F", "C", "°C"}
     * @param value The original value
     * @return The temperature in Kelvin
     */
    public static native double convertToSeconds(@StdString String unit, double value);

    /**
     * Converts temperatures given in degrees Celsius of Fahrenheit to Kelvin.
     *
     * @param unit  The original unit {"F", "°F", "C", "°C"}
     * @param value The original value
     * @return The temperature in Kelvin
     */
    public static native long convertToSeconds(@StdString String unit, long value);

    /**
     * Converts temperatures given in degrees Celsius of Fahrenheit to Kelvin.
     *
     * @param unit  The original unit {"F", "°F", "C", "°C"}
     * @param value The original value
     * @return The temperature in Kelvin
     */
    public static native double convertToKelvin(@StdString String unit, double value);

    /**
     * Converts temperatures given in degrees Celsius of Fahrenheit to Kelvin.
     *
     * @param unit  The original unit {"F", "°F", "C", "°C"}
     * @param value The original value
     * @return The temperature in Kelvin
     */
    public static native long convertToKelvin(@StdString String unit, long value);

    /**
     * Apply polynomial.
     *
     * @param coefficients coefficients
     * @param origin       origin
     * @param input        input
     * @param output       output
     * @param n            n
     */
    public static native void applyPolynomial(@StdVector double[] coefficients,
                                              double origin,
                                              @Const double[] input,
                                              double[] output,
                                              @Cast("size_t") long n);

}

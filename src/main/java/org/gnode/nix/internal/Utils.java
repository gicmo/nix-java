package org.gnode.nix.internal;

import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.Pointer;

import java.util.ArrayList;
import java.util.Date;

public class Utils {

    /**
     * Converts {@link IntPointer} to {@link Integer} {@link ArrayList}
     *
     * @param ip {@link IntPointer} pointer to be converted
     * @return array list of ints
     */
    public static ArrayList<Integer> convertPointerToList(IntPointer ip) {
        ArrayList<Integer> intList = new ArrayList<Integer>();
        if (ip != null) {
            for (int i = 0; i < ip.capacity(); i++) {
                intList.add(ip.get(i));
            }
        }
        return intList;
    }

    /**
     * Converts {@link DoublePointer} to {@link Double} {@link ArrayList}
     *
     * @param dp {@link DoublePointer} pointer to be converted
     * @return array list of doubles
     */
    public static ArrayList<Double> convertPointerToList(DoublePointer dp) {
        ArrayList<Double> doubleList = new ArrayList<Double>();
        if (dp != null) {
            for (int i = 0; i < dp.capacity(); i++) {
                doubleList.add(dp.get(i));
            }
        }
        return doubleList;
    }

    /**
     * Converts a {@link Pointer} type object of particular class to an {@link ArrayList}
     *
     * @param pointer pointer to be converted
     * @param cls     pointer object class
     * @param <T>     generic type
     * @return array list of object of class cls
     */
    public static <T> ArrayList<T> convertPointerToList(Pointer pointer, Class<T> cls) {
        ArrayList<T> arrayList = new ArrayList<T>();
        if (pointer != null) {
            for (int i = 0; i < pointer.capacity(); i++) {
                arrayList.add(cls.cast(pointer.position(i)));
            }
        }
        return arrayList;
    }

    /**
     * Convert seconds to appropriate date
     * <p/>
     * Required for functions that use time_t
     *
     * @param seconds seconds to be converted
     * @return date object
     */
    public static Date convertSecondsToDate(long seconds) {
        // convert to milliseconds by multiplying by 1000
        // DateTime expects input in milliseconds
        seconds *= 1000L;

        return new Date(seconds);
    }

    /**
     * Convert date object to seconds
     * <p/>
     * Required for functions that use time_t
     *
     * @param dateTime date to be converted
     * @return seconds
     */
    public static long convertDateToSeconds(Date dateTime) {
        long seconds = dateTime.getTime() / 1000;
        return seconds;
    }
}

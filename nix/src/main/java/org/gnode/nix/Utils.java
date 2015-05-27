package org.gnode.nix;

import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.Pointer;

import java.util.ArrayList;

public class Utils {

    /**
     * Converts {@link IntPointer} to {@link Integer} {@link ArrayList}
     *
     * @param ip {@link IntPointer} pointer to be converted
     * @return array list of ints
     */
    protected static ArrayList<Integer> convertPointerToList(IntPointer ip) {
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
    protected static ArrayList<Double> convertPointerToList(DoublePointer dp) {
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
    protected static <T> ArrayList<T> convertPointerToList(Pointer pointer, Class<T> cls) {
        ArrayList<T> arrayList = new ArrayList<T>();
        if (pointer != null) {
            for (int i = 0; i < pointer.capacity(); i++) {
                arrayList.add(cls.cast(pointer.position(i)));
            }
        }
        return arrayList;
    }
}

package org.g_node.nix.internal;

import java.util.*;
import java.util.function.Predicate;

/**
 * Creates a list from a counter and a getter.
 */
public final class ListBuilder {

    public static <T> List<T> build(Counter counter, Getter<T> getter) {
        long c = counter.count();
        List<T> result = new LinkedList<>();
        for (long i = 0; i < c; i++) {
            result.add(getter.get(i));
        }
        return result;
    }

    public static <T> List<T> build(Counter counter, Getter<T> getter, Predicate<T> filter) {
        long c = counter.count();
        List<T> result = new LinkedList<>();
        for (long i = 0; i < c; i++) {
            T elem = getter.get(i);
            if (filter.test(elem))
                result.add(elem);
        }
        return result;
    }

    @FunctionalInterface
    public interface Counter {
        long count();
    }

    @FunctionalInterface
    public interface Getter<T> {
        T get(long index);
    }
}

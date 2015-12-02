package org.g_node.nix.util;


public class Assert {
    public static <E extends Throwable> void assertThrows(Class<E> exception, Runnable test) {
        try {
            test.run();
            org.junit.Assert.fail("Was supposed to throw " + exception.getSimpleName() +
                                  ", but no exception was thrown.");
        } catch (Throwable e) {
            if (! exception.isInstance(e)) {
                org.junit.Assert.fail("Was supposed to throw " + exception.getSimpleName() +
                                      ", but " + e.getClass().getSimpleName() + " was thrown instead.");
            }
        }
    }
}

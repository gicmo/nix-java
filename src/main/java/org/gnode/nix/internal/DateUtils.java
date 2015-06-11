package org.gnode.nix.internal;

import java.util.Date;

public class DateUtils {

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

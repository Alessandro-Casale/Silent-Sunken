package com.alessandro.silentsunken.api;

public class TimeUtils {
    public static long getTimeNanos() {
        return System.nanoTime();
    }

    public static long secondsToNanos(int seconds) {
        return seconds * 1_000_000_000L;
    }

    public static long secondsToNanos(double seconds) {
        return (long) (seconds * 1_000_000_000L);
    }

    public static double nanosToSeconds(long nanos) {
        return nanos / 1_000_000_000d;
    }

    public static int secondsToTicks(int seconds) {
        return seconds * 20;
    }
}

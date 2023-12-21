package com.relogiclabs.json.schema.internal.util;

import java.util.stream.Stream;

public final class StreamHelper {
    public static <T> T halt(RuntimeException exception) {
        throw exception;
    }

    public static boolean forEachTrue(Stream<Boolean> stream) {
        // When no short-circuit evaluation with no early return
        boolean result = true;
        for(var r : iterable(stream)) result &= r;
        return result;
    }

    public static <T> int count(Stream<T> stream) {
        // Java stream count ignore exceptions
        int counter = 0;
        for(var ignored : iterable(stream)) counter++;
        return counter;
    }

    public static <T> Iterable<T> iterable(Stream<T> stream) {
        return stream::iterator;
    }
}
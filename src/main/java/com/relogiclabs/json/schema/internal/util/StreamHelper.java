package com.relogiclabs.json.schema.internal.util;

import java.util.function.Predicate;
import java.util.stream.Stream;

public class StreamHelper {
    public static <T> T halt(RuntimeException exception) {
        throw exception;
    }

    public static Predicate<Boolean> allTrue() {
        return t -> t;
    }

    public static boolean allTrue(Stream<Boolean> stream) {
        // When eagerly check all, no early return
        boolean result = true;
        for(var r : iterable(stream)) result &= r;
        return result;
    }

    public static Predicate<Boolean> anyTrue() {
        return t -> t;
    }

    public static <T> int count(Stream<T> stream) {
        // When Java stream count is not applicable
        int counter = 0;
        for(var ignored : iterable(stream)) counter++;
        return counter;
    }

    public static <T> Iterable<T> iterable(Stream<T> stream) {
        return stream::iterator;
    }
}
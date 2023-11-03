package com.relogiclabs.json.schema.internal.util;

public final class MiscellaneousHelper {
    public static <T> T nonNull(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }
}
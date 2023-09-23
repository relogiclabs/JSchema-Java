package com.relogiclabs.json.schema.internal.util;

public class MiscellaneousHelper {
    public static <T> T nonNull(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }
}
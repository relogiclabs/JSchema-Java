package com.relogiclabs.jschema.message;

import lombok.Getter;

import static org.apache.commons.lang3.StringUtils.left;
import static org.apache.commons.lang3.StringUtils.right;

public final class OutlineFormatter {
    public static final int DEFAULT_MAXIMUM_LENGTH = 200;
    private static final String ABBREVIATE_MARKER = "...";

    @Getter
    private static int maximumLength = DEFAULT_MAXIMUM_LENGTH;
    private static int startLength = 2 * maximumLength / 3;
    private static int endLength = maximumLength / 3;

    private OutlineFormatter() {
        throw new UnsupportedOperationException("This class is not intended for instantiation");
    }

    public static void setMaximumLength(int length) {
        maximumLength = length;
        startLength = 2 * length / 3;
        endLength = length / 3;
    }

    public static String createOutline(Object object) {
        var string = object.toString();
        return string.length() <= maximumLength ? string
            : left(string, startLength) + ABBREVIATE_MARKER + right(string, endLength);
    }
}
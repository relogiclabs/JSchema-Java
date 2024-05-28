package com.relogiclabs.jschema.message;

import static org.apache.commons.lang3.StringUtils.left;
import static org.apache.commons.lang3.StringUtils.right;

public final class OutlineFormatter {
    private static final String ABBREVIATE_MARKER = "...";
    private static int outlineLength = 200;
    private static int startLength = 2 * outlineLength / 3;
    private static int endLength = outlineLength / 3;

    private OutlineFormatter() {
        throw new UnsupportedOperationException("This class is not intended for instantiation");
    }

    public static void setOutlineLength(int length) {
        outlineLength = length;
        startLength = 2 * length / 3;
        endLength = length / 3;
    }

    public static String createOutline(Object object) {
        var string = object.toString();
        return outlineLength >= string.length() ? string
            : left(string, startLength) + ABBREVIATE_MARKER + right(string, endLength);
    }
}
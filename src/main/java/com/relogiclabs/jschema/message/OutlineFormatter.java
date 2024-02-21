package com.relogiclabs.jschema.message;

import static org.apache.commons.lang3.StringUtils.left;
import static org.apache.commons.lang3.StringUtils.right;

public class OutlineFormatter {
    private static int outlineLength = 200;
    private static int startLength = 2 * outlineLength / 3;
    private static int endLength = outlineLength / 3;

    public static void setOutlineLength(int length) {
        outlineLength = length;
        startLength = 2 * length / 3;
        endLength = length / 3;
    }

    public static String createOutline(Object object) {
        var string = object.toString();
        if(outlineLength >= string.length()) return string;
        return left(string, startLength) + "..." + right(string, endLength);
    }
}
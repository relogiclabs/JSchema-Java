package com.relogiclabs.jschema.message;

import com.relogiclabs.jschema.tree.RuntimeContext;
import lombok.Getter;

import static org.apache.commons.lang3.StringUtils.left;
import static org.apache.commons.lang3.StringUtils.right;

public final class OutlineFormatter {
    public static final int DEFAULT_MAX_LENGTH = 200;
    private static final String ABBREVIATE_MARKER = "...";

    @Getter
    private int maxLength;
    private int startLength;
    private int endLength;

    public OutlineFormatter() {
        this(DEFAULT_MAX_LENGTH);
    }

    public OutlineFormatter(int maxLength) {
        setMaxLength(maxLength);
    }

    public void setMaxLength(int length) {
        maxLength = length;
        var netLength = length - ABBREVIATE_MARKER.length();
        startLength = 2 * netLength / 3;
        endLength = netLength / 3;
    }

    public String getOutline(Object object) {
        var string = object.toString();
        return string.length() <= maxLength ? string
            : left(string, startLength) + ABBREVIATE_MARKER + right(string, endLength);
    }

    public static void setMaxLength(RuntimeContext runtime, long length) {
        runtime.getOutlineFormatter().setMaxLength((int) length);
    }
}
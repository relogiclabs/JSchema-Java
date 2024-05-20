package com.relogiclabs.jschema.internal.script;

import com.relogiclabs.jschema.type.EInteger;
import com.relogiclabs.jschema.type.EType;
import com.relogiclabs.jschema.type.EValue;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PRIVATE;
import static org.apache.commons.lang3.StringUtils.EMPTY;

@EqualsAndHashCode
@RequiredArgsConstructor(access = PRIVATE)
public final class GRange implements EValue {
    public static final String RANGE_OPERATOR = "..";
    private final int start;
    private final int end;

    public static GRange from(EInteger start, EInteger end) {
        var vStart = start == null ? Integer.MIN_VALUE : (int) start.getValue();
        var vEnd = end == null ? Integer.MIN_VALUE : (int) end.getValue();
        return new GRange(vStart, vEnd);
    }

    public int getStart(int size) {
        if(start == Integer.MIN_VALUE) return 0;
        return start >= 0 ? start : size + start;
    }

    public int getEnd(int size) {
        if(end == Integer.MIN_VALUE) return size;
        return end >= 0 ? end : size + end;
    }

    @Override
    public EType getType() {
        return EType.RANGE;
    }

    @Override
    public String toString() {
        return toString(start) + RANGE_OPERATOR + toString(end);
    }

    private static String toString(int value) {
        return value != Integer.MIN_VALUE ? String.valueOf(value) : EMPTY;
    }
}
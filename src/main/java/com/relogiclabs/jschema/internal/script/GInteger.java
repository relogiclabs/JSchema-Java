package com.relogiclabs.jschema.internal.script;

import com.relogiclabs.jschema.type.EInteger;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor(access = PRIVATE)
public final class GInteger implements EInteger {
    private static final int CACHE_START = -1;
    private static final int CACHE_END = 256;
    private static final GInteger[] CACHE = createCache();
    private final long value;

    public static GInteger from(long value) {
        if(value >= CACHE_START && value <= CACHE_END)
            return CACHE[(int) value - CACHE_START];
        return new GInteger(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    private static GInteger[] createCache() {
        var size = CACHE_END - CACHE_START + 1;
        GInteger[] cache = new GInteger[size];
        var value = CACHE_START;
        for(int i = 0; i < size; i++) cache[i] = new GInteger(value++);
        return cache;
    }
}
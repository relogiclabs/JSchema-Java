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
    private static final int CACHE_SIZE = 256;
    private static final GInteger[] CACHE = createCache(CACHE_SIZE);
    private final long value;

    public static GInteger of(long value) {
        if(value >= 0 && value < CACHE_SIZE) return CACHE[(int) value];
        return new GInteger(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    private static GInteger[] createCache(int size) {
        GInteger[] cache = new GInteger[size];
        for(int i = 0; i < size; i++) cache[i] = new GInteger(i);
        return cache;
    }
}
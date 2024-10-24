package com.relogiclabs.jschema.internal.script;

import com.relogiclabs.jschema.type.EBoolean;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@EqualsAndHashCode
@RequiredArgsConstructor(access = PRIVATE)
public final class GBoolean implements EBoolean {
    public static final GBoolean TRUE = new GBoolean(true);
    public static final GBoolean FALSE = new GBoolean(false);

    private final boolean value;

    public static GBoolean from(boolean value) {
        return value ? TRUE : FALSE;
    }

    @Override
    public boolean getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
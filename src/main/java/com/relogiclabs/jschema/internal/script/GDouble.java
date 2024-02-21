package com.relogiclabs.jschema.internal.script;

import com.relogiclabs.jschema.type.EDouble;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor(staticName = "of")
public final class GDouble implements EDouble {
    private final double value;

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
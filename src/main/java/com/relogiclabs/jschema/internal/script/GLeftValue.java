package com.relogiclabs.jschema.internal.script;

import com.relogiclabs.jschema.type.EType;
import com.relogiclabs.jschema.type.EValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

import static com.relogiclabs.jschema.internal.engine.ScriptTreeHelper.dereference;

@Getter
@Setter
@AllArgsConstructor
public final class GLeftValue implements EValue {
    private EValue value;

    @Override
    public EType getType() {
        return value.getType();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof EValue v && Objects.equals(value, dereference(v));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean toBoolean() {
        return value.toBoolean();
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
package com.relogiclabs.jschema.internal.script;

import com.relogiclabs.jschema.type.EType;
import com.relogiclabs.jschema.type.EValue;
import lombok.Getter;

import java.util.Objects;

import static com.relogiclabs.jschema.internal.engine.ScriptTreeHelper.dereference;

@Getter
public final class GReference implements EValue {
    private EValue value;

    public GReference(EValue value) {
        this.value = dereference(value);
    }

    public void setValue(EValue value) {
        this.value = dereference(value);
    }

    @Override
    public EType getType() {
        return value.getType();
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof EValue v)) return false;
        return Objects.equals(value, dereference(v));
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
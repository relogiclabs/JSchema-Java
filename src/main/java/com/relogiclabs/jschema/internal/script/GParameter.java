package com.relogiclabs.jschema.internal.script;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import static org.apache.commons.lang3.StringUtils.removeEnd;

@Getter
@EqualsAndHashCode
public final class GParameter {
    private static final String VARIADIC_MARKER = "...";
    private final String name;
    private final boolean variadic;

    public GParameter(String name) {
        this.variadic = name.endsWith(VARIADIC_MARKER);
        this.name = removeEnd(name, VARIADIC_MARKER);
    }

    @Override
    public String toString() {
        return variadic ? name + VARIADIC_MARKER : name;
    }
}
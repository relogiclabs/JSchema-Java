package com.relogiclabs.jschema.internal.script;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import static org.apache.commons.lang3.StringUtils.removeEnd;

@Getter
@EqualsAndHashCode
public final class GParameter {
    private static final String VARIADIC_SUFFIX = "...";
    private final String name;
    private final boolean variadic;

    public GParameter(String name) {
        this.variadic = name.endsWith(VARIADIC_SUFFIX);
        this.name = removeEnd(name, VARIADIC_SUFFIX);
    }

    @Override
    public String toString() {
        return variadic ? name.concat(VARIADIC_SUFFIX) : name;
    }

    public static boolean hasVariadic(GParameter[] parameters) {
        return parameters.length != 0 && parameters[parameters.length - 1].isVariadic();
    }
}
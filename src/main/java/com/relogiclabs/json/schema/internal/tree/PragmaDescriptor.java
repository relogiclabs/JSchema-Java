package com.relogiclabs.json.schema.internal.tree;

import com.relogiclabs.json.schema.types.JBoolean;
import com.relogiclabs.json.schema.types.JNumber;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public final class PragmaDescriptor<T> {

    private static final Map<String, PragmaDescriptor<?>> PRAGMAS = new HashMap<>();

    public static final PragmaDescriptor<Boolean> IGNORE_UNDEFINED_PROPERTIES
            = new PragmaDescriptor<>("IgnoreUndefinedProperties", JBoolean.class, false);
    public static final PragmaDescriptor<Double> FLOATING_POINT_TOLERANCE
            = new PragmaDescriptor<>("FloatingPointTolerance", JNumber.class, 1E-10);
    public static final PragmaDescriptor<Boolean> IGNORE_OBJECT_PROPERTY_ORDER
            = new PragmaDescriptor<>("IgnoreObjectPropertyOrder", JBoolean.class, true);

    private final String name;
    private final Class<?> type;
    private final T defaultValue;

    private PragmaDescriptor(String name, Class<?> type, T defaultValue) {
        this.name = name;
        this.type = type;
        this.defaultValue = defaultValue;
        PRAGMAS.put(name, this);
    }

    @SuppressWarnings("unchecked")
    public static <T> PragmaDescriptor<T> from(String name) {
        return (PragmaDescriptor<T>) PRAGMAS.get(name);
    }

    public boolean matchType(Class<?> aType) {
        return type.isAssignableFrom(aType);
    }
}
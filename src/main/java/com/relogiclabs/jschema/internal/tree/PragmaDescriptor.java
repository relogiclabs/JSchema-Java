package com.relogiclabs.jschema.internal.tree;

import com.relogiclabs.jschema.node.JBoolean;
import com.relogiclabs.jschema.node.JInteger;
import com.relogiclabs.jschema.node.JNumber;
import com.relogiclabs.jschema.node.JString;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

import static com.relogiclabs.jschema.message.OutlineFormatter.DEFAULT_MAX_LENGTH;
import static com.relogiclabs.jschema.tree.PragmaRegistry.NAME_DATE_DATA_TYPE_FORMAT;
import static com.relogiclabs.jschema.tree.PragmaRegistry.NAME_ENABLE_CONTEXTUAL_EXCEPTION;
import static com.relogiclabs.jschema.tree.PragmaRegistry.NAME_FLOATING_POINT_TOLERANCE;
import static com.relogiclabs.jschema.tree.PragmaRegistry.NAME_IGNORE_OBJECT_PROPERTY_ORDER;
import static com.relogiclabs.jschema.tree.PragmaRegistry.NAME_IGNORE_UNDEFINED_PROPERTIES;
import static com.relogiclabs.jschema.tree.PragmaRegistry.NAME_OUTLINE_MAXIMUM_LENGTH;
import static com.relogiclabs.jschema.tree.PragmaRegistry.NAME_TIME_DATA_TYPE_FORMAT;

@Getter
public final class PragmaDescriptor<T> {
    public static final String ISO_8601_DATE = "YYYY-MM-DD";
    public static final String ISO_8601_TIME = "YYYY-MM-DD'T'hh:mm:ss.FZZ";

    private static final Map<String, PragmaDescriptor<?>> PRAGMAS = new HashMap<>();

    public static final PragmaDescriptor<Boolean> IGNORE_UNDEFINED_PROPERTIES
            = new PragmaDescriptor<>(NAME_IGNORE_UNDEFINED_PROPERTIES, JBoolean.class, false);
    public static final PragmaDescriptor<Double> FLOATING_POINT_TOLERANCE
            = new PragmaDescriptor<>(NAME_FLOATING_POINT_TOLERANCE, JNumber.class, 1E-10);
    public static final PragmaDescriptor<Boolean> IGNORE_OBJECT_PROPERTY_ORDER
            = new PragmaDescriptor<>(NAME_IGNORE_OBJECT_PROPERTY_ORDER, JBoolean.class, true);
    public static final PragmaDescriptor<String> DATE_DATA_TYPE_FORMAT
            = new PragmaDescriptor<>(NAME_DATE_DATA_TYPE_FORMAT, JString.class, ISO_8601_DATE);
    public static final PragmaDescriptor<String> TIME_DATA_TYPE_FORMAT
            = new PragmaDescriptor<>(NAME_TIME_DATA_TYPE_FORMAT, JString.class, ISO_8601_TIME);
    public static final PragmaDescriptor<Boolean> ENABLE_CONTEXTUAL_EXCEPTION
            = new PragmaDescriptor<>(NAME_ENABLE_CONTEXTUAL_EXCEPTION, JBoolean.class, false);
    public static final PragmaDescriptor<Integer> OUTLINE_MAXIMUM_LENGTH
            = new PragmaDescriptor<>(NAME_OUTLINE_MAXIMUM_LENGTH, JInteger.class, DEFAULT_MAX_LENGTH);

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
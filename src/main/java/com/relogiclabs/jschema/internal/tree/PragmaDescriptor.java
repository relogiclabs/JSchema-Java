package com.relogiclabs.jschema.internal.tree;

import com.relogiclabs.jschema.node.JBoolean;
import com.relogiclabs.jschema.node.JNumber;
import com.relogiclabs.jschema.node.JString;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public final class PragmaDescriptor<T> {
    public static final String ISO_8601_DATE = "YYYY-MM-DD";
    public static final String ISO_8601_TIME = "YYYY-MM-DD'T'hh:mm:ss.FZZ";

    private static final Map<String, PragmaDescriptor<?>> PRAGMAS = new HashMap<>();

    public static final PragmaDescriptor<Boolean> IGNORE_UNDEFINED_PROPERTIES
            = new PragmaDescriptor<>("IgnoreUndefinedProperties", JBoolean.class, false);
    public static final PragmaDescriptor<Double> FLOATING_POINT_TOLERANCE
            = new PragmaDescriptor<>("FloatingPointTolerance", JNumber.class, 1E-10);
    public static final PragmaDescriptor<Boolean> IGNORE_OBJECT_PROPERTY_ORDER
            = new PragmaDescriptor<>("IgnoreObjectPropertyOrder", JBoolean.class, true);
    public static final PragmaDescriptor<String> DATE_DATA_TYPE_FORMAT
            = new PragmaDescriptor<>("DateDataTypeFormat", JString.class, ISO_8601_DATE);
    public static final PragmaDescriptor<String> TIME_DATA_TYPE_FORMAT
            = new PragmaDescriptor<>("TimeDataTypeFormat", JString.class, ISO_8601_TIME);

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
package com.relogiclabs.jschema.tree;

import com.relogiclabs.jschema.exception.DuplicatePragmaException;
import com.relogiclabs.jschema.internal.time.DateTimeParser;
import com.relogiclabs.jschema.internal.tree.PragmaDescriptor;
import com.relogiclabs.jschema.node.JPragma;
import lombok.Getter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.relogiclabs.jschema.internal.tree.PragmaDescriptor.DATE_DATA_TYPE_FORMAT;
import static com.relogiclabs.jschema.internal.tree.PragmaDescriptor.ENABLE_CONTEXTUAL_EXCEPTION;
import static com.relogiclabs.jschema.internal.tree.PragmaDescriptor.FLOATING_POINT_TOLERANCE;
import static com.relogiclabs.jschema.internal.tree.PragmaDescriptor.IGNORE_OBJECT_PROPERTY_ORDER;
import static com.relogiclabs.jschema.internal.tree.PragmaDescriptor.IGNORE_UNDEFINED_PROPERTIES;
import static com.relogiclabs.jschema.internal.tree.PragmaDescriptor.TIME_DATA_TYPE_FORMAT;
import static com.relogiclabs.jschema.message.ErrorCode.PRGDUP01;
import static com.relogiclabs.jschema.message.MessageFormatter.formatForSchema;
import static com.relogiclabs.jschema.message.OutlineFormatter.setMaxLength;
import static com.relogiclabs.jschema.time.DateTimeType.DATE_TYPE;
import static com.relogiclabs.jschema.time.DateTimeType.TIME_TYPE;

@Getter
public final class PragmaRegistry implements Iterable<Map.Entry<String, JPragma>> {
    public static final String NAME_IGNORE_UNDEFINED_PROPERTIES = "IgnoreUndefinedProperties";
    public static final String NAME_FLOATING_POINT_TOLERANCE = "FloatingPointTolerance";
    public static final String NAME_IGNORE_OBJECT_PROPERTY_ORDER = "IgnoreObjectPropertyOrder";
    public static final String NAME_DATE_DATA_TYPE_FORMAT = "DateDataTypeFormat";
    public static final String NAME_TIME_DATA_TYPE_FORMAT = "TimeDataTypeFormat";
    public static final String NAME_ENABLE_CONTEXTUAL_EXCEPTION = "EnableContextualException";
    public static final String NAME_OUTLINE_MAXIMUM_LENGTH = "OutlineMaximumLength";

    private final Map<String, JPragma> pragmas;

    private boolean ignoreUndefinedProperties = IGNORE_UNDEFINED_PROPERTIES.getDefaultValue();
    private double floatingPointTolerance = FLOATING_POINT_TOLERANCE.getDefaultValue();
    private boolean ignoreObjectPropertyOrder = IGNORE_OBJECT_PROPERTY_ORDER.getDefaultValue();
    private String dateDataTypeFormat = DATE_DATA_TYPE_FORMAT.getDefaultValue();
    private String timeDataTypeFormat = TIME_DATA_TYPE_FORMAT.getDefaultValue();
    private boolean enableContextualException = ENABLE_CONTEXTUAL_EXCEPTION.getDefaultValue();

    private final RuntimeContext runtime;
    private DateTimeParser dateTypeParser;
    private DateTimeParser timeTypeParser;

    public PragmaRegistry(RuntimeContext runtime) {
        this.runtime = runtime;
        this.pragmas = new HashMap<>();
        this.dateTypeParser = new DateTimeParser(dateDataTypeFormat, DATE_TYPE);
        this.timeTypeParser = new DateTimeParser(timeDataTypeFormat, TIME_TYPE);
    }

    public JPragma addPragma(JPragma pragma) {
        if(pragmas.containsKey(pragma.getName()))
            throw new DuplicatePragmaException(formatForSchema(PRGDUP01,
                "Duplication found for " + pragma.getOutline(), pragma));
        pragmas.put(pragma.getName(), pragma);
        setPragmaValue(pragma.getName(), pragma.getValue().toNativeValue());
        return pragma;
    }

    public <T> void setPragmaValue(String name, T value) {
        switch (name) {
            case NAME_IGNORE_UNDEFINED_PROPERTIES -> ignoreUndefinedProperties = (boolean) value;
            case NAME_FLOATING_POINT_TOLERANCE -> floatingPointTolerance = (double) value;
            case NAME_IGNORE_OBJECT_PROPERTY_ORDER -> ignoreObjectPropertyOrder = (boolean) value;
            case NAME_DATE_DATA_TYPE_FORMAT -> {
                dateDataTypeFormat = (String) value;
                dateTypeParser = new DateTimeParser(dateDataTypeFormat, DATE_TYPE);
            }
            case NAME_TIME_DATA_TYPE_FORMAT -> {
                timeDataTypeFormat = (String) value;
                timeTypeParser = new DateTimeParser(timeDataTypeFormat, TIME_TYPE);
            }
            case NAME_ENABLE_CONTEXTUAL_EXCEPTION -> enableContextualException = (boolean) value;
            case NAME_OUTLINE_MAXIMUM_LENGTH -> setMaxLength(runtime, (long) value);
        }
    }

    public <T> T getPragmaValue(String name) {
        var entry = PragmaDescriptor.<T>from(name);
        var pragma = pragmas.get(entry.getName());
        return pragma == null
            ? entry.getDefaultValue()
            : pragma.<T>getValue().toNativeValue();
    }

    public JPragma getPragma(String name) {
        var entry = PragmaDescriptor.from(name);
        return pragmas.get(entry.getName());
    }

    @Override
    public Iterator<Map.Entry<String, JPragma>> iterator() {
        return pragmas.entrySet().iterator();
    }
}
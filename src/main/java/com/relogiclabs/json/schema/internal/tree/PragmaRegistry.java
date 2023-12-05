package com.relogiclabs.json.schema.internal.tree;

import com.relogiclabs.json.schema.exception.DuplicatePragmaException;
import com.relogiclabs.json.schema.internal.time.DateTimeParser;
import com.relogiclabs.json.schema.message.MessageFormatter;
import com.relogiclabs.json.schema.types.JPragma;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

import static com.relogiclabs.json.schema.internal.time.DateTimeType.DATE_TYPE;
import static com.relogiclabs.json.schema.internal.time.DateTimeType.TIME_TYPE;
import static com.relogiclabs.json.schema.message.ErrorCode.PRAG03;

@Getter
public final class PragmaRegistry {

    private static final String IGNORE_UNDEFINED_PROPERTIES = "IgnoreUndefinedProperties";
    private static final String FLOATING_POINT_TOLERANCE = "FloatingPointTolerance";
    private static final String IGNORE_OBJECT_PROPERTY_ORDER = "IgnoreObjectPropertyOrder";
    private static final String DATE_DATA_TYPE_FORMAT = "DateDataTypeFormat";
    private static final String TIME_DATA_TYPE_FORMAT = "TimeDataTypeFormat";

    public final Map<String, JPragma> pragmas;

    private boolean ignoreUndefinedProperties = PragmaDescriptor
            .IGNORE_UNDEFINED_PROPERTIES.getDefaultValue();
    private double floatingPointTolerance = PragmaDescriptor
            .FLOATING_POINT_TOLERANCE.getDefaultValue();
    private boolean ignoreObjectPropertyOrder = PragmaDescriptor
            .IGNORE_OBJECT_PROPERTY_ORDER.getDefaultValue();
    public String dateDataTypeFormat = PragmaDescriptor
            .DATE_DATA_TYPE_FORMAT.getDefaultValue();
    public String timeDataTypeFormat = PragmaDescriptor
            .TIME_DATA_TYPE_FORMAT.getDefaultValue();

    private DateTimeParser dateTypeParser;
    private DateTimeParser timeTypeParser;

    public PragmaRegistry() {
        this.pragmas = new HashMap<>();
        this.dateTypeParser = new DateTimeParser(dateDataTypeFormat, DATE_TYPE);
        this.timeTypeParser = new DateTimeParser(timeDataTypeFormat, TIME_TYPE);
    }

    public JPragma addPragma(JPragma pragma) {
        if(pragmas.containsKey(pragma.getName()))
            throw new DuplicatePragmaException(MessageFormatter.formatForSchema(
                    PRAG03, "Duplication found for " + pragma.getOutline(), pragma.getContext()));
        pragmas.put(pragma.getName(), pragma);
        setPragmaValue(pragma.getName(), pragma.getValue().toNativeValue());
        return pragma;
    }

    public <T> void setPragmaValue(String name, T value) {
        switch (name) {
            case IGNORE_UNDEFINED_PROPERTIES -> ignoreUndefinedProperties = (boolean) value;
            case FLOATING_POINT_TOLERANCE -> floatingPointTolerance = (double) value;
            case IGNORE_OBJECT_PROPERTY_ORDER -> ignoreObjectPropertyOrder = (boolean) value;
            case DATE_DATA_TYPE_FORMAT -> {
                dateDataTypeFormat = (String) value;
                dateTypeParser = new DateTimeParser(dateDataTypeFormat, DATE_TYPE);
            }
            case TIME_DATA_TYPE_FORMAT -> {
                timeDataTypeFormat = (String) value;
                timeTypeParser = new DateTimeParser(timeDataTypeFormat, TIME_TYPE);
            }
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
}
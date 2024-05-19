package com.relogiclabs.jschema.tree;

import com.relogiclabs.jschema.exception.DuplicatePragmaException;
import com.relogiclabs.jschema.internal.time.DateTimeParser;
import com.relogiclabs.jschema.internal.tree.PragmaDescriptor;
import com.relogiclabs.jschema.node.JPragma;
import lombok.Getter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.relogiclabs.jschema.message.ErrorCode.PRAG03;
import static com.relogiclabs.jschema.message.MessageFormatter.formatForSchema;
import static com.relogiclabs.jschema.time.DateTimeType.DATE_TYPE;
import static com.relogiclabs.jschema.time.DateTimeType.TIME_TYPE;

@Getter
public final class PragmaRegistry implements Iterable<Map.Entry<String, JPragma>> {

    private static final String IGNORE_UNDEFINED_PROPERTIES = "IgnoreUndefinedProperties";
    private static final String FLOATING_POINT_TOLERANCE = "FloatingPointTolerance";
    private static final String IGNORE_OBJECT_PROPERTY_ORDER = "IgnoreObjectPropertyOrder";
    private static final String DATE_DATA_TYPE_FORMAT = "DateDataTypeFormat";
    private static final String TIME_DATA_TYPE_FORMAT = "TimeDataTypeFormat";

    private final Map<String, JPragma> pragmas;

    private boolean ignoreUndefinedProperties = PragmaDescriptor
            .IGNORE_UNDEFINED_PROPERTIES.getDefaultValue();
    private double floatingPointTolerance = PragmaDescriptor
            .FLOATING_POINT_TOLERANCE.getDefaultValue();
    private boolean ignoreObjectPropertyOrder = PragmaDescriptor
            .IGNORE_OBJECT_PROPERTY_ORDER.getDefaultValue();
    private String dateDataTypeFormat = PragmaDescriptor
            .DATE_DATA_TYPE_FORMAT.getDefaultValue();
    private String timeDataTypeFormat = PragmaDescriptor
            .TIME_DATA_TYPE_FORMAT.getDefaultValue();

    private DateTimeParser dateTypeParser;
    private DateTimeParser timeTypeParser;

    public PragmaRegistry() {
        this.pragmas = new HashMap<>();
        this.dateTypeParser = new DateTimeParser(dateDataTypeFormat, DATE_TYPE);
        this.timeTypeParser = new DateTimeParser(timeDataTypeFormat, TIME_TYPE);
    }

    public JPragma addPragma(JPragma pragma) {
        if(pragmas.containsKey(pragma.getName())) throw new DuplicatePragmaException(formatForSchema(
            PRAG03, "Duplication found for " + pragma.getOutline(), pragma));
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

    @Override
    public Iterator<Map.Entry<String, JPragma>> iterator() {
        return pragmas.entrySet().iterator();
    }
}
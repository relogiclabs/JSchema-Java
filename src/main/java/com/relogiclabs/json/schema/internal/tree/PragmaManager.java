package com.relogiclabs.json.schema.internal.tree;

import com.relogiclabs.json.schema.exception.DuplicatePragmaException;
import com.relogiclabs.json.schema.message.MessageFormatter;
import com.relogiclabs.json.schema.types.JPragma;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

import static com.relogiclabs.json.schema.message.ErrorCode.PRAG03;

@Getter
public class PragmaManager {

    public static final String IGNORE_UNDEFINED_PROPERTIES = "IgnoreUndefinedProperties";
    public static final String FLOATING_POINT_TOLERANCE = "FloatingPointTolerance";
    public static final String IGNORE_OBJECT_PROPERTY_ORDER = "IgnoreObjectPropertyOrder";

    public final Map<String, JPragma> pragmas;

    private boolean ignoreUndefinedProperties = PragmaDescriptor
            .IGNORE_UNDEFINED_PROPERTIES.getDefaultValue();
    private double floatingPointTolerance = PragmaDescriptor
            .FLOATING_POINT_TOLERANCE.getDefaultValue();
    private boolean ignoreObjectPropertyOrder = PragmaDescriptor.
            IGNORE_OBJECT_PROPERTY_ORDER.getDefaultValue();

    public PragmaManager() {
        this.pragmas = new HashMap<>();
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
            case IGNORE_UNDEFINED_PROPERTIES
                    -> ignoreUndefinedProperties = (boolean) value;
            case FLOATING_POINT_TOLERANCE
                    -> floatingPointTolerance = (double) value;
            case IGNORE_OBJECT_PROPERTY_ORDER
                    -> ignoreObjectPropertyOrder = (boolean) value;
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
package com.relogiclabs.jschema.internal.message;

import com.relogiclabs.jschema.node.JNode;
import com.relogiclabs.jschema.node.JsonTypable;
import com.relogiclabs.jschema.node.JsonType;
import com.relogiclabs.jschema.type.EType;

public final class MessageHelper {
    public static final String ValidationFailed = "Validation failed";
    public static final String ValueMismatched = "Value mismatched";
    public static final String DataTypeMismatched = "Data type mismatched";
    public static final String InvalidNonCompositeType = "Invalid non-composite value type";
    public static final String NestedFunctionFailed = "Nested function failed for invalid target";
    public static final String DataTypeArgumentFailed = "Data type argument failed";
    public static final String PropertyNotFound = "Mandatory property not found";
    public static final String ArrayElementNotFound = "Mandatory array element not found";
    public static final String UndefinedPropertyFound = "Undefined property found";
    public static final String PropertyKeyMismatched = "Property key mismatched";
    public static final String PropertyValueMismatched = "Property value mismatched";
    public static final String PropertyOrderMismatched = "Property order mismatched";

    private MessageHelper() {
        throw new UnsupportedOperationException();
    }

    public static String getTypeName(JNode node) {
        return node instanceof JsonTypable jsonTypable
            ? jsonTypable.getType().getName()
            : node.getClass().getName();
    }

    public static String getTypeName(Class<?> type) {
        EType t = JsonType.getType(type);
        if(t != null) return t.getName();
        else return type.getSimpleName();
    }
}
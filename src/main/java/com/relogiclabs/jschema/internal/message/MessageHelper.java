package com.relogiclabs.jschema.internal.message;

import com.relogiclabs.jschema.node.JNode;
import com.relogiclabs.jschema.node.JsonTypable;
import com.relogiclabs.jschema.node.JsonType;
import com.relogiclabs.jschema.type.EType;

public final class MessageHelper {

    public static final String ValidationFailed = "Validation failed";
    public static final String ValueMismatch = "Value mismatch";
    public static final String DataTypeMismatch = "Data type mismatch";
    public static final String InvalidNonCompositeType = "Invalid non-composite value type";
    public static final String DataTypeArgumentFailed = "Data type argument failed";
    public static final String InvalidNestedFunction = "Invalid nested function operation";
    public static final String PropertyNotFound = "Mandatory property not found";
    public static final String ArrayElementNotFound = "Mandatory array element not found";
    public static final String UndefinedPropertyFound = "Undefined property found";
    public static final String PropertyKeyMismatch = "Property key mismatch";
    public static final String PropertyValueMismatch = "Property value mismatch";
    public static final String PropertyOrderMismatch = "Property order mismatch";

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
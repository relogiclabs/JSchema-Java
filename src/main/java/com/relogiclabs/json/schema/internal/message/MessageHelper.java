package com.relogiclabs.json.schema.internal.message;

import com.relogiclabs.json.schema.types.JNode;
import com.relogiclabs.json.schema.types.JsonTypable;

public class MessageHelper {

    public static final String InvalidNestedDataType = "Invalid nested data type operation";
    public static final String InvalidNestedFunction = "Invalid nested function operation";
    public static final String PropertyNotFound = "Mandatory property not found";
    public static final String ArrayElementNotFound = "Mandatory array element not found";
    public static final String UndefinedPropertyFound = "Undefined property found";
    public static final String PropertyKeyMismatch = "Property key mismatch";
    public static final String PropertyValueMismatch = "Property value mismatch";
    public static final String DataTypeMismatch = "Data type mismatch";
    public static final String ValidationFailed = "Validation Failed";
    public static final String ValueMismatch = "Value mismatch";
    public static final String PropertyOrderMismatch = "Property order mismatch";

    private MessageHelper() {
        throw new UnsupportedOperationException();
    }

    public static String getTypeName(JNode node) {
        return node instanceof JsonTypable jsonNode
                ? jsonNode.getType().getName()
                : node.getClass().getName();
    }
}
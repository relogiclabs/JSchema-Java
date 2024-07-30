package com.relogiclabs.jschema.internal.message;

import com.relogiclabs.jschema.message.ActualDetail;
import com.relogiclabs.jschema.node.JArray;
import com.relogiclabs.jschema.node.JNode;
import com.relogiclabs.jschema.node.JObject;
import com.relogiclabs.jschema.node.JProperty;

import static com.relogiclabs.jschema.internal.message.MessageHelper.getTypeName;
import static com.relogiclabs.jschema.internal.util.StringHelper.quote;

public final class ActualHelper {

    private ActualHelper() {
        throw new UnsupportedOperationException();
    }

    public static ActualDetail asArrayElementNotFound(JArray array) {
        return new ActualDetail(array, "not found");
    }

    public static ActualDetail asValueMismatched(JNode node) {
        return new ActualDetail(node, "found " + node.getOutline());
    }

    public static ActualDetail asGeneralValidationFailed(JNode node) {
        return new ActualDetail(node, "found " + node.getOutline());
    }

    public static ActualDetail asInvalidNonCompositeType(JNode node) {
        return new ActualDetail(node, "found non-composite " + getTypeName(node)
            + " value " + node.getOutline());
    }

    public static ActualDetail asDataTypeArgumentFailed(JNode node) {
        return new ActualDetail(node, "found invalid value " + node.getOutline());
    }

    public static ActualDetail asDataTypeMismatched(JNode node) {
        return new ActualDetail(node, "found " + getTypeName(node)
            + " inferred by " + node.getOutline());
    }

    public static ActualDetail asPropertyNotFound(JNode node, JProperty property, JObject object) {
        return new ActualDetail(node, "not found property key " + quote(property.getKey())
            + " in target object " + object.getOutline());
    }

    public static ActualDetail asUndefinedPropertyFound(JProperty property) {
        return new ActualDetail(property, "property found {" + property.getOutline() + "}");
    }

    public static ActualDetail asPropertyOrderMismatched(JNode node) {
        return node instanceof JProperty property
            ? new ActualDetail(property, "key " + quote(property.getKey())
                + " in target is found at current position")
            : new ActualDetail(node, "no key in target found at current position");
    }

    public static ActualDetail asNestedFunctionFailed(JNode node) {
        return new ActualDetail(node, "found non-composite target " + getTypeName(node)
            + " of " + node.getOutline());
    }
}
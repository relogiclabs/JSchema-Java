package com.relogiclabs.jschema.internal.message;

import com.relogiclabs.jschema.message.ExpectedDetail;
import com.relogiclabs.jschema.node.JDataType;
import com.relogiclabs.jschema.node.JFunction;
import com.relogiclabs.jschema.node.JNode;
import com.relogiclabs.jschema.node.JObject;
import com.relogiclabs.jschema.node.JProperty;

import static com.relogiclabs.jschema.internal.message.MessageHelper.getTypeName;
import static com.relogiclabs.jschema.internal.util.StringHelper.quote;

public final class ExpectedHelper {

    private ExpectedHelper() {
        throw new UnsupportedOperationException();
    }

    public static ExpectedDetail asArrayElementNotFound(JNode node, int index) {
        return new ExpectedDetail(node, "'" + node.getOutline() + "' element at " + index);
    }

    public static ExpectedDetail asValueMismatch(JNode node) {
        return new ExpectedDetail(node, "value " + node.getOutline());
    }

    public static ExpectedDetail asGeneralValueMismatch(JNode node) {
        return new ExpectedDetail(node, "a valid value of " + node.getOutline());
    }

    public static ExpectedDetail asDataTypeMismatch(JDataType dataType) {
        return new ExpectedDetail(dataType, "data type " + dataType.toString(true));
    }

    public static ExpectedDetail asInvalidNonCompositeType(JDataType dataType) {
        return new ExpectedDetail(dataType, "a composite value");
    }

    public static ExpectedDetail asDataTypeArgumentFailed(JDataType dataType) {
        return new ExpectedDetail(dataType, "a valid value for " + quote(dataType.getAlias()));
    }

    public static ExpectedDetail asDataTypeMismatch(JNode node) {
        return new ExpectedDetail(node, getTypeName(node) + " inferred by " + node.getOutline());
    }

    public static ExpectedDetail asPropertyNotFound(JProperty property) {
        return new ExpectedDetail(property, "property {" + property.getOutline() + "}");
    }

    public static ExpectedDetail asUndefinedProperty(JObject object, JProperty property) {
        return new ExpectedDetail(object, "no property with key " + quote(property.getKey()));
    }

    public static ExpectedDetail asPropertyOrderMismatch(JProperty property) {
        return new ExpectedDetail(property, "property with key "
            + quote(property.getKey()) + " at current position");
    }

    public static ExpectedDetail asInvalidFunction(JFunction function) {
        return new ExpectedDetail(function, "applying on composite type");
    }
}
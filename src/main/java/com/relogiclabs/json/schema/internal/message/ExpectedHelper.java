package com.relogiclabs.json.schema.internal.message;

import com.relogiclabs.json.schema.message.ExpectedDetail;
import com.relogiclabs.json.schema.types.JDataType;
import com.relogiclabs.json.schema.types.JFunction;
import com.relogiclabs.json.schema.types.JNode;
import com.relogiclabs.json.schema.types.JObject;
import com.relogiclabs.json.schema.types.JProperty;

import static com.relogiclabs.json.schema.internal.message.MessageHelper.getTypeName;
import static com.relogiclabs.json.schema.internal.util.StringHelper.quote;

public class ExpectedHelper {

    private ExpectedHelper() {
        throw new UnsupportedOperationException();
    }

    public static ExpectedDetail asArrayElementNotFound(JNode node, int index) {
        return new ExpectedDetail(node, "element at ", index,
                " '", node.getOutline(), "'");
    }

    public static ExpectedDetail asValueMismatch(JNode node) {
        return new ExpectedDetail(node, "value ", node.getOutline());
    }

    public static ExpectedDetail asDataTypeMismatch(JDataType dataType) {
        return new ExpectedDetail(dataType, "data type ", dataType.toString(true));
    }

    public static ExpectedDetail asInvalidNestedDataType(JDataType dataType) {
        return new ExpectedDetail(dataType, "composite data type");
    }

    public static ExpectedDetail asDataTypeArgumentFailed(JDataType dataType) {
        return new ExpectedDetail(dataType, "a valid value for ", quote(dataType.getAlias()));
    }

    public static ExpectedDetail asDataTypeMismatch(JNode node) {
        return new ExpectedDetail(node, getTypeName(node), " inferred by ",
                node.getOutline());
    }

    public static ExpectedDetail asPropertyNotFound(JProperty property) {
        return new ExpectedDetail(property, "property {", property.getOutline(), "}");
    }

    public static ExpectedDetail asUndefinedProperty(JObject object, JProperty property) {
        return new ExpectedDetail(object, "no property with key ", quote(property.getKey()));
    }

    public static ExpectedDetail asPropertyOrderMismatch(JProperty property) {
        return new ExpectedDetail(property, "property with key ",
                quote(property.getKey()), " at current position");
    }

    public static ExpectedDetail asInvalidFunction(JFunction function) {
        return new ExpectedDetail(function, "applying on composite type");
    }
}
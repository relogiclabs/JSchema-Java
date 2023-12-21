package com.relogiclabs.json.schema.internal.message;

import com.relogiclabs.json.schema.message.ExpectedDetail;
import com.relogiclabs.json.schema.type.JDataType;
import com.relogiclabs.json.schema.type.JFunction;
import com.relogiclabs.json.schema.type.JNode;
import com.relogiclabs.json.schema.type.JObject;
import com.relogiclabs.json.schema.type.JProperty;

import static com.relogiclabs.json.schema.internal.message.MessageHelper.getTypeName;
import static com.relogiclabs.json.schema.internal.util.StringHelper.quote;

public final class ExpectedHelper {

    private ExpectedHelper() {
        throw new UnsupportedOperationException();
    }

    public static ExpectedDetail asArrayElementNotFound(JNode node, int index) {
        return new ExpectedDetail(node, "'", node.getOutline(), "' element at ", index);
    }

    public static ExpectedDetail asValueMismatch(JNode node) {
        return new ExpectedDetail(node, "value ", node.getOutline());
    }

    public static ExpectedDetail asGeneralValueMismatch(JNode node) {
        return new ExpectedDetail(node, "a valid value of ", node.getOutline());
    }

    public static ExpectedDetail asDataTypeMismatch(JDataType dataType) {
        return new ExpectedDetail(dataType, "data type ", dataType.toString(true));
    }

    public static ExpectedDetail asInvalidNonCompositeType(JDataType dataType) {
        return new ExpectedDetail(dataType, "a composite value");
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
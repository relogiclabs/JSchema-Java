package com.relogiclabs.jschema.internal.message;

import com.relogiclabs.jschema.internal.util.MatchResult;
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
        return new ExpectedDetail(node, "an element for [" + node.getOutline()
            + "] at " + index);
    }

    public static ExpectedDetail asValueMismatched(JNode node) {
        return new ExpectedDetail(node, "value " + node.getOutline());
    }

    public static ExpectedDetail asGeneralValidationFailed(JNode node) {
        return new ExpectedDetail(node, "a valid value of " + node.getOutline());
    }

    public static ExpectedDetail asDataTypeMismatched(JDataType dataType, MatchResult result) {
        var builder = new StringBuilder("data type ").append(dataType.toString(true));
        if(result.getPattern() == null) return new ExpectedDetail(dataType, builder.toString());
        builder.append(" formatted as ").append(quote(result.getPattern()));
        return new ExpectedDetail(dataType, builder.toString());
    }

    public static ExpectedDetail asInvalidNonCompositeType(JDataType dataType) {
        return new ExpectedDetail(dataType, "a composite value");
    }

    public static ExpectedDetail asDataTypeArgumentFailed(JDataType dataType) {
        return new ExpectedDetail(dataType, "a valid value for " + quote(dataType.getAlias()));
    }

    public static ExpectedDetail asDataTypeMismatched(JNode node) {
        return new ExpectedDetail(node, getTypeName(node) + " inferred by " + node.getOutline());
    }

    public static ExpectedDetail asPropertyNotFound(JProperty property) {
        return new ExpectedDetail(property, "a property for {" + property.getOutline() + "}");
    }

    public static ExpectedDetail asUndefinedPropertyFound(JObject object, JProperty property) {
        return new ExpectedDetail(object, "no property with key " + quote(property.getKey()));
    }

    public static ExpectedDetail asPropertyOrderMismatched(JProperty property) {
        return new ExpectedDetail(property, "a property with key "
            + quote(property.getKey()) + " at current position");
    }

    public static ExpectedDetail asNestedFunctionFailed(JFunction function) {
        return new ExpectedDetail(function, "a composite data type for " + function);
    }
}
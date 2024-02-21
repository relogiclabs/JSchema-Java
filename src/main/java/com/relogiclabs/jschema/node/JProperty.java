package com.relogiclabs.jschema.node;

import com.relogiclabs.jschema.collection.Keyable;
import com.relogiclabs.jschema.exception.JsonSchemaException;
import com.relogiclabs.jschema.internal.builder.JPropertyBuilder;
import com.relogiclabs.jschema.internal.message.ActualHelper;
import com.relogiclabs.jschema.internal.message.ExpectedHelper;
import com.relogiclabs.jschema.message.ErrorDetail;
import lombok.Getter;

import java.util.Objects;

import static com.relogiclabs.jschema.internal.message.MessageHelper.PropertyKeyMismatch;
import static com.relogiclabs.jschema.internal.message.MessageHelper.PropertyValueMismatch;
import static com.relogiclabs.jschema.internal.util.CollectionHelper.asList;
import static com.relogiclabs.jschema.internal.util.StringHelper.quote;
import static com.relogiclabs.jschema.message.ErrorCode.PROP01;
import static com.relogiclabs.jschema.message.ErrorCode.PROP02;
import static java.util.Objects.requireNonNull;

@Getter
public final class JProperty extends JBranch implements Keyable<String> {
    private final String key;
    private final JNode value;

    private JProperty(JPropertyBuilder builder) {
        super(builder);
        key = requireNonNull(builder.key());
        value = requireNonNull(builder.value());
        children = asList(value);
    }

    public static JProperty from(JPropertyBuilder builder) {
        return new JProperty(builder).initialize();
    }

    @Override
    public boolean equals(Object other) {
        if(this == other) return true;
        if(other == null || getClass() != other.getClass()) return false;
        JProperty property = (JProperty) other;
        return Objects.equals(key, property.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

    @Override
    public boolean match(JNode node) {
        var other = castType(node, JProperty.class);
        if(other == null) return false;
        if(!key.equals(other.key)) return fail(new JsonSchemaException(
                new ErrorDetail(PROP01, PropertyKeyMismatch),
                ExpectedHelper.asValueMismatch(this),
                ActualHelper.asValueMismatch(other)));
        if(!value.match(other.value)) return fail(new JsonSchemaException(
                new ErrorDetail(PROP02, PropertyValueMismatch),
                ExpectedHelper.asValueMismatch(this),
                ActualHelper.asValueMismatch(other)));
        return true;
    }

    @Override
    public String toString() {
        return quote(key) + ": " + value;
    }
}
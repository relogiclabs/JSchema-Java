package com.relogiclabs.json.schema.types;

import com.relogiclabs.json.schema.collection.Keyable;
import com.relogiclabs.json.schema.exception.JsonSchemaException;
import com.relogiclabs.json.schema.internal.message.ActualHelper;
import com.relogiclabs.json.schema.internal.message.ExpectedHelper;
import com.relogiclabs.json.schema.message.ErrorDetail;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Objects;

import static com.relogiclabs.json.schema.internal.message.MessageHelper.PropertyKeyMismatch;
import static com.relogiclabs.json.schema.internal.message.MessageHelper.PropertyValueMismatch;
import static com.relogiclabs.json.schema.internal.util.CollectionHelper.asList;
import static com.relogiclabs.json.schema.internal.util.StringHelper.quote;
import static com.relogiclabs.json.schema.message.ErrorCode.PROP01;
import static com.relogiclabs.json.schema.message.ErrorCode.PROP02;
import static java.util.Objects.requireNonNull;

@Getter
public final class JProperty extends JBranch implements Keyable<String> {
    private final String key;
    private final JNode value;

    private JProperty(Builder builder) {
        super(builder);
        key = requireNonNull(builder.key);
        value = requireNonNull(builder.value);
        children = asList(value);
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
        if(!key.equals(other.key)) return failWith(new JsonSchemaException(
                new ErrorDetail(PROP01, PropertyKeyMismatch),
                ExpectedHelper.asValueMismatch(this),
                ActualHelper.asValueMismatch(other)));
        if(!value.match(other.value)) return failWith(new JsonSchemaException(
                new ErrorDetail(PROP02, PropertyValueMismatch),
                ExpectedHelper.asValueMismatch(this),
                ActualHelper.asValueMismatch(other)));
        return true;
    }

    @Override
    public String toString() {
        return quote(key) + ": " + value;
    }

    @Setter
    @Accessors(fluent = true)
    public static class Builder extends JNode.Builder<Builder> {
        private String key;
        private JNode value;

        @Override
        public JProperty build() {
            return build(new JProperty(this));
        }
    }
}
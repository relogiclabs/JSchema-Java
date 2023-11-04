package com.relogiclabs.json.schema.types;

import com.relogiclabs.json.schema.exception.JsonSchemaException;
import com.relogiclabs.json.schema.internal.message.ActualHelper;
import com.relogiclabs.json.schema.internal.message.ExpectedHelper;
import com.relogiclabs.json.schema.message.ErrorDetail;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import static com.relogiclabs.json.schema.internal.message.MessageHelper.ValueMismatch;
import static com.relogiclabs.json.schema.message.ErrorCode.BOOL01;
import static java.util.Objects.requireNonNull;

@Getter
@EqualsAndHashCode
public final class JBoolean extends JPrimitive implements PragmaValue<Boolean> {
    private final boolean value;

    private JBoolean(Builder builder) {
        super(builder);
        value = requireNonNull(builder.value);
    }

    @Override
    public JsonType getType() {
        return JsonType.BOOLEAN;
    }

    @Override
    public boolean match(JNode node) {
        var other = castType(node, JBoolean.class);
        if(other == null) return false;
        if(value == other.value) return true;
        return failWith(new JsonSchemaException(
                new ErrorDetail(BOOL01, ValueMismatch),
                ExpectedHelper.asValueMismatch(this),
                ActualHelper.asValueMismatch(other)));
    }

    @Override
    public Boolean toNativeValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Setter
    @Accessors(fluent = true)
    public static class Builder extends JPrimitive.Builder<Boolean> {
        @Override
        public JBoolean build() {
            return build(new JBoolean(this));
        }
    }
}
package com.relogiclabs.json.schema.types;

import com.relogiclabs.json.schema.exception.JsonSchemaException;
import com.relogiclabs.json.schema.internal.message.ActualHelper;
import com.relogiclabs.json.schema.internal.message.ExpectedHelper;
import com.relogiclabs.json.schema.message.ErrorDetail;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import static com.relogiclabs.json.schema.internal.message.MessageHelper.ValueMismatch;
import static com.relogiclabs.json.schema.message.ErrorCode.INTE01;
import static java.util.Objects.requireNonNull;

@Getter
@EqualsAndHashCode
public class JInteger extends JNumber implements PragmaValue<Long> {
    private final long value;

    private JInteger(Builder builder) {
        super(builder.relations, builder.context);
        this.value = requireNonNull(builder.value);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public JsonType getType() {
        return JsonType.INTEGER;
    }

    @Override
    public boolean match(JNode node) {
        var other = castType(node, JInteger.class);
        if(other == null) return false;
        if(value == other.value) return true;
        return failWith(new JsonSchemaException(
                new ErrorDetail(INTE01, ValueMismatch),
                ExpectedHelper.asValueMismatch(this),
                ActualHelper.asValueMismatch(other)));
    }

    @Override
    public double toDouble() {
        return value;
    }

    @Override
    public Long toNativeValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static class Builder extends JNumber.Builder<Long> {
        @Override
        public JInteger build() {
            return new JInteger(this).initialize();
        }
    }
}
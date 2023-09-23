package com.relogiclabs.json.schema.types;

import com.relogiclabs.json.schema.exception.JsonSchemaException;
import com.relogiclabs.json.schema.internal.message.ActualHelper;
import com.relogiclabs.json.schema.internal.message.ExpectedHelper;
import com.relogiclabs.json.schema.message.ErrorDetail;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.text.DecimalFormat;

import static com.relogiclabs.json.schema.internal.message.MessageHelper.ValueMismatch;
import static com.relogiclabs.json.schema.message.ErrorCode.FLOT01;
import static java.util.Objects.requireNonNull;

@Getter
@EqualsAndHashCode
public class JFloat extends JNumber implements PragmaValue<Double> {
    private static final DecimalFormat FLOAT_FORMAT = new DecimalFormat("0.0##############");
    private final double value;

    public JFloat(Builder builder) {
        super(builder.relations, builder.context);
        this.value = requireNonNull(builder.value);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public JsonType getType() {
        return JsonType.FLOAT;
    }

    @Override
    public boolean match(JNode node) {
        var other = castType(node, JFloat.class);
        if(other == null) return false;
        if(isDoubleEqual(value, other.value)) return true;
        return failWith(new JsonSchemaException(
                new ErrorDetail(FLOT01, ValueMismatch),
                ExpectedHelper.asValueMismatch(this),
                ActualHelper.asValueMismatch(other)));
    }

    @Override
    public double toDouble() {
        return value;
    }

    @Override
    public Double toNativeValue() {
        return value;
    }

    @Override
    public String toString() {
        return FLOAT_FORMAT.format(value);
    }

    public static class Builder extends JNumber.Builder<Double> {
        @Override
        public JFloat build() {
            return new JFloat(this).initialize();
        }
    }
}
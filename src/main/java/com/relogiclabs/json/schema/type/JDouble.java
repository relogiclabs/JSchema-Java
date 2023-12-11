package com.relogiclabs.json.schema.type;

import com.relogiclabs.json.schema.exception.JsonSchemaException;
import com.relogiclabs.json.schema.internal.builder.JDoubleBuilder;
import com.relogiclabs.json.schema.internal.message.ActualHelper;
import com.relogiclabs.json.schema.internal.message.ExpectedHelper;
import com.relogiclabs.json.schema.message.ErrorDetail;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.text.DecimalFormat;

import static com.relogiclabs.json.schema.internal.message.MessageHelper.ValueMismatch;
import static com.relogiclabs.json.schema.message.ErrorCode.DUBL01;
import static java.util.Objects.requireNonNull;

@Getter
@EqualsAndHashCode
public final class JDouble extends JNumber implements PragmaValue<Double> {
    private static final DecimalFormat DOUBLE_FORMAT = new DecimalFormat("0.0##############E0");
    private final double value;

    private JDouble(JDoubleBuilder builder) {
        super(builder);
        value = requireNonNull(builder.value());
    }

    public static JDouble from(JDoubleBuilder builder) {
        return new JDouble(builder).initialize();
    }

    @Override
    public JsonType getType() {
        return JsonType.DOUBLE;
    }

    @Override
    public boolean match(JNode node) {
        var other = castType(node, JDouble.class);
        if(other == null) return false;
        if(areEqual(value, other.value)) return true;
        return failWith(new JsonSchemaException(
                new ErrorDetail(DUBL01, ValueMismatch),
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
        return DOUBLE_FORMAT.format(value);
    }
}
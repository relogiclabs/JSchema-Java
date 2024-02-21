package com.relogiclabs.jschema.node;

import com.relogiclabs.jschema.exception.JsonSchemaException;
import com.relogiclabs.jschema.internal.builder.JFloatBuilder;
import com.relogiclabs.jschema.internal.message.ActualHelper;
import com.relogiclabs.jschema.internal.message.ExpectedHelper;
import com.relogiclabs.jschema.message.ErrorDetail;
import com.relogiclabs.jschema.type.EDouble;
import com.relogiclabs.jschema.type.EType;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.text.DecimalFormat;

import static com.relogiclabs.jschema.internal.message.MessageHelper.ValueMismatch;
import static com.relogiclabs.jschema.message.ErrorCode.FLOT01;
import static java.util.Objects.requireNonNull;

@Getter
@EqualsAndHashCode
public final class JFloat extends JNumber implements EDouble, PragmaValue<Double> {
    private static final DecimalFormat FLOAT_FORMAT = new DecimalFormat("0.0##############");
    private final double value;

    private JFloat(JFloatBuilder builder) {
        super(builder);
        value = requireNonNull(builder.value());
    }

    public static JFloat from(JFloatBuilder builder) {
        return new JFloat(builder).initialize();
    }

    @Override
    public EType getType() {
        return EType.FLOAT;
    }

    @Override
    public boolean match(JNode node) {
        var other = castType(node, JFloat.class);
        if(other == null) return false;
        if(areEqual(value, other.value)) return true;
        return fail(new JsonSchemaException(
                new ErrorDetail(FLOT01, ValueMismatch),
                ExpectedHelper.asValueMismatch(this),
                ActualHelper.asValueMismatch(other)));
    }

    @Override
    public Double toNativeValue() {
        return value;
    }

    @Override
    public String toString() {
        return FLOAT_FORMAT.format(value);
    }
}
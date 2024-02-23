package com.relogiclabs.jschema.node;

import com.relogiclabs.jschema.exception.JsonSchemaException;
import com.relogiclabs.jschema.internal.builder.JDoubleBuilder;
import com.relogiclabs.jschema.internal.message.ActualHelper;
import com.relogiclabs.jschema.internal.message.ExpectedHelper;
import com.relogiclabs.jschema.message.ErrorDetail;
import com.relogiclabs.jschema.type.EDouble;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.text.DecimalFormat;

import static com.relogiclabs.jschema.internal.message.MessageHelper.ValueMismatch;
import static com.relogiclabs.jschema.message.ErrorCode.DUBL01;
import static java.util.Objects.requireNonNull;

@Getter
@EqualsAndHashCode
public final class JDouble extends JNumber implements EDouble, PragmaValue<Double> {
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
    public boolean match(JNode node) {
        var other = castType(node, JDouble.class);
        if(other == null) return false;
        if(areEqual(value, other.value)) return true;
        return fail(new JsonSchemaException(
                new ErrorDetail(DUBL01, ValueMismatch),
                ExpectedHelper.asValueMismatch(this),
                ActualHelper.asValueMismatch(other)));
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
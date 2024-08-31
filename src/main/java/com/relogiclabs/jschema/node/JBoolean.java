package com.relogiclabs.jschema.node;

import com.relogiclabs.jschema.exception.ValueValidationException;
import com.relogiclabs.jschema.internal.builder.JBooleanBuilder;
import com.relogiclabs.jschema.internal.message.ActualHelper;
import com.relogiclabs.jschema.internal.message.ExpectedHelper;
import com.relogiclabs.jschema.message.ErrorDetail;
import com.relogiclabs.jschema.type.EBoolean;
import lombok.EqualsAndHashCode;

import static com.relogiclabs.jschema.internal.message.MessageHelper.ValueMismatched;
import static com.relogiclabs.jschema.message.ErrorCode.BOOLVL01;
import static java.util.Objects.requireNonNull;

@EqualsAndHashCode
public final class JBoolean extends JPrimitive implements EBoolean, PragmaValue<Boolean> {
    private final boolean value;

    private JBoolean(JBooleanBuilder builder) {
        super(builder);
        value = requireNonNull(builder.value());
    }

    public static JBoolean from(JBooleanBuilder builder) {
        return new JBoolean(builder).initialize();
    }

    @Override
    public boolean match(JNode node) {
        var other = castType(node, JBoolean.class);
        if(other == null) return false;
        if(value == other.value) return true;
        return fail(new ValueValidationException(
            new ErrorDetail(BOOLVL01, ValueMismatched),
            ExpectedHelper.asValueMismatched(this),
            ActualHelper.asValueMismatched(other)));
    }

    @Override
    public boolean getValue() {
        return value;
    }

    @Override
    public Boolean toNativeValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
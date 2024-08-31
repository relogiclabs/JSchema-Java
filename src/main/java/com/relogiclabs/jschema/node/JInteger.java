package com.relogiclabs.jschema.node;

import com.relogiclabs.jschema.exception.ValueValidationException;
import com.relogiclabs.jschema.internal.builder.JIntegerBuilder;
import com.relogiclabs.jschema.internal.message.ActualHelper;
import com.relogiclabs.jschema.internal.message.ExpectedHelper;
import com.relogiclabs.jschema.message.ErrorDetail;
import com.relogiclabs.jschema.type.EInteger;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import static com.relogiclabs.jschema.internal.message.MessageHelper.ValueMismatched;
import static com.relogiclabs.jschema.message.ErrorCode.INTVAL01;
import static java.util.Objects.requireNonNull;

@Getter
@EqualsAndHashCode
public final class JInteger extends JNumber implements EInteger, PragmaValue<Long> {
    private final long value;

    private JInteger(JIntegerBuilder builder) {
        super(builder);
        value = requireNonNull(builder.value());
    }

    public static JInteger from(JIntegerBuilder builder) {
        return new JInteger(builder).initialize();
    }

    @Override
    public boolean match(JNode node) {
        var other = castType(node, JInteger.class);
        if(other == null) return false;
        if(value == other.value) return true;
        return fail(new ValueValidationException(
            new ErrorDetail(INTVAL01, ValueMismatched),
            ExpectedHelper.asValueMismatched(this),
            ActualHelper.asValueMismatched(other)));
    }

    @Override
    public Long toNativeValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

}
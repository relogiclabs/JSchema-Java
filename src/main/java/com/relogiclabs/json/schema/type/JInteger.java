package com.relogiclabs.json.schema.type;

import com.relogiclabs.json.schema.exception.JsonSchemaException;
import com.relogiclabs.json.schema.internal.builder.JIntegerBuilder;
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
public final class JInteger extends JNumber implements PragmaValue<Long> {
    private final long value;

    private JInteger(JIntegerBuilder builder) {
        super(builder);
        value = requireNonNull(builder.value());
    }

    public static JInteger from(JIntegerBuilder builder) {
        return new JInteger(builder).initialize();
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

}
package com.relogiclabs.json.schema.type;

import com.relogiclabs.json.schema.exception.JsonSchemaException;
import com.relogiclabs.json.schema.internal.builder.JStringBuilder;
import com.relogiclabs.json.schema.internal.message.ActualHelper;
import com.relogiclabs.json.schema.internal.message.ExpectedHelper;
import com.relogiclabs.json.schema.message.ErrorDetail;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import static com.relogiclabs.json.schema.internal.message.MessageHelper.ValueMismatch;
import static com.relogiclabs.json.schema.internal.util.StringHelper.quote;
import static com.relogiclabs.json.schema.message.ErrorCode.STRN01;
import static java.util.Objects.requireNonNull;

@Getter
@EqualsAndHashCode
public class JString extends JPrimitive implements Derivable, PragmaValue<String> {
    private final String value;
    @Getter @Setter private JNode derived;

    private JString(JStringBuilder builder) {
        super(builder);
        value = requireNonNull(builder.value());
    }

    JString(JString node) {
        super(node);
        this.value = requireNonNull(node.value);
    }

    public static JString from(JStringBuilder builder) {
        return new JString(builder).initialize();
    }

    @Override
    public JsonType getType() {
        return JsonType.STRING;
    }

    @Override
    public boolean match(JNode node) {
        var other = castType(node, JString.class);
        if(other == null) return false;
        if(value.equals(other.value)) return true;
        return failWith(new JsonSchemaException(
                new ErrorDetail(STRN01, ValueMismatch),
                ExpectedHelper.asValueMismatch(this),
                ActualHelper.asValueMismatch(other)));
    }

    @Override
    public String toNativeValue() {
        return value;
    }

    @Override
    public String toString() {
        return quote(value);
    }
}
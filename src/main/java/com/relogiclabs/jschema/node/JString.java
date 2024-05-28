package com.relogiclabs.jschema.node;

import com.relogiclabs.jschema.exception.JsonSchemaException;
import com.relogiclabs.jschema.internal.builder.JStringBuilder;
import com.relogiclabs.jschema.internal.message.ActualHelper;
import com.relogiclabs.jschema.internal.message.ExpectedHelper;
import com.relogiclabs.jschema.message.ErrorDetail;
import com.relogiclabs.jschema.type.EString;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import static com.relogiclabs.jschema.internal.message.MessageHelper.ValueMismatch;
import static com.relogiclabs.jschema.internal.util.StringHelper.quote;
import static com.relogiclabs.jschema.message.ErrorCode.STRV01;
import static java.util.Objects.requireNonNull;

@Getter
@EqualsAndHashCode
public class JString extends JPrimitive implements EString, Derivable, PragmaValue<String> {
    private final String value;
    @Setter private JNode derived;

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
    public boolean match(JNode node) {
        var other = castType(node, JString.class);
        if(other == null) return false;
        if(value.equals(other.value)) return true;
        return fail(new JsonSchemaException(
                new ErrorDetail(STRV01, ValueMismatch),
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
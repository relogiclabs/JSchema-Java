package com.relogiclabs.jschema.node;

import com.relogiclabs.jschema.internal.builder.JNullBuilder;
import com.relogiclabs.jschema.type.ENull;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public final class JNull extends JPrimitive implements ENull {
    private JNull(JNullBuilder builder) {
        super(builder);
    }

    public static JNull from(JNullBuilder builder) {
        return new JNull(builder).initialize();
    }

    @Override
    public boolean match(JNode node) {
        return checkType(node, JNull.class);
    }

    @Override
    public String toString() {
        return ENull.STRING;
    }
}
package com.relogiclabs.json.schema.type;

import com.relogiclabs.json.schema.internal.builder.JNullBuilder;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public final class JNull extends JPrimitive {
    static final String NULL_MARKER = "null";

    private JNull(JNullBuilder builder) {
        super(builder);
    }

    public static JNull from(JNullBuilder builder) {
        return new JNull(builder).initialize();
    }

    @Override
    public JsonType getType() {
        return JsonType.NULL;
    }

    @Override
    public boolean match(JNode node) {
        return checkType(node, JNull.class);
    }

    @Override
    public String toString() {
        return NULL_MARKER;
    }
}
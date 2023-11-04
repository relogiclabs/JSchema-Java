package com.relogiclabs.json.schema.types;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public final class JNull extends JPrimitive {
    public static final String NULL_MARKER = "null";

    private JNull(Builder builder) {
        super(builder);
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

    public static class Builder extends JNode.Builder<Builder> {
        @Override
        public JNull build() {
            return build(new JNull(this));
        }
    }
}
package com.relogiclabs.json.schema.types;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class JNull extends JPrimitive implements JsonTypable {

    public static final String NULL_MARKER = "null";

    private JNull(Builder builder) {
        super(builder.relations, builder.context);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public JsonType getType() {
        return JsonType.NULL;
    }

    @Override
    public boolean match(JNode node) {
        return isOfType(node, JNull.class);
    }

    @Override
    public String toString() {
        return NULL_MARKER;
    }

    public static class Builder extends JNode.Builder<Builder> {
        @Override
        public JNull build() {
            return new JNull(this).initialize();
        }
    }
}
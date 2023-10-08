package com.relogiclabs.json.schema.types;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class JUndefined extends JLeaf {
    public static final String UNDEFINED_MARKER = "!";

    private JUndefined(Builder builder) {
        super(builder.relations, builder.context);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public boolean match(JNode node) {
        return true;
    }

    @Override
    public String toString() {
        return UNDEFINED_MARKER;
    }

    public static class Builder extends JNode.Builder<Builder> {
        @Override
        public JUndefined build() {
            return new JUndefined(this).initialize();
        }
    }
}
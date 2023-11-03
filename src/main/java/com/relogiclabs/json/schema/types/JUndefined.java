package com.relogiclabs.json.schema.types;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public final class JUndefined extends JLeaf {
    public static final String UNDEFINED_MARKER = "!";

    private JUndefined(Builder builder) {
        super(builder);
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
            return build(new JUndefined(this));
        }
    }
}
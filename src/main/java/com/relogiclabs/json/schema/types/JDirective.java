package com.relogiclabs.json.schema.types;

public abstract class JDirective extends JNode {
    protected JDirective(Builder<?> builder) {
        super(builder);
    }

    @Override
    public boolean match(JNode node) {
        throw new IllegalStateException();
    }
}
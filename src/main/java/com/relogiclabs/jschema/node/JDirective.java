package com.relogiclabs.jschema.node;

import com.relogiclabs.jschema.internal.builder.JNodeBuilder;

public abstract class JDirective extends JNode {
    JDirective(JNodeBuilder<?> builder) {
        super(builder);
    }

    @Override
    public boolean match(JNode node) {
        throw new IllegalStateException("Invalid runtime state");
    }
}
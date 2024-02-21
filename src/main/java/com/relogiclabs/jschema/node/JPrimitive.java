package com.relogiclabs.jschema.node;

import com.relogiclabs.jschema.internal.builder.JNodeBuilder;

public abstract class JPrimitive extends JLeaf implements JsonTypable {
    JPrimitive(JNodeBuilder<?> builder) {
        super(builder);
    }

    JPrimitive(JPrimitive node) {
        super(node);
    }

    @Override
    public JNode getNode() {
        return this;
    }
}
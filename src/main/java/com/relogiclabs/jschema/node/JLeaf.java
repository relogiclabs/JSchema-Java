package com.relogiclabs.jschema.node;

import com.relogiclabs.jschema.internal.builder.JNodeBuilder;

public abstract class JLeaf extends JNode {
    JLeaf(JNodeBuilder<?> builder) {
        super(builder);
    }

    JLeaf(JLeaf node) {
        super(node);
    }
}
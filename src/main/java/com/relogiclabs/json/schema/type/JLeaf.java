package com.relogiclabs.json.schema.type;

import com.relogiclabs.json.schema.internal.builder.JNodeBuilder;

public abstract class JLeaf extends JNode {
    JLeaf(JNodeBuilder<?> builder) {
        super(builder);
    }

    JLeaf(JLeaf node) {
        super(node);
    }
}
package com.relogiclabs.json.schema.types;

public abstract class JLeaf extends JNode {
    protected JLeaf(Builder<?> builder) {
        super(builder);
    }

    protected JLeaf(JLeaf node) {
        super(node);
    }
}
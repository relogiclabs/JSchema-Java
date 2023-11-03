package com.relogiclabs.json.schema.types;

import java.util.Collection;

public abstract class JComposite extends JBranch implements JsonTypable {
    protected JComposite(Builder<?> builder) {
        super(builder);
    }

    public abstract Collection<? extends JNode> components();

    @Override
    public JsonType getType() {
        return JsonType.COMPOSITE;
    }

    @Override
    public JNode getNode() {
        return this;
    }
}
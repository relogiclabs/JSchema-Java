package com.relogiclabs.json.schema.type;

import com.relogiclabs.json.schema.internal.builder.JNodeBuilder;

import java.util.Collection;

public abstract class JComposite extends JBranch implements JsonTypable {
    JComposite(JNodeBuilder<?> builder) {
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
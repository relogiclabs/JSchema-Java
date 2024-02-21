package com.relogiclabs.jschema.node;

import com.relogiclabs.jschema.internal.builder.JNodeBuilder;

import java.util.Collection;

public abstract class JComposite extends JBranch implements JsonTypable {
    JComposite(JNodeBuilder<?> builder) {
        super(builder);
    }

    public abstract Collection<? extends JNode> components();

    @Override
    public JNode getNode() {
        return this;
    }
}
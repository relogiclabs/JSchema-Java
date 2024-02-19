package com.relogiclabs.jschema.internal.builder;

import com.relogiclabs.jschema.node.JUndefined;

public final class JUndefinedBuilder extends JNodeBuilder<JUndefinedBuilder> {
    @Override
    public JUndefined build() {
        return JUndefined.from(this);
    }
}
package com.relogiclabs.jschema.internal.builder;

import com.relogiclabs.jschema.node.JNull;

public final class JNullBuilder extends JNodeBuilder<JNullBuilder> {
    @Override
    public JNull build() {
        return JNull.from(this);
    }
}
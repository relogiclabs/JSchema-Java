package com.relogiclabs.jschema.internal.builder;

import com.relogiclabs.jschema.node.JInteger;

public final class JIntegerBuilder extends JPrimitiveBuilder<Long> {
    @Override
    public JInteger build() {
        return JInteger.from(this);
    }
}
package com.relogiclabs.jschema.internal.builder;

import com.relogiclabs.jschema.node.JFloat;

public final class JFloatBuilder extends JPrimitiveBuilder<Double> {
    @Override
    public JFloat build() {
        return JFloat.from(this);
    }
}
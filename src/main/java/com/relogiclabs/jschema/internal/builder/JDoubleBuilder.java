package com.relogiclabs.jschema.internal.builder;

import com.relogiclabs.jschema.node.JDouble;

public final class JDoubleBuilder extends JPrimitiveBuilder<Double> {
    @Override
    public JDouble build() {
        return JDouble.from(this);
    }
}
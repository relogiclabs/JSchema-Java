package com.relogiclabs.json.schema.internal.builder;

import com.relogiclabs.json.schema.type.JDouble;

public class JDoubleBuilder extends JPrimitiveBuilder<Double> {
    @Override
    public JDouble build() {
        return JDouble.from(this);
    }
}
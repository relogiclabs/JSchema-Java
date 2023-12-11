package com.relogiclabs.json.schema.internal.builder;

import com.relogiclabs.json.schema.type.JFloat;

public class JFloatBuilder extends JPrimitiveBuilder<Double> {
    @Override
    public JFloat build() {
        return JFloat.from(this);
    }
}
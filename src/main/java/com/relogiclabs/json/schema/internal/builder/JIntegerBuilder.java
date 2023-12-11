package com.relogiclabs.json.schema.internal.builder;

import com.relogiclabs.json.schema.type.JInteger;

public class JIntegerBuilder extends JPrimitiveBuilder<Long> {
    @Override
    public JInteger build() {
        return JInteger.from(this);
    }
}
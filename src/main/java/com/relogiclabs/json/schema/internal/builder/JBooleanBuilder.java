package com.relogiclabs.json.schema.internal.builder;

import com.relogiclabs.json.schema.type.JBoolean;

public class JBooleanBuilder extends JPrimitiveBuilder<Boolean> {
    @Override
    public JBoolean build() {
        return JBoolean.from(this);
    }
}
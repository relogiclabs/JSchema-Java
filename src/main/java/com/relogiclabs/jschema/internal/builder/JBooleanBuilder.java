package com.relogiclabs.jschema.internal.builder;

import com.relogiclabs.jschema.node.JBoolean;

public final class JBooleanBuilder extends JPrimitiveBuilder<Boolean> {
    @Override
    public JBoolean build() {
        return JBoolean.from(this);
    }
}
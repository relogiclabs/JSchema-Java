package com.relogiclabs.jschema.internal.builder;

import com.relogiclabs.jschema.node.JString;

public final class JStringBuilder extends JPrimitiveBuilder<String> {
    @Override
    public JString build() {
        return JString.from(this);
    }
}
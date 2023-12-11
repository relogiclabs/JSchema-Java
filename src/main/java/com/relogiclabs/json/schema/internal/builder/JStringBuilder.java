package com.relogiclabs.json.schema.internal.builder;

import com.relogiclabs.json.schema.type.JString;

public class JStringBuilder extends JPrimitiveBuilder<String> {
    @Override
    public JString build() {
        return JString.from(this);
    }
}
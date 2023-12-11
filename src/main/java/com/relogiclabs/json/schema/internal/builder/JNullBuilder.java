package com.relogiclabs.json.schema.internal.builder;

import com.relogiclabs.json.schema.type.JNull;

public class JNullBuilder extends JNodeBuilder<JNullBuilder> {
    @Override
    public JNull build() {
        return JNull.from(this);
    }
}
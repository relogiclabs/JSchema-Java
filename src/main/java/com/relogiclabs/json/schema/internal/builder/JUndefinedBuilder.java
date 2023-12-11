package com.relogiclabs.json.schema.internal.builder;

import com.relogiclabs.json.schema.type.JUndefined;

public class JUndefinedBuilder extends JNodeBuilder<JUndefinedBuilder> {
    @Override
    public JUndefined build() {
        return JUndefined.from(this);
    }
}
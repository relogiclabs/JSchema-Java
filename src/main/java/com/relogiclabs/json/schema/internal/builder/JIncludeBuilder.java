package com.relogiclabs.json.schema.internal.builder;

import com.relogiclabs.json.schema.type.JInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter
@Accessors(fluent = true)
public class JIncludeBuilder extends JNodeBuilder<JIncludeBuilder> {
    private String className;

    @Override
    public JInclude build() {
        return JInclude.from(this);
    }
}
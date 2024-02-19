package com.relogiclabs.jschema.internal.builder;

import com.relogiclabs.jschema.node.JVersion;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter
@Accessors(fluent = true)
public final class JVersionBuilder extends JNodeBuilder<JVersionBuilder> {
    private String version;

    @Override
    public JVersion build() {
        return JVersion.from(this);
    }
}
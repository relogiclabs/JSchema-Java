package com.relogiclabs.jschema.internal.builder;

import com.relogiclabs.jschema.node.JAlias;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter
@Accessors(fluent = true)
public final class JAliasBuilder extends JNodeBuilder<JAliasBuilder> {
    private String name;

    @Override
    public JAlias build() {
        return JAlias.from(this);
    }
}
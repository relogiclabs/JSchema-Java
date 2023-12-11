package com.relogiclabs.json.schema.internal.builder;

import com.relogiclabs.json.schema.type.JAlias;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter
@Accessors(fluent = true)
public class JAliasBuilder extends JNodeBuilder<JAliasBuilder> {
    private String name;

    @Override
    public JAlias build() {
        return JAlias.from(this);
    }
}
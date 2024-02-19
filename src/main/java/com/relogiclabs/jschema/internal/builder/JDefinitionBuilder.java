package com.relogiclabs.jschema.internal.builder;

import com.relogiclabs.jschema.node.JAlias;
import com.relogiclabs.jschema.node.JDefinition;
import com.relogiclabs.jschema.node.JValidator;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter
@Accessors(fluent = true)
public final class JDefinitionBuilder extends JNodeBuilder<JDefinitionBuilder> {
    private JAlias alias;
    private JValidator validator;

    @Override
    public JDefinition build() {
        return JDefinition.from(this);
    }
}
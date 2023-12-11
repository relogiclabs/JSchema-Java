package com.relogiclabs.json.schema.internal.builder;

import com.relogiclabs.json.schema.type.JAlias;
import com.relogiclabs.json.schema.type.JDefinition;
import com.relogiclabs.json.schema.type.JValidator;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter
@Accessors(fluent = true)
public class JDefinitionBuilder extends JNodeBuilder<JDefinitionBuilder> {
    private JAlias alias;
    private JValidator validator;

    @Override
    public JDefinition build() {
        return JDefinition.from(this);
    }
}
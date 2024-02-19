package com.relogiclabs.jschema.internal.builder;

import com.relogiclabs.jschema.node.JAlias;
import com.relogiclabs.jschema.node.JDataType;
import com.relogiclabs.jschema.node.JsonType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter
@Accessors(fluent = true)
public final class JDataTypeBuilder extends JNodeBuilder<JDataTypeBuilder> {
    private JsonType jsonType;
    private Boolean nested;
    private JAlias alias;

    @Override
    public JDataType build() {
        return JDataType.from(this);
    }
}
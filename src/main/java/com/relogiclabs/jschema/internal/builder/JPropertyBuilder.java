package com.relogiclabs.jschema.internal.builder;

import com.relogiclabs.jschema.node.JNode;
import com.relogiclabs.jschema.node.JProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter
@Accessors(fluent = true)
public final class JPropertyBuilder extends JNodeBuilder<JPropertyBuilder> {
    private String key;
    private JNode value;

    @Override
    public JProperty build() {
        return JProperty.from(this);
    }
}
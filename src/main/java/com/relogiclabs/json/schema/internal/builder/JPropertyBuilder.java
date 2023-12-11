package com.relogiclabs.json.schema.internal.builder;

import com.relogiclabs.json.schema.type.JNode;
import com.relogiclabs.json.schema.type.JProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter
@Accessors(fluent = true)
public class JPropertyBuilder extends JNodeBuilder<JPropertyBuilder> {
    private String key;
    private JNode value;

    @Override
    public JProperty build() {
        return JProperty.from(this);
    }
}
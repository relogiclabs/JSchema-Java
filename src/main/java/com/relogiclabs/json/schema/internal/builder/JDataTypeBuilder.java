package com.relogiclabs.json.schema.internal.builder;

import com.relogiclabs.json.schema.type.JAlias;
import com.relogiclabs.json.schema.type.JDataType;
import com.relogiclabs.json.schema.type.JsonType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter
@Accessors(fluent = true)
public class JDataTypeBuilder extends JNodeBuilder<JDataTypeBuilder> {
    private JsonType jsonType;
    private Boolean nested;
    private JAlias alias;

    @Override
    public JDataType build() {
        return JDataType.from(this);
    }
}
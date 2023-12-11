package com.relogiclabs.json.schema.internal.builder;

import com.relogiclabs.json.schema.type.JArray;
import com.relogiclabs.json.schema.type.JNode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter @Setter
@Accessors(fluent = true)
public class JArrayBuilder extends JNodeBuilder<JArrayBuilder> {
    private List<JNode> elements;

    @Override
    public JArray build() {
        return JArray.from(this);
    }
}
package com.relogiclabs.jschema.internal.builder;

import com.relogiclabs.jschema.node.JArray;
import com.relogiclabs.jschema.node.JNode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter @Setter
@Accessors(fluent = true)
public final class JArrayBuilder extends JNodeBuilder<JArrayBuilder> {
    private List<JNode> elements;

    @Override
    public JArray build() {
        return JArray.from(this);
    }
}
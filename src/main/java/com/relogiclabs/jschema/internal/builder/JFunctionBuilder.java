package com.relogiclabs.jschema.internal.builder;

import com.relogiclabs.jschema.node.JFunction;
import com.relogiclabs.jschema.node.JNode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter @Setter
@Accessors(fluent = true)
public final class JFunctionBuilder extends JNodeBuilder<JFunctionBuilder> {
    private String name;
    private Boolean nested;
    private List<JNode> arguments;

    @Override
    public JFunction build() {
        return JFunction.from(this);
    }
}
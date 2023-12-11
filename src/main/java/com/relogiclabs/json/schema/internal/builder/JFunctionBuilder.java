package com.relogiclabs.json.schema.internal.builder;

import com.relogiclabs.json.schema.type.JFunction;
import com.relogiclabs.json.schema.type.JNode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter @Setter
@Accessors(fluent = true)
public class JFunctionBuilder extends JNodeBuilder<JFunctionBuilder> {
    private String name;
    private Boolean nested;
    private List<JNode> arguments;

    @Override
    public JFunction build() {
        return JFunction.from(this);
    }
}
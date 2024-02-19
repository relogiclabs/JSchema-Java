package com.relogiclabs.jschema.internal.builder;

import com.relogiclabs.jschema.node.JDataType;
import com.relogiclabs.jschema.node.JFunction;
import com.relogiclabs.jschema.node.JNode;
import com.relogiclabs.jschema.node.JReceiver;
import com.relogiclabs.jschema.node.JValidator;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter @Setter
@Accessors(fluent = true)
public final class JValidatorBuilder extends JNodeBuilder<JValidatorBuilder> {
    private JNode value;
    private List<JFunction> functions;
    private List<JDataType> dataTypes;
    private List<JReceiver> receivers;
    private boolean optional;

    @Override
    public JValidator build() {
        return JValidator.from(this);
    }
}
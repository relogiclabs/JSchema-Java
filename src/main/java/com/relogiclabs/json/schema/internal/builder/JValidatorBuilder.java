package com.relogiclabs.json.schema.internal.builder;

import com.relogiclabs.json.schema.type.JDataType;
import com.relogiclabs.json.schema.type.JFunction;
import com.relogiclabs.json.schema.type.JNode;
import com.relogiclabs.json.schema.type.JReceiver;
import com.relogiclabs.json.schema.type.JValidator;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter @Setter
@Accessors(fluent = true)
public class JValidatorBuilder extends JNodeBuilder<JValidatorBuilder> {
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
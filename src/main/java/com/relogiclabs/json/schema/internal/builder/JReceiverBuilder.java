package com.relogiclabs.json.schema.internal.builder;

import com.relogiclabs.json.schema.type.JReceiver;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter
@Accessors(fluent = true)
public class JReceiverBuilder extends JNodeBuilder<JReceiverBuilder> {
    private String name;

    @Override
    public JReceiver build() {
        return JReceiver.from(this);
    }
}
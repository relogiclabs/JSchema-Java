package com.relogiclabs.jschema.internal.builder;

import com.relogiclabs.jschema.node.JReceiver;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter
@Accessors(fluent = true)
public final class JReceiverBuilder extends JNodeBuilder<JReceiverBuilder> {
    private String name;

    @Override
    public JReceiver build() {
        return JReceiver.from(this);
    }
}
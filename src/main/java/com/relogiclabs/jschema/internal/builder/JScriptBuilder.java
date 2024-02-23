package com.relogiclabs.jschema.internal.builder;

import com.relogiclabs.jschema.internal.engine.Evaluator;
import com.relogiclabs.jschema.node.JNode;
import com.relogiclabs.jschema.node.JScript;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter
@Accessors(fluent = true)
public final class JScriptBuilder extends JNodeBuilder<JScriptBuilder> {
    private Evaluator evaluator;
    private String source;

    @Override
    public JNode build() {
        return JScript.from(this);
    }
}
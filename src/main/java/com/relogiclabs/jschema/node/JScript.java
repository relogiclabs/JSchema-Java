package com.relogiclabs.jschema.node;

import com.relogiclabs.jschema.internal.builder.JScriptBuilder;
import com.relogiclabs.jschema.internal.engine.Evaluator;
import lombok.EqualsAndHashCode;

import static java.util.Objects.requireNonNull;

@EqualsAndHashCode
public final class JScript extends JDirective {
    private final Evaluator evaluator;
    private final String source;

    private JScript(JScriptBuilder builder) {
        super(builder);
        this.evaluator = requireNonNull(builder.evaluator());
        this.source = requireNonNull(builder.source());
    }

    public static JScript from(JScriptBuilder builder) {
        return new JScript(builder).initialize();
    }

    @Override
    public String toString() {
        return source;
    }
}
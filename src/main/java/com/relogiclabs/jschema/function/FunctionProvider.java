package com.relogiclabs.jschema.function;

import com.relogiclabs.jschema.node.JFunction;
import com.relogiclabs.jschema.tree.RuntimeContext;
import lombok.Getter;
import lombok.Setter;

@Getter
public abstract class FunctionProvider {
    protected final RuntimeContext runtime;
    @Setter protected JFunction caller;

    public FunctionProvider(RuntimeContext runtime) {
        this.runtime = runtime;
    }

    protected boolean fail(RuntimeException exception) {
        return runtime.getExceptions().fail(exception);
    }
}
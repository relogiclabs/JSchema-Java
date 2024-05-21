package com.relogiclabs.jschema.function;

import com.relogiclabs.jschema.node.JFunction;
import com.relogiclabs.jschema.tree.RuntimeContext;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class FunctionProvider {
    protected RuntimeContext runtime;
    protected JFunction caller;

    protected boolean fail(RuntimeException exception) {
        return runtime.getExceptions().fail(exception);
    }
}
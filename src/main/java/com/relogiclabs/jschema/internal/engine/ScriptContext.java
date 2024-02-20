package com.relogiclabs.jschema.internal.engine;

import com.relogiclabs.jschema.tree.RuntimeContext;
import lombok.Getter;

@Getter
public class ScriptContext extends ScopeContext {
    private final RuntimeContext runtime;

    public ScriptContext(RuntimeContext runtime) {
        super(null);
        this.runtime = runtime;
    }
}
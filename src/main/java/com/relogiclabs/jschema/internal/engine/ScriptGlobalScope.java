package com.relogiclabs.jschema.internal.engine;

import com.relogiclabs.jschema.tree.RuntimeContext;
import lombok.Getter;

@Getter
public class ScriptGlobalScope extends ScriptScope {
    private final RuntimeContext runtime;

    public ScriptGlobalScope(RuntimeContext runtime) {
        super(null);
        this.runtime = runtime;
    }
}
package com.relogiclabs.jschema.internal.library;

import com.relogiclabs.jschema.extension.ScriptFunctions;
import com.relogiclabs.jschema.internal.engine.ScriptScope;
import com.relogiclabs.jschema.tree.RuntimeContext;
import com.relogiclabs.jschema.type.EValue;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;

@Getter @Setter
public abstract class AbstractFunctions implements ScriptFunctions {
    private ScriptScope scope;
    private Method method;

    @Setter(AccessLevel.NONE)
    private RuntimeContext runtime;

    @Override
    public void setScope(ScriptScope scope) {
        this.scope = scope;
        this.runtime = null;
    }

    @Override
    public RuntimeContext getRuntime() {
        if(runtime != null) return runtime;
        return runtime = scope.getRuntime();
    }

    protected EValue resolve(String key) {
        return scope.resolve(key);
    }

    protected void fail(RuntimeException exception) {
        getRuntime().getExceptions().fail(exception);
    }
}
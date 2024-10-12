package com.relogiclabs.jschema.extension;

import com.relogiclabs.jschema.internal.engine.ScriptScope;

public interface ScriptFunctions extends InvokableNative {
    ScriptScope getScope();
    void setScope(ScriptScope scope);

    default void setContext(ScriptFunctions context) {
        setScope(context.getScope());
        setMethod(context.getMethod());
    }
}
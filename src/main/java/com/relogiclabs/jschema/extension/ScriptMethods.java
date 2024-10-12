package com.relogiclabs.jschema.extension;

import com.relogiclabs.jschema.type.EValue;

public interface ScriptMethods extends ScriptFunctions {
    EValue getSelf();
    void setSelf(EValue self);

    default void setContext(ScriptMethods context) {
        setSelf(context.getSelf());
        setScope(context.getScope());
        setMethod(context.getMethod());
    }
}
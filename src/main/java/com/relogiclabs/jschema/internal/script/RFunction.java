package com.relogiclabs.jschema.internal.script;

import com.relogiclabs.jschema.internal.engine.ScriptScope;
import com.relogiclabs.jschema.type.EValue;

import java.util.List;

public interface RFunction extends EValue {
    GParameter[] getParameters();
    boolean isVariadic();
    ScriptScope bind(ScriptScope parentScope, List<EValue> arguments);
    EValue invoke(ScriptScope functionScope, List<EValue> arguments);

    static boolean hasVariadic(GParameter[] parameters) {
        return parameters.length != 0 && parameters[parameters.length - 1].isVariadic();
    }
}
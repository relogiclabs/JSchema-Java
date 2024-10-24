package com.relogiclabs.jschema.internal.script;

import com.relogiclabs.jschema.internal.engine.ScriptScope;
import com.relogiclabs.jschema.type.EType;
import com.relogiclabs.jschema.type.EValue;

import java.util.List;

public interface RFunction extends EValue {
    EValue invoke(List<EValue> arguments, ScriptScope parentScope);

    @Override
    default EType getType() {
        return EType.INVOCABLE;
    }
}
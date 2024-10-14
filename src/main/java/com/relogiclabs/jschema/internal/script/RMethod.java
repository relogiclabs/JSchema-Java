package com.relogiclabs.jschema.internal.script;

import com.relogiclabs.jschema.internal.engine.ScriptScope;
import com.relogiclabs.jschema.type.EType;
import com.relogiclabs.jschema.type.EValue;

import java.util.List;

public interface RMethod extends EValue {
    EValue invoke(EValue self, List<EValue> arguments, ScriptScope parentScope);

    @Override
    default EType getType() {
        return EType.INVOCABLE;
    }
}
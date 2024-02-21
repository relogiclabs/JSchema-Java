package com.relogiclabs.jschema.internal.script;

import com.relogiclabs.jschema.internal.engine.ScopeContext;
import com.relogiclabs.jschema.type.EValue;

import java.util.List;

public interface RFunction extends EValue {
    ScopeContext bind(ScopeContext parentScope, List<EValue> arguments);
    EValue invoke(ScopeContext functionScope, List<EValue> arguments);
    GParameter[] getParameters();
    boolean isVariadic();
}
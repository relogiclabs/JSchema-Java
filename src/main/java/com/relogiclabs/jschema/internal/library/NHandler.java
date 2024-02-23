package com.relogiclabs.jschema.internal.library;

import com.relogiclabs.jschema.internal.engine.ScopeContext;
import com.relogiclabs.jschema.type.EValue;

import java.util.List;

@FunctionalInterface
public interface NHandler {
    EValue invoke(ScopeContext scope, List<EValue> arguments);
}
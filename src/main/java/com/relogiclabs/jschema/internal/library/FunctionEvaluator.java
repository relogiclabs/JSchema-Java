package com.relogiclabs.jschema.internal.library;

import com.relogiclabs.jschema.internal.engine.ScriptScope;
import com.relogiclabs.jschema.type.EValue;

import java.util.List;

@FunctionalInterface
public interface FunctionEvaluator {
    EValue evaluate(ScriptScope scope, List<EValue> arguments);
}
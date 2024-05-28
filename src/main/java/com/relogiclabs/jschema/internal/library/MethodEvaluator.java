package com.relogiclabs.jschema.internal.library;

import com.relogiclabs.jschema.internal.engine.ScriptScope;
import com.relogiclabs.jschema.type.EValue;

import java.util.List;

@FunctionalInterface
public interface MethodEvaluator {
    EValue evaluate(EValue self, List<EValue> arguments, ScriptScope scope);
}
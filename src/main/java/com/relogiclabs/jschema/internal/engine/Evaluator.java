package com.relogiclabs.jschema.internal.engine;

import com.relogiclabs.jschema.type.EValue;

@FunctionalInterface
public interface Evaluator {
    EValue evaluate(ScriptScope scope);
}
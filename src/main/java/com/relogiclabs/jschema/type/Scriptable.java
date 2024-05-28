package com.relogiclabs.jschema.type;

import com.relogiclabs.jschema.internal.library.MethodEvaluator;

interface Scriptable {
    MethodEvaluator getMethod(String name, int argCount);
}
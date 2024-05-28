package com.relogiclabs.jschema.type;

import com.relogiclabs.jschema.internal.library.MethodEvaluator;
import com.relogiclabs.jschema.internal.library.ObjectLibrary;

import java.util.Set;

public interface EObject extends EValue {
    Set<String> keySet();
    EValue get(String key);
    void set(String key, EValue value);
    int size();

    @Override
    default EType getType() {
        return EType.OBJECT;
    }

    @Override
    default MethodEvaluator getMethod(String name, int argCount) {
        return ObjectLibrary.getInstance().getMethod(name, argCount);
    }
}
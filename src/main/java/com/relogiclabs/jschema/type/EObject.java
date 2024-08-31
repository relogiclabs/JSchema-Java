package com.relogiclabs.jschema.type;

import com.relogiclabs.jschema.exception.MethodNotFoundException;
import com.relogiclabs.jschema.internal.library.MethodEvaluator;
import com.relogiclabs.jschema.internal.library.ObjectMethods;

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
        try {
            return ObjectMethods.getInstance().getMethod(name, argCount);
        } catch(MethodNotFoundException e) {
            e.setType(getType());
            throw e;
        }
    }
}
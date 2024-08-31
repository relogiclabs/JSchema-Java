package com.relogiclabs.jschema.type;

import com.relogiclabs.jschema.exception.MethodNotFoundException;
import com.relogiclabs.jschema.internal.library.MethodEvaluator;
import com.relogiclabs.jschema.internal.library.NumberMethods;

public interface ENumber extends EValue {
    double toDouble();

    @Override
    default EType getType() {
        return EType.NUMBER;
    }

    @Override
    default MethodEvaluator getMethod(String name, int argCount) {
        try {
            return NumberMethods.getInstance().getMethod(name, argCount);
        } catch(MethodNotFoundException e) {
            e.setType(getType());
            throw e;
        }
    }
}
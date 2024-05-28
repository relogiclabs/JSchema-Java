package com.relogiclabs.jschema.type;

import com.relogiclabs.jschema.internal.library.MethodEvaluator;
import com.relogiclabs.jschema.internal.library.NumberLibrary;

public interface ENumber extends EValue {
    double toDouble();

    @Override
    default EType getType() {
        return EType.NUMBER;
    }

    @Override
    default MethodEvaluator getMethod(String name, int argCount) {
        return NumberLibrary.getInstance().getMethod(name, argCount);
    }
}
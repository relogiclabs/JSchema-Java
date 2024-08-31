package com.relogiclabs.jschema.type;

import com.relogiclabs.jschema.exception.MethodNotFoundException;
import com.relogiclabs.jschema.internal.library.MethodEvaluator;
import com.relogiclabs.jschema.internal.library.StringMethods;

public interface EString extends EValue {
    String getValue();

    default int length() {
        return getValue().length();
    }

    @Override
    default EType getType() {
        return EType.STRING;
    }

    @Override
    default MethodEvaluator getMethod(String name, int argCount) {
        try {
            return StringMethods.getInstance().getMethod(name, argCount);
        } catch(MethodNotFoundException e) {
            e.setType(getType());
            throw e;
        }
    }
}
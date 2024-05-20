package com.relogiclabs.jschema.type;

import com.relogiclabs.jschema.internal.library.MethodEvaluator;
import com.relogiclabs.jschema.internal.library.StringLibrary;

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
        return StringLibrary.getInstance().getMethod(name, argCount);
    }
}
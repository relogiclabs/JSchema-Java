package com.relogiclabs.jschema.type;

import com.relogiclabs.jschema.internal.library.ArrayLibrary;
import com.relogiclabs.jschema.internal.library.MethodEvaluator;

import java.util.List;

public interface EArray extends EValue {
    List<? extends EValue> elements();
    EValue get(int index);
    void set(int index, EValue value);
    default int size() {
        return elements().size();
    }

    @Override
    default EType getType() {
        return EType.ARRAY;
    }

    @Override
    default MethodEvaluator getMethod(String name, int argCount) {
        return ArrayLibrary.getInstance().getMethod(name, argCount);
    }
}
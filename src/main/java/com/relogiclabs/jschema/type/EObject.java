package com.relogiclabs.jschema.type;

import java.util.Set;

public interface EObject extends EValue {
    Set<String> keySet();
    EValue get(String key);
    void set(String key, EValue value);
    int size();

    default boolean isReadonly() {
        return true;
    }

    @Override
    default EType getType() {
        return EType.OBJECT;
    }
}
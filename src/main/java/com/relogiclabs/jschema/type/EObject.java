package com.relogiclabs.jschema.type;

import java.util.Set;

public interface EObject extends EValue {
    EValue get(String key);
    int size();
    Set<String> keySet();

    @Override
    default EType getType() {
        return EType.OBJECT;
    }
}
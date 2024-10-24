package com.relogiclabs.jschema.type;

import java.util.List;

public interface EArray extends EValue {
    List<? extends EValue> elements();
    EValue get(int index);
    void set(int index, EValue value);

    default boolean isReadonly() {
        return true;
    }

    default int size() {
        return elements().size();
    }

    @Override
    default EType getType() {
        return EType.ARRAY;
    }
}
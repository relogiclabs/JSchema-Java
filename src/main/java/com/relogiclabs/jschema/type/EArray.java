package com.relogiclabs.jschema.type;

import java.util.List;

public interface EArray extends EValue {
    EValue get(int index);
    List<? extends EValue> elements();

    default int size() {
        return elements().size();
    }

    @Override
    default EType getType() {
        return EType.ARRAY;
    }
}
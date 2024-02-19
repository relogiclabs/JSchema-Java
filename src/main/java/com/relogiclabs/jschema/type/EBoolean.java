package com.relogiclabs.jschema.type;

public interface EBoolean extends EValue {
    boolean getValue();

    @Override
    default boolean toBoolean() {
        return getValue();
    }

    @Override
    default EType getType() {
        return EType.BOOLEAN;
    }
}
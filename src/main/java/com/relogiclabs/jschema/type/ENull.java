package com.relogiclabs.jschema.type;

public interface ENull extends EValue {
    String STRING = "null";
    ENull NULL = new ENull() {
        @Override
        public String toString() {
            return STRING;
        }
    };

    @Override
    default EType getType() {
        return EType.NULL;
    }

    @Override
    default boolean toBoolean() {
        return false;
    }
}
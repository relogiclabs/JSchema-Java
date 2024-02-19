package com.relogiclabs.jschema.type;

public interface EUndefined extends EValue {
    String STRING = "undefined";
    EUndefined UNDEFINED = new EUndefined() {
        @Override
        public String toString() {
            return STRING;
        }
    };

    @Override
    default EType getType() {
        return EType.UNDEFINED;
    }

    @Override
    default boolean toBoolean() {
        return false;
    }
}
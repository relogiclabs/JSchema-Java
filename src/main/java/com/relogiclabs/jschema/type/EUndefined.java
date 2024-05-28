package com.relogiclabs.jschema.type;

public interface EUndefined extends EValue {
    String LITERAL = "undefined";
    String MARKER = "!";
    EUndefined UNDEFINED = new EUndefined() {
        @Override
        public String toString() {
            return LITERAL;
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
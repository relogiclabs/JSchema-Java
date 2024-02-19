package com.relogiclabs.jschema.type;

public interface EValue {
    EValue VOID = new EValue() {
        @Override
        public EType getType() {
            return EType.VOID;
        }

        @Override
        public boolean toBoolean() {
            return false;
        }

        @Override
        public String toString() {
            return EType.VOID.toString();
        }
    };

    default EType getType() {
        return EType.ANY;
    }

    default boolean toBoolean() {
        return true;
    }
}
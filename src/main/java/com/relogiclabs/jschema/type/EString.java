package com.relogiclabs.jschema.type;

public interface EString extends EValue {
    String getValue();

    default int length() {
        return getValue().length();
    }

    @Override
    default EType getType() {
        return EType.STRING;
    }
}
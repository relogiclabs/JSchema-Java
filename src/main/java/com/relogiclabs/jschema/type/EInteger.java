package com.relogiclabs.jschema.type;

public interface EInteger extends ENumber {
    long getValue();

    @Override
    default double toDouble() {
        return getValue();
    }

    @Override
    default EType getType() {
        return EType.INTEGER;
    }
}
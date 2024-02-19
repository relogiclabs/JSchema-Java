package com.relogiclabs.jschema.type;

public interface EDouble extends ENumber {
    double getValue();

    @Override
    default double toDouble() {
        return getValue();
    }

    @Override
    default EType getType() {
        return EType.DOUBLE;
    }
}
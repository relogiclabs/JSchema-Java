package com.relogiclabs.jschema.type;

public interface ENumber extends EValue {
    double toDouble();

    @Override
    default EType getType() {
        return EType.NUMBER;
    }
}
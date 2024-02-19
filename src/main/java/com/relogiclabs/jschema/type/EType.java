package com.relogiclabs.jschema.type;

import lombok.Getter;

@Getter
public enum EType {
    NUMBER("#number"),
    INTEGER("#integer"),
    FLOAT("#float"),
    DOUBLE("#double"),
    STRING("#string"),
    ARRAY("#array"),
    RANGE("#range"),
    OBJECT("#object"),
    BOOLEAN("#boolean"),
    DATETIME("#datetime"),
    DATE("#date"),
    TIME("#time"),
    PRIMITIVE("#primitive"),
    COMPOSITE("#composite"),
    ANY("#any"),
    NULL("#null"),
    UNDEFINED("#undefined"),
    VOID("#void");

    private final String name;

    EType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
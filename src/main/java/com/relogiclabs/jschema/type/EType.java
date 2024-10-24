package com.relogiclabs.jschema.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EType {
    ANY("#any", null),
    PRIMITIVE("#primitive", ANY),
    NUMBER("#number", PRIMITIVE),
    INTEGER("#integer", NUMBER),
    FLOAT("#float", NUMBER),
    DOUBLE("#double", NUMBER),
    BOOLEAN("#boolean", PRIMITIVE),
    STRING("#string", PRIMITIVE),
    DATETIME("#datetime", STRING),
    DATE("#date", DATETIME),
    TIME("#time", DATETIME),
    NULL("#null", PRIMITIVE),
    UNDEFINED("#undefined", PRIMITIVE),
    COMPOSITE("#composite", ANY),
    ARRAY("#array", COMPOSITE),
    OBJECT("#object", COMPOSITE),
    RANGE("#range", ANY),
    NATIVE("#native", ANY),
    INVOCABLE("#invocable", null),
    VOID("#void", null);

    public static final int count = EType.values().length;

    private final String name;
    private final EType parent;

    @Override
    public String toString() {
        return name;
    }
}
package com.relogiclabs.jschema.internal.script;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DataOwner {
    EXPECTED_FUNCTION("expected function"),
    ACTUAL_FUNCTION("actual function");

    private final String name;

    @Override
    public String toString() {
        return name;
    }
}
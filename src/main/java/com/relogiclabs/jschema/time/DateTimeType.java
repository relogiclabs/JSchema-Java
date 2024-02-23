package com.relogiclabs.jschema.time;

import com.relogiclabs.jschema.type.EType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.relogiclabs.jschema.type.EType.DATE;
import static com.relogiclabs.jschema.type.EType.TIME;

@Getter
@AllArgsConstructor
public enum DateTimeType {
    DATE_TYPE("date", DATE),
    TIME_TYPE("time", TIME);

    private final String name;
    private final EType type;

    @Override
    public String toString() {
        return name;
    }
}
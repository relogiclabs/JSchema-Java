package com.relogiclabs.json.schema.internal.time;

import com.relogiclabs.json.schema.types.JsonType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.relogiclabs.json.schema.types.JsonType.DATE;
import static com.relogiclabs.json.schema.types.JsonType.TIME;

@Getter
@AllArgsConstructor
public enum DateTimeType {
    DATE_TYPE("date", DATE),
    TIME_TYPE("time", TIME);

    private final String name;
    private final JsonType type;

    @Override
    public String toString() {
        return name;
    }
}
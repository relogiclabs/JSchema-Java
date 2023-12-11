package com.relogiclabs.json.schema.time;

import com.relogiclabs.json.schema.type.JsonType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.relogiclabs.json.schema.type.JsonType.DATE;
import static com.relogiclabs.json.schema.type.JsonType.TIME;

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
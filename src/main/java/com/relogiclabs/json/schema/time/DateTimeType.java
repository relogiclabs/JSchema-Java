package com.relogiclabs.json.schema.time;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DateTimeType {
    DATE_TYPE("date"),
    TIME_TYPE("time");

    private final String name;

    @Override
    public String toString() {
        return name;
    }
}
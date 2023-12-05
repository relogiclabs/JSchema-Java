package com.relogiclabs.json.schema.tree;

import lombok.Getter;

@Getter
public enum TreeType {
    SCHEMA_TREE("Schema"),
    JSON_TREE("Json");

    private final String name;

    TreeType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
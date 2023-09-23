package com.relogiclabs.json.schema.types;

public interface JsonTypable {
    JsonType getType();
    JNode getNode();
}

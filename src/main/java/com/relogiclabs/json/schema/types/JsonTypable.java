package com.relogiclabs.json.schema.types;

public interface JsonTypable {
    default JsonType getType() {
        return JsonType.ANY;
    }
    JNode getNode();
}
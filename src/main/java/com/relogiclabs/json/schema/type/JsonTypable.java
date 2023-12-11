package com.relogiclabs.json.schema.type;

public interface JsonTypable {
    default JsonType getType() {
        return JsonType.ANY;
    }
    JNode getNode();
}
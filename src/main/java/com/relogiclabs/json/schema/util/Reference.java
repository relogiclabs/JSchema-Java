package com.relogiclabs.json.schema.util;

import lombok.Data;

@Data
public final class Reference<V> {
    private V value;

    public Reference() {
        value = null;
    }

    public Reference(V initialValue) {
        this.value = initialValue;
    }
}
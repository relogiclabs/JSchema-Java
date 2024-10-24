package com.relogiclabs.jschema.internal.loader;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class ConstraintStorage {
    private final Map<String, List<SchemaFunction>> functions;

    public ConstraintStorage(int capacity) {
        this.functions = new HashMap<>(capacity);
    }

    public void addFunction(SchemaFunction function) {
        for(var key : function.getKeys()) {
            var list = functions.get(key);
            if(list == null) list = new ArrayList<>(10);
            list.add(function);
            functions.put(key, list);
        }
    }

    public List<SchemaFunction> getFunction(String key) {
        return functions.get(key);
    }
}
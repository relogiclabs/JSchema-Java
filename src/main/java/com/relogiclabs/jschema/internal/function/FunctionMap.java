package com.relogiclabs.jschema.internal.function;

import com.relogiclabs.jschema.internal.tree.EFunction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class FunctionMap {
    private final Map<String, List<EFunction>> functions = new HashMap<>();

    public void add(EFunction function) {
        var key = FunctionId.generate(function);
        var list = functions.get(key);
        if(list == null) list = new ArrayList<>();
        list.add(function);
        functions.put(key, list);
    }

    public List<EFunction> get(String key) {
        return functions.get(key);
    }

    public void mergeWith(FunctionMap other) {
        for(var entry : other.functions.entrySet()) {
            var list = functions.get(entry.getKey());
            if(list == null) functions.put(entry.getKey(), entry.getValue());
            else list.addAll(entry.getValue());
        }
    }
}
package com.relogiclabs.jschema.internal.loader;

import com.relogiclabs.jschema.exception.DuplicateFunctionException;
import com.relogiclabs.jschema.exception.DuplicateMethodException;
import com.relogiclabs.jschema.internal.script.RFunction;
import com.relogiclabs.jschema.internal.script.RMethod;

import java.util.HashMap;
import java.util.Map;

import static com.relogiclabs.jschema.message.ErrorCode.FNCDUP01;
import static com.relogiclabs.jschema.message.ErrorCode.MTHDUP01;

public class ScriptStorage {
    private final Map<String, RFunction> functions;
    private final Map<String, RMethod> methods;

    public ScriptStorage(int functionCapacity, int methodCapacity) {
        this.functions = new HashMap<>(functionCapacity);
        this.methods = new HashMap<>(methodCapacity);
    }

    public void addFunction(ScriptFunctionWrapper wrapper) {
        for(var key : wrapper.getKeys()) {
            var oldValue = functions.put(key, wrapper);
            if(oldValue != null) throw new DuplicateFunctionException(FNCDUP01,
                "Script function: " + wrapper.getSignature()
                    + " with matching parameter(s) already defined");
        }
    }

    public void addMethod(ScriptMethodWrapper wrapper) {
        for(var key : wrapper.getKeys()) {
            var oldValue = methods.put(key, wrapper);
            if(oldValue != null) throw new DuplicateMethodException(MTHDUP01,
                "Script method: " + wrapper.getSignature()
                    + " with matching parameter(s) already defined");
        }
    }

    public RFunction getFunction(String key) {
        return functions.get(key);
    }

    public RMethod getMethod(String key) {
        return methods.get(key);
    }
}
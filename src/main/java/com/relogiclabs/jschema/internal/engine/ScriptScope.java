package com.relogiclabs.jschema.internal.engine;

import com.relogiclabs.jschema.exception.DuplicateFunctionException;
import com.relogiclabs.jschema.exception.DuplicateVariableException;
import com.relogiclabs.jschema.internal.script.GFunction;
import com.relogiclabs.jschema.internal.script.GLeftValue;
import com.relogiclabs.jschema.tree.RuntimeContext;
import com.relogiclabs.jschema.type.EValue;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

import static com.relogiclabs.jschema.internal.loader.SchemaFunction.CONSTRAINT_PREFIX;
import static com.relogiclabs.jschema.message.ErrorCode.FNSDUP01;
import static com.relogiclabs.jschema.message.ErrorCode.VARDUP01;
import static org.apache.commons.lang3.StringUtils.substringBefore;

public class ScriptScope {
    private static final String CONSTRAINT_TYPE = "Constraint";
    private static final String SUBROUTINE_TYPE = "Subroutine";

    @Getter private final ScriptScope parent;
    private final Map<String, EValue> symbols;

    public ScriptScope(ScriptScope parent) {
        this.parent = parent;
        this.symbols = new HashMap<>();
    }

    public GLeftValue addVariable(String key, EValue value) {
        var lvalue = new GLeftValue(value);
        var oldValue = symbols.put(key, lvalue);
        if(oldValue == null) return lvalue;
        throw new DuplicateVariableException(VARDUP01,
            "Variable '" + key + "' already declared in the scope");
    }

    public void addFunction(String key, GFunction function) {
        var oldValue = symbols.put(key, function);
        if(oldValue != null) throw failOnDuplicateFunction(key);
    }

    private static DuplicateFunctionException failOnDuplicateFunction(String key) {
        return new DuplicateFunctionException(FNSDUP01,
            (key.startsWith(CONSTRAINT_PREFIX) ? CONSTRAINT_TYPE : SUBROUTINE_TYPE)
                + " function '" + substringBefore(key, '#')
                + "' with matching parameter(s) already defined");
    }

    public EValue resolve(String key) {
        var current = this;
        do {
            var value = current.symbols.get(key);
            if(value != null) return value;
            current = current.parent;
        } while(current != null);
        return null;
    }

    public EValue resolve(String key1, String key2) {
        var value = resolve(key1);
        if(value != null) return value;
        return resolve(key2);
    }

    public RuntimeContext getRuntime() {
        var current = this;
        while(current.parent != null) current = current.parent;
        return current.getRuntime();
    }

    public void update(String key, EValue value) {
        symbols.put(key, value);
    }
}
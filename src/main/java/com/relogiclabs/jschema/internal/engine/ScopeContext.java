package com.relogiclabs.jschema.internal.engine;

import com.relogiclabs.jschema.exception.ScriptFunctionException;
import com.relogiclabs.jschema.exception.ScriptVariableException;
import com.relogiclabs.jschema.internal.script.GFunction;
import com.relogiclabs.jschema.internal.script.GReference;
import com.relogiclabs.jschema.tree.RuntimeContext;
import com.relogiclabs.jschema.type.EValue;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

import static com.relogiclabs.jschema.internal.library.ScriptLibrary.resolveStatic;
import static com.relogiclabs.jschema.internal.script.GFunction.CONSTRAINT_MARKER;
import static com.relogiclabs.jschema.internal.util.StringHelper.concat;
import static com.relogiclabs.jschema.message.ErrorCode.FUNS02;
import static com.relogiclabs.jschema.message.ErrorCode.FUNS03;
import static com.relogiclabs.jschema.message.ErrorCode.VARD01;
import static org.apache.commons.lang3.StringUtils.substringBefore;

public class ScopeContext {
    @Getter private final ScopeContext parent;
    private final Map<String, EValue> symbols;

    public ScopeContext(ScopeContext parent) {
        this.parent = parent;
        this.symbols = new HashMap<>();
    }

    public GReference addVariable(String name, EValue value) {
        if(symbols.containsKey(name)) throw new ScriptVariableException(VARD01,
                concat("Variable '", name, "' already defined in the scope"));
        var reference = new GReference(value);
        symbols.put(name, reference);
        return reference;
    }

    public void addFunction(String name, GFunction function) {
        if(symbols.containsKey(name)) {
            if(name.startsWith(CONSTRAINT_MARKER))
                throw failOnDuplicateDefinition(FUNS02, "Constraint", name);
            else throw failOnDuplicateDefinition(FUNS03, "Subroutine", name);
        }
        symbols.put(name, function);
    }

    private static ScriptFunctionException failOnDuplicateDefinition(String code,
                String functionType, String name) {
        return new ScriptFunctionException(code, concat(functionType, " function '",
                substringBefore(name, '#'), "' with matching parameter(s) already defined"));
    }

    public EValue resolve(String name) {
        var current = this;
        do {
            var value = current.symbols.get(name);
            if(value != null) return value;
            current = current.parent;
        } while(current != null);
        return resolveStatic(name);
    }

    public RuntimeContext getRuntime() {
        var current = this;
        while(current.parent != null) current = current.parent;
        return current.getRuntime();
    }

    public void update(String name, EValue value) {
        symbols.put(name, value);
    }
}
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

import static com.relogiclabs.jschema.internal.library.LibraryFunctions3.resolveStatic;
import static com.relogiclabs.jschema.internal.tree.EFunction.CONSTRAINT_PREFIX;
import static com.relogiclabs.jschema.message.ErrorCode.FNSDUP01;
import static com.relogiclabs.jschema.message.ErrorCode.FNSDUP02;
import static com.relogiclabs.jschema.message.ErrorCode.VARDUP01;
import static org.apache.commons.lang3.StringUtils.substringBefore;

public class ScriptScope {
    @Getter private final ScriptScope parent;
    private final Map<String, EValue> symbols;

    public ScriptScope(ScriptScope parent) {
        this.parent = parent;
        this.symbols = new HashMap<>();
    }

    public GLeftValue addVariable(String name, EValue value) {
        var lvalue = new GLeftValue(value);
        var oldValue = symbols.put(name, lvalue);
        if(oldValue == null) return lvalue;
        throw new DuplicateVariableException(VARDUP01,
            "Variable '" + name + "' already declared in the scope");
    }

    public void addFunction(String functionId, GFunction function) {
        var oldValue = symbols.put(functionId, function);
        if(oldValue == null) return;
        if(functionId.startsWith(CONSTRAINT_PREFIX))
            throw failOnDuplicateFunction(FNSDUP01, "Constraint", functionId);
        else throw failOnDuplicateFunction(FNSDUP02, "Subroutine", functionId);
    }

    private static DuplicateFunctionException failOnDuplicateFunction(String code,
                String functionType, String functionId) {
        return new DuplicateFunctionException(code, functionType + " function '"
            + substringBefore(functionId, '#') + "' with matching parameter(s) already defined");
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
package com.relogiclabs.jschema.internal.engine;

import com.relogiclabs.jschema.exception.ScriptFunctionException;
import com.relogiclabs.jschema.exception.ScriptVariableException;
import com.relogiclabs.jschema.internal.script.GFunction;
import com.relogiclabs.jschema.internal.script.GLeftValue;
import com.relogiclabs.jschema.tree.RuntimeContext;
import com.relogiclabs.jschema.type.EValue;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

import static com.relogiclabs.jschema.internal.library.ScriptLibrary3.resolveStatic;
import static com.relogiclabs.jschema.internal.tree.EFunction.CONSTRAINT_PREFIX;
import static com.relogiclabs.jschema.message.ErrorCode.FUND01;
import static com.relogiclabs.jschema.message.ErrorCode.FUND02;
import static com.relogiclabs.jschema.message.ErrorCode.VARD01;
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
        throw new ScriptVariableException(VARD01,
            "Variable '" + name + "' already defined in the scope");
    }

    public void addFunction(String name, GFunction function) {
        var oldValue = symbols.put(name, function);
        if(oldValue == null) return;
        if(name.startsWith(CONSTRAINT_PREFIX))
            throw failOnDuplicateDefinition(FUND01, "Constraint", name);
        else throw failOnDuplicateDefinition(FUND02, "Subroutine", name);
    }

    private static ScriptFunctionException failOnDuplicateDefinition(String code,
                String functionType, String name) {
        return new ScriptFunctionException(code, functionType + " function '"
            + substringBefore(name, '#') + "' with matching parameter(s) already defined");
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
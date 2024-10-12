package com.relogiclabs.jschema.internal.tree;

import com.relogiclabs.jschema.internal.loader.ScriptStorage;
import com.relogiclabs.jschema.internal.script.RFunction;
import com.relogiclabs.jschema.internal.script.RMethod;
import com.relogiclabs.jschema.tree.RuntimeContext;
import com.relogiclabs.jschema.type.EValue;
import lombok.Getter;

import static com.relogiclabs.jschema.internal.script.ScriptType.getCode;

@Getter
public class ScriptRegistry {
    private static final int INTERNAL_FUNCTION_CAPACITY = 10;
    private static final int INTERNAL_METHOD_CAPACITY = 200;
    private static final int EXTERNAL_FUNCTION_CAPACITY = 10;
    private static final int EXTERNAL_METHOD_CAPACITY = 50;

    private static final ScriptStorage INTERNALS = new ScriptStorage(INTERNAL_FUNCTION_CAPACITY,
                                                                     INTERNAL_METHOD_CAPACITY);
    private final ScriptStorage externals = new ScriptStorage(EXTERNAL_FUNCTION_CAPACITY,
                                                              EXTERNAL_METHOD_CAPACITY);
    private final RuntimeContext runtime;

    public ScriptRegistry(RuntimeContext runtime) {
        this.runtime = runtime;
    }

    public static ScriptStorage getInternals() {
        return INTERNALS;
    }

    public RFunction getFunction(String key1, String key2) {
        RFunction entry;
        if((entry = externals.getFunction(key1)) != null) return entry;
        if((entry = externals.getFunction(key2)) != null) return entry;
        if((entry = INTERNALS.getFunction(key1)) != null) return entry;
        return INTERNALS.getFunction(key2);
    }

    public RMethod getMethod(EValue self, String key1, String key2) {
        var code = getCode(self.getType());
        if(code == null) return null;
        var ck1 = code.concat(key1);
        var ck2 = code.concat(key2);
        RMethod entry;
        if((entry = externals.getMethod(ck1)) != null) return entry;
        if((entry = externals.getMethod(ck2)) != null) return entry;
        if((entry = INTERNALS.getMethod(ck1)) != null) return entry;
        return INTERNALS.getMethod(ck2);
    }
}
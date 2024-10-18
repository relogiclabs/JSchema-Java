package com.relogiclabs.jschema.library;

import com.relogiclabs.jschema.extension.ScriptMethods;
import com.relogiclabs.jschema.internal.library.NumberMethods1;

/**
 * {@code NumberMethods} is designed to provide public access to all internal number
 * methods of script.
 */
public class NumberMethods extends NumberMethods1 {
    public static NumberMethods getInstance(ScriptMethods methods) {
        var instance = methods.getRuntime().getImports().getInstance(NumberMethods.class);
        if(instance == null) throw new IllegalStateException("Instance not found for "
            + NumberMethods.class);
        instance.setContext(methods);
        return instance;
    }
}
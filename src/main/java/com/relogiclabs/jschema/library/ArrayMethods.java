package com.relogiclabs.jschema.library;

import com.relogiclabs.jschema.extension.ScriptMethods;
import com.relogiclabs.jschema.internal.library.ArrayMethods1;

/**
 * {@code ArrayMethods} is designed to provide public access to all internal array
 * methods of script.
 */
public class ArrayMethods extends ArrayMethods1 {
    public static ArrayMethods getInstance(ScriptMethods methods) {
        var instance = methods.getRuntime().getImports().getInstance(ArrayMethods.class);
        if(instance == null) throw new IllegalStateException("Instance not found for "
            + ArrayMethods.class);
        instance.setContext(methods);
        return instance;
    }
}
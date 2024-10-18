package com.relogiclabs.jschema.library;

import com.relogiclabs.jschema.extension.ScriptFunctions;
import com.relogiclabs.jschema.internal.library.GlobalFunctions1;

/**
 * {@code GlobalFunctions} is designed to provide public access to all internal library
 * functions of script.
 */
public class GlobalFunctions extends GlobalFunctions1 {
    public static GlobalFunctions getInstance(ScriptFunctions functions) {
        var instance = functions.getRuntime().getImports().getInstance(GlobalFunctions.class);
        if(instance == null) throw new IllegalStateException("Instance not found for "
            + GlobalFunctions.class);
        instance.setContext(functions);
        return instance;
    }
}
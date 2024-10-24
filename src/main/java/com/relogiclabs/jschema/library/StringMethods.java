package com.relogiclabs.jschema.library;

import com.relogiclabs.jschema.extension.ScriptMethods;
import com.relogiclabs.jschema.internal.library.StringMethods1;

/**
 * {@code StringMethods} is designed to provide public access to all internal string
 * methods of script.
 */
public class StringMethods extends StringMethods1 {
    public static StringMethods getInstance(ScriptMethods methods) {
        var instance = methods.getRuntime().getImports().getInstance(StringMethods.class);
        if(instance == null) throw new IllegalStateException("Instance not found for "
            + StringMethods.class);
        instance.setContext(methods);
        return instance;
    }
}
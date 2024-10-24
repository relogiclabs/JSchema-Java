package com.relogiclabs.jschema.library;

import com.relogiclabs.jschema.extension.ScriptMethods;
import com.relogiclabs.jschema.internal.library.CommonMethods1;

/**
 * {@code CommonMethods} is designed to provide public access to all internal common
 * methods of script.
 */
public class CommonMethods extends CommonMethods1 {
    public static CommonMethods getInstance(ScriptMethods methods) {
        var instance = methods.getRuntime().getImports().getInstance(CommonMethods.class);
        if(instance == null) throw new IllegalStateException("Instance not found for "
            + CommonMethods.class);
        instance.setContext(methods);
        return instance;
    }
}
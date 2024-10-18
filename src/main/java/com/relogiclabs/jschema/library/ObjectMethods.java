package com.relogiclabs.jschema.library;

import com.relogiclabs.jschema.extension.ScriptMethods;
import com.relogiclabs.jschema.internal.library.ObjectMethods1;

/**
 * {@code ObjectMethods} is designed to provide public access to all internal object
 * methods of script.
 */
public class ObjectMethods extends ObjectMethods1 {
    public static ObjectMethods getInstance(ScriptMethods methods) {
        var instance = methods.getRuntime().getImports().getInstance(ObjectMethods.class);
        if(instance == null) throw new IllegalStateException("Instance not found for "
            + ObjectMethods.class);
        instance.setContext(methods);
        return instance;
    }
}
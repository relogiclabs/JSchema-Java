package com.relogiclabs.jschema.function;

import com.relogiclabs.jschema.extension.ConstraintFunctions;
import com.relogiclabs.jschema.internal.function.SchemaFunctions5;

/**
 * {@code SchemaFunctions} is designed to provide public access to all internal constraint
 * functions.
 */
public class SchemaFunctions extends SchemaFunctions5 {
    public static SchemaFunctions getInstance(ConstraintFunctions functions) {
        var instance = functions.getRuntime().getImports().getInstance(SchemaFunctions.class);
        if(instance == null) throw new IllegalStateException("Instance not found for "
            + SchemaFunctions.class);
        instance.setContext(functions);
        return instance;
    }
}